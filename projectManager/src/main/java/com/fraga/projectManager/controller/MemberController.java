package com.fraga.projectManager.controller;

import com.fraga.projectManager.controller.defaultController.DefaultController;
import com.fraga.projectManager.controller.defaultController.DefaultResponse;
import com.fraga.projectManager.data.dto.MemberDTO;
import com.fraga.projectManager.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@AllArgsConstructor
public class MemberController implements DefaultController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<DefaultResponse<MemberDTO>> create(
            @Valid
            @RequestBody MemberDTO memberDTO) {
        return success(memberService.create(memberDTO));
    }

    @GetMapping("/{memberName}")
    public ResponseEntity<DefaultResponse<MemberDTO>> getMemberByName(
            @PathVariable String memberName) {
        return success(memberService.getMemberByName(memberName));
    }
}
