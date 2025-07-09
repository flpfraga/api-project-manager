package mocks;

import com.fraga.projectManager.data.dto.MemberDTO;
import com.fraga.projectManager.data.enums.EFunction;
import com.fraga.projectManager.data.model.Member;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MemberMocks {

    public static Member getMockMemberFunctionary() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        member.setName("Test Member");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static MemberDTO getMockMemberDTOFunctionary() {
        MemberDTO member = new MemberDTO();
        member.setName("Test Member");
        member.setFunction("FUNCIONARIO");
        return member;
    }

    public static Member getMockMemberNotFunctionary() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        member.setName("Test Member");
        member.setFunction(EFunction.ADMINISTRADOR);
        return member;
    }

    public static Member getMockMemberFunctionary_1() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        member.setName("Test Member 1");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_2() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        member.setName("Test Member 2");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_3() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));
        member.setName("Test Member 3");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_4() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174004"));
        member.setName("Test Member 4");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_5() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174005"));
        member.setName("Test Member 5");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_6() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174006"));
        member.setName("Test Member 6");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_7() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174007"));
        member.setName("Test Member 7");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_8() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174008"));
        member.setName("Test Member 8");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_9() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174009"));
        member.setName("Test Member 9");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }

    public static Member getMockMemberFunctionary_10() {
        Member member = new Member();
        member.setId(UUID.fromString("123e4567-e89b-12d3-a456-426614174010"));
        member.setName("Test Member 10");
        member.setFunction(EFunction.FUNCIONARIO);
        return member;
    }


    public static Set<Member> getMock10Members() {
        Set<Member> members = new HashSet<>();
        members.add(getMockMemberFunctionary_1());
        members.add(getMockMemberFunctionary_2());
        members.add(getMockMemberFunctionary_3());
        members.add(getMockMemberFunctionary_4());
        members.add(getMockMemberFunctionary_5());
        members.add(getMockMemberFunctionary_6());
        members.add(getMockMemberFunctionary_7());
        members.add(getMockMemberFunctionary_8());
        members.add(getMockMemberFunctionary_9());
        members.add(getMockMemberFunctionary_10());
        return members;
    }

}
