package com.springbootapplication.studentmanagementsystem.DTO.StudentDTO;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO {
    private Integer Id;
    private Long rollNumber;
    private String name, collegeName;
    private DepartmentResponseDTO department;
}
