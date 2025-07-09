package com.fraga.projectManager.data.model;

import com.fraga.projectManager.data.enums.EFunction;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Member member = new Member();
        UUID id = UUID.randomUUID();
        member.setId(id);
        member.setName("João");
        member.setFunction(EFunction.FUNCIONARIO);

        assertEquals(id, member.getId());
        assertEquals("João", member.getName());
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
        Member member = new Member();
        member.setFunction(EFunction.FUNCIONARIO);
        assertTrue(member.isMemberFunctionaryType());
    }

    @Test
    void testIsMemberFunctionaryTypeFalse() {
        Member member = new Member();
        member.setFunction(EFunction.GERENTE);
        assertFalse(member.isMemberFunctionaryType());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Member member1 = new Member(id, "João", EFunction.FUNCIONARIO);
        Member member2 = new Member(id, "João", EFunction.FUNCIONARIO);
        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());
    }
} 