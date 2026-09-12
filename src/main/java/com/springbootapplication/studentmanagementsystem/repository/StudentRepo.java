package com.springbootapplication.studentmanagementsystem.repository;

import com.springbootapplication.studentmanagementsystem.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<Student, Integer> {
    Optional<Student> findByRollNumber(Long rollNumber);
    @EntityGraph(attributePaths = "department")
    List<Student> findByDepartment_Id(Integer departmentId);


    @EntityGraph(attributePaths = "department")
    @Query("SELECT s FROM Student s")
    List<Student> findAllStudents();

    @EntityGraph(attributePaths = "department")
    Page<Student> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "department")
    List<Student> findAll(Sort sort);

    @EntityGraph(attributePaths = "department")
    List<Student> findByNameContainingIgnoreCase(String studentName);
}
