package com.fraga.projectManager.controller;

import com.fraga.projectManager.controller.defaultController.DefaultController;
import com.fraga.projectManager.controller.defaultController.DefaultResponse;
import com.fraga.projectManager.data.dto.ProjectDTO;
import com.fraga.projectManager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@AllArgsConstructor
public class ProjectController implements DefaultController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<DefaultResponse<ProjectDTO>> create(
            @Valid
            @RequestBody ProjectDTO projectDTO
            ) {
        return success(projectService.create(projectDTO));
    }

    @GetMapping
    public ResponseEntity<DefaultResponse<Set<ProjectDTO>>> getAll(
            Pageable pageable
    ) {
        return success(projectService.getAll(pageable));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<DefaultResponse<ProjectDTO>> getById(
            @PathVariable UUID projectId
    ) {
        return success(projectService.getById(projectId));
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<DefaultResponse<ProjectDTO>> update(
            @PathVariable UUID projectId,
            @RequestBody ProjectDTO projectDTO
    ) {
        return success(projectService.update(projectId, projectDTO));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<DefaultResponse<String>> delete(
            @PathVariable UUID projectId
    ) {
        projectService.delete(projectId);
        return success("Projeto deletado com sucesso");
    }

    @PatchMapping("/status-up/{projectId}")
    public ResponseEntity<DefaultResponse<String>> upStatus(
            @PathVariable UUID projectId
    ) {
        return success(projectService.upStatus(projectId));
    }

    @PatchMapping("/project-cancel/{projectId}")
    public ResponseEntity<DefaultResponse<String>> cancelProject(
            @PathVariable UUID projectId
    ) {
        return success(projectService.cancelProject(projectId));
    }

    @PostMapping("/project-members/{projectId}")
    public ResponseEntity<DefaultResponse<ProjectDTO>> addMembers(
            @PathVariable UUID projectId,
            @RequestBody Set<String> memberNames
    ) {
        return success(projectService.addMembers(projectId, memberNames));
    }
}
