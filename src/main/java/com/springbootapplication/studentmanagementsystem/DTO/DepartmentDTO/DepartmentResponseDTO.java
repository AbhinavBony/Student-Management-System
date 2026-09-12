package com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponseDTO {
    private Integer Id;
    private String departmentName;
}
