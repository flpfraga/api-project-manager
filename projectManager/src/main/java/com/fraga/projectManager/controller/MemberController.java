package com.fraga.projectManager.controller;

import com.fraga.projectManager.controller.defaultController.DefaultController;
import com.fraga.projectManager.controller.defaultController.DefaultResponse;
import com.fraga.projectManager.data.dto.MemberDTO;
import com.fraga.projectManager.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// OpenAPI imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1/members")
@AllArgsConstructor
@Tag(name = "Member", description = "Endpoints for managing members")
public class MemberController implements DefaultController {

    private final MemberService memberService;

    @PostMapping
    @Operation(summary = "Create a new member", description = "Creates a new member and returns the created member data.")
    public ResponseEntity<DefaultResponse<MemberDTO>> create(
            @Valid
            @RequestBody MemberDTO memberDTO) {
        return success(memberService.create(memberDTO));
    }

    @GetMapping("/{memberName}")
    @Operation(summary = "Get member by name", description = "Retrieves a member by their name.")
    public ResponseEntity<DefaultResponse<MemberDTO>> getMemberByName(
            @Parameter(description = "Name of the member", required = true)
            @PathVariable String memberName) {
        return success(memberService.getMemberByName(memberName));
    }
}
