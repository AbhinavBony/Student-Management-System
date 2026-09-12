package com.springbootapplication.studentmanagementsystem.config;

import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentRequestDTO;
import com.springbootapplication.studentmanagementsystem.entity.Student;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentMapper {
    @Bean
    public ModelMapper mapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(StudentRequestDTO.class, Student.class).addMappings(m-> m.skip(Student::setId));
        return mapper;
    }
}
