package org.khanhpham.todo.config;

import org.khanhpham.todo.entity.Task;
import org.khanhpham.todo.payload.dto.TaskDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.addMappings(new PropertyMap<Task, TaskDTO>() {
            @Override
            protected void configure() {
                map().setCreatedDate(String.valueOf(source.getCreatedDate()));
                map().setUpdatedDate(String.valueOf(source.getUpdatedDate()));
            }
        });

        return modelMapper;
    }
}
