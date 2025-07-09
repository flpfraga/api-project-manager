package com.fraga.projectManager.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fraga.projectManager.data.enums.EStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class ProjectDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty("nome")
    @NotBlank(message = "Project name is required")
    private String name;

    @JsonProperty("descricao")
    @NotBlank(message = "Description is required")
    private String description;

    @JsonProperty("data_inicio")
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @JsonProperty("data_previsao_termino")
    @NotNull(message = "Expected end date is required")
    private LocalDate expectedEndDate;

    @JsonProperty("orcamento_total")
    @NotNull(message = "Total budget is required")
    private BigDecimal total;

    @JsonProperty(value = "gerente_responsavel", access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Manager name is required")
    private String managerName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<MemberDTO> projectManager;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EStatus status;

    @JsonProperty(value= "data_real_termino",access = JsonProperty.Access.READ_ONLY)
    private LocalDate realEndDate;
}
