package com.fraga.projectManager.service.impl;

import com.fraga.projectManager.data.dto.ProjectDTO;
import com.fraga.projectManager.data.entity.Member;
import com.fraga.projectManager.data.entity.Project;
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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
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

        var entity = mapper.map(projectDTO, Project.class);
        entity.setProjectManager(new HashSet<>());

        allocationMemberIsValid(entity, memberClient, findProjectsValidStatus());

        entity.setStatus(EStatus.IN_ANALISIS);

        Member savedMember = memberService.upsert(memberClient);
        entity.getProjectManager().add(savedMember);

        var project = projectRepository.save(entity);

        return mapper.map(project, ProjectDTO.class);
    }

    private Boolean allocationMemberIsValid(Project project, Member member, Set<Project> projectsValidStatus) {
        if (project.getProjectManager().size() >= MAX_ALLOCATION_MEMBER_IN_PROJECT) {
            throw new IlegalArgumentException("Project has reached the maximum number of allocations"
                    + MAX_ALLOCATION_MEMBER_IN_PROJECT);
        }

        Long memberAllocation = project.countActiveAllocationsByMember(member, projectsValidStatus);
        if (memberAllocation >= MAX_ALLOCATIONS_MEMBER) {
            throw new IlegalArgumentException("Member " + member.getName() +
                    " has reached the maximum number of allocations: " + MAX_ALLOCATIONS_MEMBER);
        }
        return true;
    }

    private Set<Project> findProjectsValidStatus() {
        return projectRepository.findByStatusIn(
                Set.of(EStatus.IN_PROGRESS,
                        EStatus.IN_ANALISIS,
                        EStatus.STARTED,
                        EStatus.DO_ANALISIS,
                        EStatus.PLANNED,
                        EStatus.APROVED_ANALISIS)
        );
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
        if (EStatus.CANCELLED.equals(status) || EStatus.COMPLETED.equals(status)) {
            throw new IlegalArgumentException("Cannot change status of a project that is already completed or cancelled");
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
        Set<Project> projectsValidStatus = findProjectsValidStatus();
        Set<Member> canditateMembers = members.stream()
                .filter(member -> allocationMemberIsValid(entity, member, projectsValidStatus))
                .collect(Collectors.toSet());
        entity.removeExistingMembers(canditateMembers);

        if (canditateMembers.isEmpty()){
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

}
