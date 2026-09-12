package com.springbootapplication.studentmanagementsystem.DTO.StudentDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateRequestDTO {
    @NotBlank(message = "student name can't be blank / empty!!")
    private String name;
    @NotBlank(message = "college name can't be blank / empty!!")
    private String collegeName;
}
