package mocks;

import com.fraga.projectManager.data.dto.ProjectDTO;
import com.fraga.projectManager.data.enums.EStatus;
import com.fraga.projectManager.data.model.Project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static mocks.MemberMocks.getMock10Members;
import static mocks.MemberMocks.getMockMemberFunctionary;

public class ProjectMocks {

    public static Project getMockProject(){
        Project project = new Project();
        project.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        project.setName("Test Project");
        project.setDescription("This is a test project.");
        project.setStatus(EStatus.IN_ANALISIS);
        project.setStartDate(LocalDate.parse("2023-01-01"));
        project.setRealEndDate(LocalDate.parse("2023-12-31"));
        project.setExpectedEndDate(LocalDate.parse("2023-12-31"));
        project.setProjectManager(Set.of(getMockMemberFunctionary()));
        project.setTotal(BigDecimal.TEN);
        return project;
    }

    public static Project getMockProject1(){
        Project project = new Project();
        project.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        project.setName("Test Project 1");
        project.setDescription("This is a test project.");
        project.setStatus(EStatus.IN_ANALISIS);
        project.setStartDate(LocalDate.parse("2023-01-01"));
        project.setRealEndDate(LocalDate.parse("2023-12-31"));
        project.setProjectManager(Set.of(getMockMemberFunctionary()));
        return project;
    }

    public static Project getMockProject2(){
        Project project = new Project();
        project.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        project.setName("Test Project 2");
        project.setDescription("This is a test project.");
        project.setStatus(EStatus.IN_ANALISIS);
        project.setStartDate(LocalDate.parse("2023-01-01"));
        project.setRealEndDate(LocalDate.parse("2023-12-31"));
        project.setProjectManager(Set.of(getMockMemberFunctionary()));
        return project;
    }

    public static Set<Project> getMock3ProjectsSameMember(){
        return Set.of(getMockProject(), getMockProject1(),getMockProject2());
    }

    public static Project getMockProjectWithMaxMember(){
        Project project = new Project();
        project.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        project.setName("Test Project");
        project.setDescription("This is a test project.");
        project.setStatus(EStatus.IN_ANALISIS);
        project.setStartDate(LocalDate.parse("2023-01-01"));
        project.setRealEndDate(LocalDate.parse("2023-12-31"));
        project.setProjectManager(getMock10Members());
        return project;
    }

    public static ProjectDTO getMockProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        projectDTO.setName("Test Project");
        projectDTO.setDescription("This is a test project.");
        projectDTO.setStatus(EStatus.IN_ANALISIS);
        projectDTO.setStartDate(LocalDate.parse("2023-01-01"));
        projectDTO.setRealEndDate(LocalDate.parse("2023-12-31"));
        return projectDTO;
    }
}
