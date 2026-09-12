package com.springbootapplication.studentmanagementsystem.interfaces;

import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.DepartmentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentUpdateRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentServices {
    List<StudentResponseDTO> getAllStudents();
    StudentResponseDTO updateStudentDetails(Long rollNumber, StudentUpdateRequestDTO dto);
    void deleteStudentDetails(Long rollNumber);
    StudentResponseDTO getStudentByRollNumber(Long rollNumber);
    StudentResponseDTO addStudentByDepartment(StudentRequestDTO dto);
    StudentResponseDTO updateStudentByDepartment(Long rollNumber, DepartmentUpdateRequestDTO dto);
    List<StudentResponseDTO> getStudentsByDepartment(Integer departmentId);
    Page<StudentResponseDTO> getAllStudentsPaginated(int pageNumber, int pageSize);
    List<StudentResponseDTO> getSortedStudents(String sortBy, String sortDir);
    List<StudentResponseDTO> searchByStudentName(String studentName);
}
