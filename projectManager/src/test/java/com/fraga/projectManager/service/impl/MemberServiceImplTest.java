package com.fraga.projectManager.service.impl;

import com.fraga.projectManager.data.dto.MemberDTO;
import com.fraga.projectManager.data.model.Member;
import com.fraga.projectManager.data.enums.EFunction;
import com.fraga.projectManager.exception.ResourceNotFoundException;
import com.fraga.projectManager.httpClient.member.MemberHttpClient;
import com.fraga.projectManager.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.*;
import reactor.core.publisher.Mono;

import java.util.*;

import static mocks.MemberMocks.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @Mock
    private MemberHttpClient memberHttpClient;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        memberService = new MemberServiceImpl(memberHttpClient, memberRepository, mapper);
    }

    @Test
    void testGetFunctionaryMemberByNameWithSucess() {
        Member member = getMockMemberFunctionary();
        when(memberHttpClient.getMemberByName("Test Member")).thenReturn(Mono.just(member));

        Member result = memberService.getValidProjectMemberByName("Test Member");
        assertEquals(member, result);
    }

    @Test
    void testGetMemberByNameExceptionNotFunctionary() {
        Member member = getMockMemberNotFunctionary();
        when(memberHttpClient.getMemberByName("Test Member")).thenReturn(Mono.just(member));

        assertThrows(ResourceNotFoundException.class, () ->
                memberService.getValidProjectMemberByName("Test Member"));
    }

    @Test
    void testGetMemberByNameWithSucess() {
        Member member = getMockMemberFunctionary();
        MemberDTO dto = getMockMemberDTOFunctionary();
        when(memberHttpClient.getMemberByName("Test Member")).thenReturn(Mono.just(member));
        when(mapper.map(member, MemberDTO.class)).thenReturn(dto);

        MemberDTO result = memberService.getMemberByName("Test Member");
        assertEquals(dto, result);
    }

    @Test
    void testCreateMemberWithSucess() {
        Member member = getMockMemberFunctionary();
        MemberDTO dto = getMockMemberDTOFunctionary();
        when(mapper.map(dto, Member.class)).thenReturn(member);
        when(memberHttpClient.sendMember(member)).thenReturn(Mono.just(member));
        when(mapper.map(member, MemberDTO.class)).thenReturn(dto);

        MemberDTO result = memberService.create(dto);
        assertEquals(dto, result);
    }

    @Test
    void testUpsertExitingMemberWithSucess() {
        Member member = getMockMemberFunctionary();
        member.setName("Test Member");
        when(memberRepository.findByNameAndFunction("Test Member", EFunction.FUNCIONARIO)).thenReturn(Optional.of(member));

        Member result = memberService.upsert(member);
        assertEquals(member, result);
        verify(memberRepository, never()).save(any());
    }

    @Test
    void testUpsertNotExitingMemberWithSucess() {
        Member member = getMockMemberFunctionary();
        member.setName("Test Member");
        when(memberRepository.findByNameAndFunction("Test Member", EFunction.FUNCIONARIO)).thenReturn(Optional.empty());
        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.upsert(member);
        assertEquals(member, result);
    }

    @Test
    void testUpsertManyMembersWithSucess() {
        Member member1 = getMockMemberFunctionary();
        Member member2 = getMockMemberFunctionary_1();
        Set<Member> input = new HashSet<>(Arrays.asList(member1, member2));

        Set<Member> persisted = new HashSet<>(Collections.singletonList(member1));
        Set<Member> toPersist = new HashSet<>(Collections.singletonList(member2));
        List<Member> saved = new ArrayList<>(Collections.singletonList(member2));

        when(memberRepository.findByNameInAndFunction(anySet(), eq(EFunction.FUNCIONARIO))).thenReturn(persisted);
        when(memberRepository.saveAll(toPersist)).thenReturn(saved);

        Set<Member> result = memberService.upsert(input);
        assertTrue(result.contains(member1));
        assertTrue(result.contains(member2));
    }

    @Test
    void testGetMembersWithSucess() {
        Member member1 = getMockMemberFunctionary();
        Member member2 = getMockMemberFunctionary_1();

        when(memberHttpClient.getMemberByName("Test Member")).thenReturn(Mono.just(member1));
        when(memberHttpClient.getMemberByName("Test Member 1")).thenReturn(Mono.just(member2));

        Set<Member> result = memberService.getMembers(new HashSet<>(Arrays.asList("Test Member", "Test Member 1")));
        assertEquals(2, result.size());
        assertTrue(result.contains(member1));
        assertTrue(result.contains(member2));
    }

    @Test
    void testGetMemberByNameExceptionNullMember() {
        when(memberHttpClient.getMemberByName("Test Member")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                memberService.getValidProjectMemberByName("Test Member")
        );

    }

    @Test
    void testGetMemberByNameExceptionClientReturn() throws Throwable {
        when(memberHttpClient.getMemberByName("X")).thenReturn(Mono.error(new RuntimeException()));

        assertThrows(ResourceNotFoundException.class, () -> {

            try {
                var method = MemberServiceImpl.class.getDeclaredMethod("getValidMemberByName", String.class);
                method.setAccessible(true);
                method.invoke(memberService, "X");
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }
} 