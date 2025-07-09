package com.fraga.projectManager.config;

import com.fraga.projectManager.data.dto.ProjectDTO;
import com.fraga.projectManager.data.model.Project;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.typeMap(ProjectDTO.class, Project.class)
                .addMappings(map -> {
                    map.skip(Project::setStatus);
                });
        return new ModelMapper();
    }
}
