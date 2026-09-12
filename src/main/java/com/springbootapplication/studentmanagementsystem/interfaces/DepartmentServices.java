package com.springbootapplication.studentmanagementsystem.interfaces;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentStudentCountDTO;
import org.springframework.data.domain.Page;

import java.util.List;
public interface DepartmentServices {
    List<DepartmentResponseDTO> getAllDepartments();
    DepartmentResponseDTO addDepartment(DepartmentRequestDTO dto);
    DepartmentResponseDTO updateDepartment(Integer Id, DepartmentRequestDTO dto);
    void deleteDepartment(Integer Id);
    DepartmentResponseDTO getDepartmentById(Integer Id);
    List<DepartmentStudentCountDTO> getAllStudentCountByDepartment();
    DepartmentStudentCountDTO getStudentCountByDepartmentId(Integer departmentId);
    Page<DepartmentResponseDTO> getDepartmentsPaginated(int pageNumber, int pageSize);
    List<DepartmentResponseDTO> getAllDepartmentsSorted(String sortBy, String sortDirection);
    DepartmentResponseDTO searchByDepartmentName(String departmentName);
}
