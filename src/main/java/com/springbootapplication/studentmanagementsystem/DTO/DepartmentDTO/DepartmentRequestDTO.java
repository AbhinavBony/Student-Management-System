package com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequestDTO {
    @NotBlank(message = "Department name can't be empty / blank!!")
    private String departmentName;
}
