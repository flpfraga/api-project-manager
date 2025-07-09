package com.fraga.projectManager.data.model;

import com.fraga.projectManager.data.enums.EStatus;
import com.fraga.projectManager.data.enums.ERiskClassification;
import mocks.ProjectMocks;
import mocks.MemberMocks;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        Project project = ProjectMocks.getMockProject();
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), project.getId());
        assertEquals("Test Project", project.getName());
        assertEquals("This is a test project.", project.getDescription());
        assertEquals(EStatus.IN_ANALISIS, project.getStatus());
        assertEquals(LocalDate.parse("2023-01-01"), project.getStartDate());
        assertEquals(LocalDate.parse("2023-12-31"), project.getRealEndDate());
        assertEquals(BigDecimal.TEN, project.getTotal());
        assertEquals(Set.of(MemberMocks.getMockMemberFunctionary()), project.getProjectManager());
    }

    @Test
    void testUpStatus() {
        Project project = ProjectMocks.getMockProject();
        project.setStatus(EStatus.IN_ANALISIS);
        project.upStatus();
        assertEquals(EStatus.IN_ANALISIS.getNext(), project.getStatus());
    }

    @Test
    void testIsInForbidenDeleteStatus() {
        Project project = ProjectMocks.getMockProject();
        project.setStatus(EStatus.STARTED);
        assertTrue(project.isInForbidenDeleteStatus());
        project.setStatus(EStatus.IN_PROGRESS);
        assertTrue(project.isInForbidenDeleteStatus());
        project.setStatus(EStatus.COMPLETED);
        assertTrue(project.isInForbidenDeleteStatus());
        project.setStatus(EStatus.IN_ANALISIS);
        assertFalse(project.isInForbidenDeleteStatus());
    }

    @Test
    void testCountActiveAllocationsByMember() {
        Member member = MemberMocks.getMockMemberFunctionary();
        Project project1 = ProjectMocks.getMockProject();
        project1.setProjectManager(new HashSet<>(Set.of(member)));
        Project project2 = ProjectMocks.getMockProject1();
        project2.setProjectManager(new HashSet<>());
        Set<Project> validProjects = Set.of(project1, project2);
        Project project = ProjectMocks.getMockProject();
        long count = project.countActiveAllocationsByMember(member, validProjects);
        assertEquals(1, count);
    }

    @Test
    void testRemoveExistingMembers() {
        Member member1 = MemberMocks.getMockMemberFunctionary();
        Member member2 = MemberMocks.getMockMemberNotFunctionary();
        Set<Member> managers = new HashSet<>(Set.of(member1));
        Project project = ProjectMocks.getMockProject();
        project.setProjectManager(managers);
        Set<Member> toRemove = new HashSet<>(Set.of(member1, member2));
        project.removeExistingMembers(toRemove);
        assertFalse(toRemove.contains(member1));
        assertTrue(toRemove.contains(member2));
    }

    @Test
    void testSetStatusAndRealEndDate() {
        Project project = ProjectMocks.getMockProject();
        project.setStatus(EStatus.CANCELLED);
        assertEquals(EStatus.CANCELLED, project.getStatus());
        assertNotNull(project.getRealEndDate());
        project.setStatus(EStatus.COMPLETED);
        assertEquals(EStatus.COMPLETED, project.getStatus());
        assertNotNull(project.getRealEndDate());
    }

    @Test
    void getRisk_shouldReturnHigh_whenTotalAbove500kOrMonthsAbove6() {
        Project project = ProjectMocks.getMockProject();
        project.setTotal(new BigDecimal("600000"));
        project.setStartDate(LocalDate.now());
        project.setExpectedEndDate(LocalDate.now().plusMonths(7));
        assertEquals(ERiskClassification.HIGH, project.getRisk());
    }

    @Test
    void getRisk_shouldReturnLow_whenTotalBelow100kOrMonthsBelow3() {
        Project project = ProjectMocks.getMockProject();
        project.setTotal(new BigDecimal("90000"));
        project.setStartDate(LocalDate.now());
        project.setExpectedEndDate(LocalDate.now().plusMonths(2));
        assertEquals(ERiskClassification.LOW, project.getRisk());
    }

    @Test
    void getRisk_shouldReturnMedium_whenNotHighOrLow() {
        Project project = ProjectMocks.getMockProject();
        project.setTotal(new BigDecimal("200000"));
        project.setStartDate(LocalDate.now());
        project.setExpectedEndDate(LocalDate.now().plusMonths(4));
        assertEquals(ERiskClassification.MEDIUM, project.getRisk());
    }
} 