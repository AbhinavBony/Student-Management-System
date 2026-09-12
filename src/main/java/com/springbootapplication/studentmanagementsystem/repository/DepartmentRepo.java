package com.springbootapplication.studentmanagementsystem.repository;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentStudentCountDTO;
import com.springbootapplication.studentmanagementsystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer> {
    Optional<Department> findByDepartmentNameContaining(String departmentName);

    Optional<Department> findByDepartmentNameContainingIgnoreCase(String departmentName);
    @Query("""
        SELECT new com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentStudentCountDTO(
            d.id, d.departmentName, COUNT(s.id))
        FROM Department d
        LEFT JOIN d.studentList s
        GROUP BY d.id, d.departmentName
        """)
    List<DepartmentStudentCountDTO> getStudentCountPerDepartment();

    @Query("""
    SELECT new com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentStudentCountDTO(
        d.id, d.departmentName, COUNT(s.id))
    FROM Department d
    LEFT JOIN d.studentList s
    WHERE d.id = :departmentId
    GROUP BY d.id, d.departmentName
    """)
    DepartmentStudentCountDTO getStudentCountByDepartmentId(@Param("departmentId") Integer departmentId);
}
