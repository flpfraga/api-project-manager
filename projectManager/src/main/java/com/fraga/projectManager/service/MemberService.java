package com.fraga.projectManager.service;

import com.fraga.projectManager.data.dto.MemberDTO;
import com.fraga.projectManager.data.entity.Member;

import java.util.Set;

public interface MemberService {

    Member getValidProjectMemberByName(String memberName);

    MemberDTO create(MemberDTO memberDTO);

    MemberDTO getMemberByName(String memberName);

    Member upsert(Member member);

    Set<Member> upsert(Set<Member> members);

    Set<Member> getMembers(Set<String> memberNames);
}
