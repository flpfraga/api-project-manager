package com.fraga.projectManager.service;

import com.fraga.projectManager.data.dto.ProjectDTO;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

/**
 * Service interface for managing projects.
 */
public interface ProjectService {

    /**
     * Creates a new project.
     * @param projectDTO the project data transfer object
     * @return the created ProjectDTO
     */
    ProjectDTO create(ProjectDTO projectDTO);

    /**
     * Updates an existing project by its ID.
     * @param projectId the project unique identifier
     * @param projectDTO the project data transfer object
     * @return the updated ProjectDTO
     */
    ProjectDTO update(UUID projectId, ProjectDTO projectDTO);

    /**
     * Retrieves a project by its ID.
     * @param projectId the project unique identifier
     * @return the ProjectDTO
     */
    ProjectDTO getById(UUID projectId);

    /**
     * Deletes a project by its ID.
     * @param id the project unique identifier
     */
    void delete(UUID id);

    /**
     * Retrieves all projects with pagination support.
     * @param pageable the pagination information
     * @return a set of ProjectDTOs
     */
    Set<ProjectDTO> getAll(Pageable pageable);

    /**
     * Upgrades the status of a project by its ID.
     * @param id the project unique identifier
     * @return the new status as a String
     */
    String upStatus(UUID id);

    /**
     * Cancels a project by its ID.
     * @param id the project unique identifier
     * @return the cancelled status as a String
     */
    String cancelProject(UUID id);

    /**
     * Adds members to a project by its ID.
     * @param projectId the project unique identifier
     * @param memberNames the set of member names to add
     * @return the updated ProjectDTO
     */
    ProjectDTO addMembers(UUID projectId, Set<String> memberNames);
}
