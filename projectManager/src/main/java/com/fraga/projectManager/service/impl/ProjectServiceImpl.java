package com.fraga.projectManager.service.impl;

import com.fraga.projectManager.data.dto.ProjectDTO;
import com.fraga.projectManager.data.dto.ProjectRelatoryDTO;
import com.fraga.projectManager.data.enums.ERiskClassification;
import com.fraga.projectManager.data.model.Member;
import com.fraga.projectManager.data.model.Project;
import com.fraga.projectManager.data.enums.EStatus;
import com.fraga.projectManager.exception.IlegalArgumentException;
import com.fraga.projectManager.exception.ResourceNotFoundException;
import com.fraga.projectManager.repository.ProjectRepository;
import com.fraga.projectManager.service.MemberService;
import com.fraga.projectManager.service.ProjectService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.fraga.projectManager.constants.MemberAllocationConstants.MAX_ALLOCATIONS_MEMBER;
import static com.fraga.projectManager.constants.MemberAllocationConstants.MAX_ALLOCATION_MEMBER_IN_PROJECT;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberService memberService;
    private final ModelMapper mapper;

    @Override
    public ProjectDTO create(ProjectDTO projectDTO) {
        var memberClient = memberService.getValidProjectMemberByName(projectDTO.getManagerName());

        var projectEntity = mapper.map(projectDTO, Project.class);
        projectEntity.setProjectManager(new HashSet<>());

        verifyMemberCanBeAllocade(projectEntity, memberClient, findProjectsNotFinalStatus());

        projectEntity.setStatus(EStatus.IN_ANALISIS);

        Member savedMember = memberService.upsert(memberClient);
        projectEntity.getProjectManager().add(savedMember);

        var project = projectRepository.save(projectEntity);

        return mapper.map(project, ProjectDTO.class);
    }

    private Boolean verifyMemberCanBeAllocade(Project project, Member member, Set<Project> projectsValidStatus) {
        Long memberAllocation = project.countActiveAllocationsByMember(member, projectsValidStatus);
        if (memberAllocation >= MAX_ALLOCATIONS_MEMBER) {
            throw new IlegalArgumentException("Member " + member.getName() +
                    " has reached the maximum number of allocations: " + MAX_ALLOCATIONS_MEMBER);
        }
        return true;
    }

    private Set<Project> findProjectsNotFinalStatus() {
        return projectRepository.findByStatusIn(EStatus.notFinalStatus());
    }

    @Override
    public ProjectDTO getById(UUID projectId) {
        return projectRepository.findById(projectId)
                .map(project -> mapper.map(project, ProjectDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
    }

    @Override
    public Set<ProjectDTO> getAll(Pageable pageable) {
        var projects = projectRepository.findAll(pageable);
        return projects.stream()
                .map(project -> mapper.map(project, ProjectDTO.class))
                .collect(Collectors.toSet());
    }

    public ProjectDTO update(UUID projectId, ProjectDTO projectDTO) {
        var entity = getProject(projectId);

        mapper.map(projectDTO, entity);
        var project = projectRepository.save(entity);

        return mapper.map(project, ProjectDTO.class);

    }

    public void delete(UUID projectId) {
        var entity = getProject(projectId);

        if (entity.isInForbidenDeleteStatus()) {
            throw new IlegalArgumentException(
                    "Cannot delete a project that is not in a deletable status. " +
                            "Current status: " + entity.getStatus());
        }
        projectRepository.delete(entity);
    }

    public String upStatus(UUID projectId) {
        var entity = getProject(projectId);

        EStatus status = entity.getStatus();
        if (EStatus.finalStatus().contains(status)) {
            throw new IlegalArgumentException(
                    "Cannot change status of a project that is already completed or cancelled");
        }
        entity.upStatus();
        projectRepository.save(entity);
        return entity.getStatus().getStatus();
    }

    public String cancelProject(UUID projectId) {
        var entity = getProject(projectId);

        entity.setStatus(EStatus.CANCELLED);

        projectRepository.save(entity);
        return entity.getStatus().getStatus();
    }

    @Override
    public ProjectDTO addMembers(UUID projectId, Set<String> memberNames) {
        var entity = getProject(projectId);

        Set<Member> members = memberService.getMembers(memberNames);
        Set<Project> projectsValidStatus = findProjectsNotFinalStatus();

        Set<Member> canditateMembers = members.stream()
                .filter(member -> verifyMemberCanBeAllocade(entity, member, projectsValidStatus))
                .collect(Collectors.toSet());

        entity.removeExistingMembers(canditateMembers);
        if (canditateMembers.isEmpty()) {
            return mapper.map(entity, ProjectDTO.class);
        }

        if ((entity.getProjectManager().size() + canditateMembers.size()) > MAX_ALLOCATION_MEMBER_IN_PROJECT) {
            throw new IlegalArgumentException("Cannot add more than " +
                    MAX_ALLOCATION_MEMBER_IN_PROJECT + " members to a project");
        }

        Set<Member> savedMembers = memberService.upsert(canditateMembers);
        entity.getProjectManager().addAll(savedMembers);
        var project = projectRepository.save(entity);

        return mapper.map(project, ProjectDTO.class);
    }

    private Project getProject(UUID projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));
    }

    @Override
    public ERiskClassification evaluateProjectRisk(UUID projectId) {
        var entity = getProject(projectId);
        if (ObjectUtils.isEmpty(entity.getTotal())) {
            throw new IllegalStateException("The project does not have a total cost defined, cannot evaluate risk.");
        }
        return entity.getRisk();
    }

    @Override
    public ProjectRelatoryDTO getProjectsRelatory() {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("No projects found for relatory.");
        }
        ProjectRelatoryDTO relatory = new ProjectRelatoryDTO();
        relatory.setProjectsByStatus(getStatusCount(projects));
        relatory.setTotalByStatus(getTotalByStatus(projects));
        relatory.setAverageDurationOfFinishedProjects(getAverageDurationOfFinishedProjects(projects));
        relatory.setTotalMembersOnlyOneProject(getTotalMembersOnlyOneProject(projects));

        return relatory;
    }

    private Map<EStatus, Long> getStatusCount(List<Project> projects) {
        return projects.stream()
                .collect(Collectors.groupingBy(Project::getStatus, Collectors.counting()));
    }

    private Map<EStatus, BigDecimal> getTotalByStatus(List<Project> projects) {
        return projects.stream()
                .collect(Collectors.groupingBy(Project::getStatus,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Project::getTotal,
                                BigDecimal::add)));
    }

    private Double getAverageDurationOfFinishedProjects(List<Project> projects) {
        return projects.stream()
                .filter(project -> EStatus.finalStatus().contains(project.getStatus()))
                .mapToLong(project -> ChronoUnit.DAYS.between(project.getStartDate(), project.getRealEndDate()))
                .average()
                .orElse(0L);
    }

    private Long getTotalMembersOnlyOneProject(List<Project> projects) {
        return projects.stream()
                .flatMap(project -> project.getProjectManager().stream())
                .collect(Collectors.groupingBy(Member::getId, Collectors.counting()))
                .values().stream()
                .filter(count -> count == 1)
                .count();
    }

}
