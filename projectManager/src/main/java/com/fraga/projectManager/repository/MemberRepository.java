package com.fraga.projectManager.repository;

import com.fraga.projectManager.data.model.Member;
import com.fraga.projectManager.data.enums.EFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByNameAndFunction(String name, EFunction function);
    Set<Member> findByNameInAndFunction(Set<String> names, EFunction function);
}
