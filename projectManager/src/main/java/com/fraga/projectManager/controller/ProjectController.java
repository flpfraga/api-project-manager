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
// OpenAPI imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/v1/projects")
@AllArgsConstructor
@Tag(name = "Project", description = "Endpoints for managing projects")
public class ProjectController implements DefaultController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project and returns the created project data.")
    public ResponseEntity<DefaultResponse<ProjectDTO>> create(
            @Valid
            @RequestBody ProjectDTO projectDTO
            ) {
        return success(projectService.create(projectDTO));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves all projects with pagination support.")
    public ResponseEntity<DefaultResponse<Set<ProjectDTO>>> getAll(
            Pageable pageable
    ) {
        return success(projectService.getAll(pageable));
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project by ID", description = "Retrieves a project by its unique identifier.")
    public ResponseEntity<DefaultResponse<ProjectDTO>> getById(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable UUID projectId
    ) {
        return success(projectService.getById(projectId));
    }

    @PutMapping("/update/{projectId}")
    @Operation(summary = "Update a project", description = "Updates an existing project by its ID.")
    public ResponseEntity<DefaultResponse<ProjectDTO>> update(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable UUID projectId,
            @RequestBody ProjectDTO projectDTO
    ) {
        return success(projectService.update(projectId, projectDTO));
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Delete a project", description = "Deletes a project by its ID.")
    public ResponseEntity<DefaultResponse<String>> delete(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable UUID projectId
    ) {
        projectService.delete(projectId);
        return success("Projeto deletado com sucesso");
    }

    @PatchMapping("/status-up/{projectId}")
    @Operation(summary = "Upgrade project status", description = "Upgrades the status of a project by its ID.")
    public ResponseEntity<DefaultResponse<String>> upStatus(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable UUID projectId
    ) {
        return success(projectService.upStatus(projectId));
    }

    @PatchMapping("/project-cancel/{projectId}")
    @Operation(summary = "Cancel a project", description = "Cancels a project by its ID.")
    public ResponseEntity<DefaultResponse<String>> cancelProject(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable UUID projectId
    ) {
        return success(projectService.cancelProject(projectId));
    }

    @PostMapping("/project-members/{projectId}")
    @Operation(summary = "Add members to project", description = "Adds members to a project by its ID.")
    public ResponseEntity<DefaultResponse<ProjectDTO>> addMembers(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable UUID projectId,
            @RequestBody Set<String> memberNames
    ) {
        return success(projectService.addMembers(projectId, memberNames));
    }
}
