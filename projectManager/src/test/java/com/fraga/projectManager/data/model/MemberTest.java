package com.fraga.projectManager.data.model;

import com.fraga.projectManager.data.enums.EFunction;
import mocks.MemberMocks;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Member member = MemberMocks.getMockMemberFunctionary();
        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"), member.getId());
        assertEquals("Test Member", member.getName());
        assertEquals(EFunction.FUNCIONARIO, member.getFunction());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        Member member = new Member(id, "Maria", EFunction.GERENTE);
        assertEquals(id, member.getId());
        assertEquals("Maria", member.getName());
        assertEquals(EFunction.GERENTE, member.getFunction());
    }

    @Test
    void testIsMemberFunctionaryTypeTrue() {
        Member member = MemberMocks.getMockMemberFunctionary();
        assertTrue(member.isMemberFunctionaryType());
    }

    @Test
    void testIsMemberFunctionaryTypeFalse() {
        Member member = MemberMocks.getMockMemberNotFunctionary();
        assertFalse(member.isMemberFunctionaryType());
    }

    @Test
    void testEqualsAndHashCode() {
        Member member1 = MemberMocks.getMockMemberFunctionary();
        Member member2 = MemberMocks.getMockMemberFunctionary();
        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());
    }
} 