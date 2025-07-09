package com.fraga.projectManager.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fraga.projectManager.data.enums.EStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProjectRelatoryDTO {
    @JsonProperty("projetos_por_status")
    private Map<EStatus, Long> projectsByStatus;
    @JsonProperty("orcamento_por_status")
    private Map<EStatus, BigDecimal> totalByStatus;
    @JsonProperty("duracao_media_projetos_finalizados")
    private Double averageDurationOfFinishedProjects;
    @JsonProperty("total_membros_unicos_por_projeto")
    private Long totalMembersOnlyOneProject;
}
