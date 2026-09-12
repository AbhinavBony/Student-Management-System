package com.springbootapplication.studentmanagementsystem.DTO.StudentDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDTO {
    @Positive(message = "roll number can't be negative / zero!!")
    @NotNull(message = "roll number can't be blank / empty!!")
    private Long rollNumber;
    @NotBlank(message = "student name can't be blank / empty!!")
    private String name;
    @NotBlank(message = "college name can't be blank / empty!!")
    private String collegeName;

    @Positive(message = "department id can't be negative / zero!!")
    @NotNull(message = "department id can't be blank / empty!!")
    private Integer departmentId;
}
