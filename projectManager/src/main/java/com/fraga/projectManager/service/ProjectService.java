package com.fraga.projectManager.service;

import com.fraga.projectManager.data.dto.ProjectDTO;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

public interface ProjectService {

    ProjectDTO create(ProjectDTO projectDTO);
    ProjectDTO update(UUID projectId, ProjectDTO projectDTO);
    ProjectDTO getById(UUID projectId);
    void delete(UUID id);
    Set<ProjectDTO> getAll( Pageable pageable);
    String upStatus(UUID id);
    String cancelProject(UUID id);
    ProjectDTO addMembers(UUID projectId, Set<String> memberNames);
}
