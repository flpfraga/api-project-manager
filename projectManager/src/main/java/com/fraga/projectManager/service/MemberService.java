package com.fraga.projectManager.service;

import com.fraga.projectManager.data.dto.MemberDTO;
import com.fraga.projectManager.data.model.Member;

import java.util.Set;

/**
 * Service interface for managing members.
 */
public interface MemberService {

    /**
     * Retrieves a valid project member by name, ensuring the member is a functionary.
     * @param memberName the name of the member
     * @return the valid Member entity
     */
    Member getValidProjectMemberByName(String memberName);

    /**
     * Creates a new member.
     * @param memberDTO the member data transfer object
     * @return the created MemberDTO
     */
    MemberDTO create(MemberDTO memberDTO);

    /**
     * Retrieves a member by name.
     * @param memberName the name of the member
     * @return the MemberDTO
     */
    MemberDTO getMemberByName(String memberName);

    /**
     * Updates or inserts a member.
     * @param member the member entity
     * @return the upserted Member entity
     */
    Member upsert(Member member);

    /**
     * Updates or inserts a set of members.
     * @param members the set of members
     * @return the set of upserted members
     */
    Set<Member> upsert(Set<Member> members);

    /**
     * Retrieves a set of members by their names.
     * @param memberNames the set of member names
     * @return the set of Member entities
     */
    Set<Member> getMembers(Set<String> memberNames);
}
