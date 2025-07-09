package com.fraga.projectManager.repository;

import com.fraga.projectManager.data.model.Project;
import com.fraga.projectManager.data.enums.EStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Set<Project> findByStatusIn(Set<EStatus> statuses);
}
