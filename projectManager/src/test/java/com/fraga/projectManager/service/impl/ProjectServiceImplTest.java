package com.fraga.projectManager.service.impl;

import com.fraga.projectManager.data.dto.ProjectDTO;
import com.fraga.projectManager.data.dto.ProjectRelatoryDTO;
import com.fraga.projectManager.data.enums.ERiskClassification;
import com.fraga.projectManager.data.enums.EStatus;
import com.fraga.projectManager.data.model.Member;
import com.fraga.projectManager.data.model.Project;
import com.fraga.projectManager.exception.IlegalArgumentException;
import com.fraga.projectManager.exception.ResourceNotFoundException;
import com.fraga.projectManager.repository.ProjectRepository;
import com.fraga.projectManager.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static mocks.MemberMocks.getMock10Members;
import static mocks.MemberMocks.getMockMemberFunctionary;
import static mocks.ProjectMocks.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectService = new ProjectServiceImpl(projectRepository, memberService, mapper);
    }

    @Test
    void testCreateProjectWithSucess() {
        Member member = getMockMemberFunctionary();
        ProjectDTO dto = getMockProjectDTO();
        dto.setManagerName("manager");
        Project entity = getMockProject();
        entity.setProjectManager(new HashSet<>());

        when(memberService.getValidProjectMemberByName("manager")).thenReturn(member);
        when(mapper.map(dto, Project.class)).thenReturn(entity);
        when(projectRepository.findByStatusIn(EStatus.notFinalStatus())).thenReturn(new HashSet<>());
        when(memberService.upsert(member)).thenReturn(member);
        when(projectRepository.save(entity)).thenReturn(entity);
        when(mapper.map(entity, ProjectDTO.class)).thenReturn(dto);

        ProjectDTO result = projectService.create(dto);
        assertEquals(dto, result);
    }

    @Test
    void testCreateProjectEXceptionMaxMember() {
        Member member = getMockMemberFunctionary();
        ProjectDTO dto = getMockProjectDTO();
        dto.setManagerName("manager");
        Project entity = getMockProjectWithMaxMember();
        entity.setProjectManager(new HashSet<>());

        when(memberService.getValidProjectMemberByName("manager")).thenReturn(member);
        when(mapper.map(dto, Project.class)).thenReturn(entity);
        when(projectRepository.findByStatusIn(EStatus.notFinalStatus())).thenReturn(getMock3ProjectsSameMember());

        assertThrows(IlegalArgumentException.class, () -> projectService.create(dto));
    }

    @Test
    void testeGetProjectByIdWithSucess() {
        UUID id = UUID.randomUUID();
        Project project = getMockProject();
        ProjectDTO dto = getMockProjectDTO();
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(mapper.map(project, ProjectDTO.class)).thenReturn(dto);

        ProjectDTO result = projectService.getById(id);
        assertEquals(dto, result);
    }

    @Test
    void testeGetProjectByIdExcptionNotFound() {
        UUID id = UUID.randomUUID();
        when(projectRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> projectService.getById(id));
    }

    @Test
    void testeGetAllProjectsWithSucess() {
        Pageable pageable = mock(Pageable.class);
        Project project = getMockProject();
        ProjectDTO dto = getMockProjectDTO();
        List<Project> projects = List.of(project);
        when(projectRepository.findAll(pageable)).thenReturn(new PageImpl<>(projects));
        when(mapper.map(project, ProjectDTO.class)).thenReturn(dto);

        Set<ProjectDTO> result = projectService.getAll(pageable);
        assertTrue(result.contains(dto));
    }

    @Test
    void testeUpdateProjectWithSucess() {
        UUID id = UUID.randomUUID();
        ProjectDTO dto = getMockProjectDTO();
        Project entity = getMockProject();
        Project saved = getMockProject1();

        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));
        when(projectRepository.save(entity)).thenReturn(saved);
        when(mapper.map(saved, ProjectDTO.class)).thenReturn(dto);

        ProjectDTO result = projectService.update(id, dto);
        assertEquals(dto, result);
    }

    @Test
    void testDeleteProjectWithSucess() {
        UUID id = UUID.randomUUID();
        Project entity = mock(Project.class);
        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));
        when(entity.isInForbidenDeleteStatus()).thenReturn(false);

        projectService.delete(id);
        verify(projectRepository).delete(entity);
    }

    @Test
    void testDeleteProjectExceptionFinalizedStatus() {
        UUID id = UUID.randomUUID();
        Project entity = getMockProject();
        entity.setStatus(EStatus.STARTED);
        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));

        assertThrows(IlegalArgumentException.class, () -> projectService.delete(id));
    }

    @Test
    void testUpStatusProjectWithSucess() {
        UUID id = UUID.randomUUID();
        Project entity = getMockProject();
        entity.setStatus(EStatus.IN_ANALISIS);
        Project saved = getMockProject();
        saved.setStatus(EStatus.DO_ANALISIS);
        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));
        when(projectRepository.save(any())).thenReturn(saved);

        String result = projectService.upStatus(id);
        verify(projectRepository).save(entity);
        assertEquals("Do analisis", result);
    }

    @Test
    void testUpStatusProjectExceptionFinalizedStatus() {
        UUID id = UUID.randomUUID();
        Project entity = getMockProject();
        entity.setStatus(EStatus.COMPLETED);
        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));

        assertThrows(IlegalArgumentException.class, () -> projectService.upStatus(id));
    }

    @Test
    void cancelProject_shouldSetStatusCancelled() {
        UUID id = UUID.randomUUID();
        Project entity = getMockProject();
        Project saved = getMockProject();
        saved.setStatus(EStatus.CANCELLED);
        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));
        when(projectRepository.save(any())).thenReturn(saved);

        String result = projectService.cancelProject(id);
        verify(projectRepository).save(saved);
        assertEquals("Cancelled", result);
    }

    @Test
    void testAddMemberToProject() {
        UUID id = UUID.randomUUID();
        Project entity = getMockProject();
        Set<String> memberNames = Set.of("A");
        Set<Member> members = Set.of(getMockMemberFunctionary());

        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));
        when(memberService.getMembers(memberNames)).thenReturn(members);

        when(projectRepository.findByStatusIn(EStatus.notFinalStatus())).thenReturn(new HashSet<>());

        when(memberService.upsert(anySet())).thenReturn(members);
        when(projectRepository.save(entity)).thenReturn(entity);
        when(mapper.map(entity, ProjectDTO.class)).thenReturn(new ProjectDTO());

        ProjectDTO result = projectService.addMembers(id, memberNames);
        assertNotNull(result);
    }


    @Test
    void testAddMemberToProjectExceptionMaxAllocation() {
        UUID id = UUID.randomUUID();
        Project entity = getMockProject();
        Set<String> memberNames = Set.of("A");
        Set<Member> members = getMock10Members();

        when(projectRepository.findById(id)).thenReturn(Optional.of(entity));
        when(memberService.getMembers(memberNames)).thenReturn(members);

        when(projectRepository.findByStatusIn(EStatus.notFinalStatus())).thenReturn(new HashSet<>());

        when(memberService.upsert(anySet())).thenReturn(members);
        when(projectRepository.save(entity)).thenReturn(entity);
        when(mapper.map(entity, ProjectDTO.class)).thenReturn(new ProjectDTO());

        assertThrows(IlegalArgumentException.class, ()->
            projectService.addMembers(id, memberNames));
    }

    @Test
    void testEvaluateProjectRiskWithSucess() {
        UUID id = UUID.randomUUID();
        Project project = getMockProject();

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        ERiskClassification result = projectService.evaluateProjectRisk(id);
        assertEquals(ERiskClassification.HIGH, result);
    }

    @Test
    void testEvaluateProjectRiskExceptionTotalIsNull() {
        UUID id = UUID.randomUUID();
        Project project = getMockProject();
        project.setTotal(null);
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        assertThrows(IllegalStateException.class, () -> projectService.evaluateProjectRisk(id));
    }

    @Test
    void getProjectsRelatory_shouldReturnRelatory() {
        Project project = getMockProject();
        List<Project> projects = List.of(project);
        when(projectRepository.findAll()).thenReturn(projects);
        ProjectRelatoryDTO relatory = projectService.getProjectsRelatory();
        assertNotNull(relatory);
    }

    @Test
    void getProjectsRelatory_shouldThrow_whenNoProjects() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(com.fraga.projectManager.exception.ResourceNotFoundException.class, () -> projectService.getProjectsRelatory());
    }
} 