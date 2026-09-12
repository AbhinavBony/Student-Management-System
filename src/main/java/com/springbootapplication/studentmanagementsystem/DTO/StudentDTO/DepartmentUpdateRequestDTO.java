package com.springbootapplication.studentmanagementsystem.DTO.StudentDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentUpdateRequestDTO {
    @Positive(message = "department id can't be negative / zero!!")
    @NotNull(message = "department id can't be null!!")
    private Integer departmentId;
}
