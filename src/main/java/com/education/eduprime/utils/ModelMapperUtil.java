package com.education.eduprime.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtil {
    @Bean
    public ModelMapper modelMapperUtility() {
        return new ModelMapper();
    }
}
