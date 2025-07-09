package com.fraga.projectManager.data.model;

import com.fraga.projectManager.data.enums.EStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static mocks.MemberMocks.getMockMemberFunctionary;
import static mocks.ProjectMocks.getMockProject;
import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Project project = getMockProject();
        Set<Member> managers = Set.of(getMockMemberFunctionary());
        project.setProjectManager(managers);

        assertEquals("Test Project", project.getName());
        assertEquals("This is a test project.", project.getDescription());
        assertEquals(EStatus.IN_ANALISIS, project.getStatus());
        assertEquals(BigDecimal.TEN, project.getTotal());
        assertEquals(managers, project.getProjectManager());
    }

    @Test
    void testUpStatus() {
        Project project = new Project();
        project.setStatus(EStatus.IN_ANALISIS);
        project.upStatus();
        assertEquals(EStatus.DO_ANALISIS, project.getStatus());
    }

    @Test
    void testIsInForbidenDeleteStatus() {
        Project project = new Project();
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
        Member member = new Member(UUID.randomUUID(), "João", null);
        Project project1 = new Project();
        project1.setProjectManager(new HashSet<>(Set.of(member)));
        Project project2 = new Project();
        project2.setProjectManager(new HashSet<>());
        Set<Project> validProjects = Set.of(project1, project2);
        Project project = new Project();
        long count = project.countActiveAllocationsByMember(member, validProjects);
        assertEquals(1, count);
    }

    @Test
    void testRemoveExistingMembers() {
        Member member1 = new Member(UUID.randomUUID(), "João", null);
        Member member2 = new Member(UUID.randomUUID(), "Maria", null);
        Set<Member> managers = new HashSet<>(Set.of(member1));
        Project project = new Project();
        project.setProjectManager(managers);
        Set<Member> toRemove = new HashSet<>(Set.of(member1, member2));
        project.removeExistingMembers(toRemove);
        assertFalse(toRemove.contains(member1));
        assertTrue(toRemove.contains(member2));
    }

    @Test
    void testSetStatusAndRealEndDate() {
        Project project = new Project();
        project.setStatus(EStatus.CANCELLED);
        assertEquals(EStatus.CANCELLED, project.getStatus());
        assertNotNull(project.getRealEndDate());
        project.setStatus(EStatus.COMPLETED);
        assertEquals(EStatus.COMPLETED, project.getStatus());
        assertNotNull(project.getRealEndDate());
    }
} 