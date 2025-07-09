package com.fraga.projectManager.service.impl;

import com.fraga.projectManager.data.dto.MemberDTO;
import com.fraga.projectManager.data.model.Member;
import com.fraga.projectManager.data.enums.EFunction;
import com.fraga.projectManager.exception.ResourceNotFoundException;
import com.fraga.projectManager.httpClient.member.MemberHttpClient;
import com.fraga.projectManager.repository.MemberRepository;
import com.fraga.projectManager.service.MemberService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static com.fraga.projectManager.constants.CacheConstants.PREFIX_KEY_MEMBER;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberHttpClient memberHttpClient;
    private final MemberRepository memberRepository;
    private final ModelMapper mapper;

    @Override
    @Cacheable(PREFIX_KEY_MEMBER)
    public Member getValidProjectMemberByName(String memberName) {
        Member member = getValidMemberByName(memberName);
        if (!Objects.requireNonNull(member).isMemberFunctionaryType()) {
            throw new ResourceNotFoundException("Member not be a functionary." + memberName);
        }

        return member;
    }

    @Override
    public MemberDTO getMemberByName(String memberName) {
        return mapper.map(getValidMemberByName(memberName), MemberDTO.class);
    }

    @Override
    public MemberDTO create(MemberDTO memberDTO) {
        Member member = mapper.map(memberDTO, Member.class);
        var entity = memberHttpClient.sendMember(member).block();
        return mapper.map(entity, MemberDTO.class);
    }

    @Override
    public Member upsert(Member member) {
        return memberRepository.findByNameAndFunction(member.getName(), EFunction.FUNCIONARIO)
                .orElseGet(() ->
                        memberRepository.save(member));
    }

    @Override
    public Set<Member> upsert(Set<Member> members) {
        Set<Member> persistedFunctionaryMembers = memberRepository.findByNameInAndFunction(
                members.stream().map(Member::getName).collect(Collectors.toSet()), EFunction.FUNCIONARIO);

        Set<Member> notPersistedFunctionaryMembers = members.stream()
                .filter(member -> !persistedFunctionaryMembers.contains(member))
                .collect(Collectors.toSet());
        persistedFunctionaryMembers.addAll(memberRepository.saveAll(notPersistedFunctionaryMembers));
        return persistedFunctionaryMembers;
    }

    private Member getValidMemberByName(String memberName) {
        Mono<Member> memberResponse = memberHttpClient.getMemberByName(memberName);
        if (!ObjectUtils.isEmpty(memberResponse)) {
            try {
                Member member = memberResponse.block();
                if (memberName.equalsIgnoreCase(member.getName())) {
                    return member;
                }
            } catch (Exception e) {
                throw new ResourceNotFoundException("Member not found with name: " + memberName, e);
            }

        }
        throw new ResourceNotFoundException("Member not found with name: " + memberName);
    }

    @Override
    public Set<Member> getMembers(Set<String> memberNames) {
        Set<Member> members = Collections.synchronizedSet(new HashSet<>());
        Flux.fromIterable(memberNames)
                .flatMap(name -> memberHttpClient.getMemberByName(name)
                                .doOnNext(members::add)
                                .onErrorResume(ex -> Mono.empty())
                        , 5)
                .blockLast();
        return members;
    }
}
