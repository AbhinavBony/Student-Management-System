package com.springbootapplication.studentmanagementsystem.controller;

import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.DepartmentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.interfaces.StudentServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/")
@Validated
public class StudentController {
    private final StudentServices service;

    public StudentController(StudentServices service) {
        this.service = service;
    }
    @GetMapping(value = "/students")
    public ResponseEntity<List<StudentResponseDTO>> getAll(){
        List<StudentResponseDTO> list = this.service.getAllStudents();
        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/students")
    public ResponseEntity<StudentResponseDTO> addStudentByDepartment(@Valid @RequestBody StudentRequestDTO dto){
        StudentResponseDTO addedByDepartment = this.service.addStudentByDepartment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedByDepartment);
    }
    @PutMapping(value = "/students/{rollNumber}")
    public ResponseEntity<StudentResponseDTO> update(@Positive(message = "Roll Number can't be negative / zero!!") @PathVariable Long rollNumber, @Valid @RequestBody StudentUpdateRequestDTO dto){
        StudentResponseDTO updated = this.service.updateStudentDetails(rollNumber, dto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping(value = "/students/{rollNumber}")
    public ResponseEntity<?> delete(@Positive(message = "Roll Number can't be negative / zero!!")@PathVariable Long rollNumber){
        this.service.deleteStudentDetails(rollNumber);
        return ResponseEntity.ok("Student Details Deleted Successfully!!!");
    }
    @GetMapping(value = "/students/{rollNumber}")
    public ResponseEntity<StudentResponseDTO> getByRollNumber(@Positive(message = "Roll Number can't be negative / zero!!")@PathVariable Long rollNumber){
        StudentResponseDTO found = this.service.getStudentByRollNumber(rollNumber);
        return ResponseEntity.status(HttpStatus.FOUND).body(found);
    }
    @PatchMapping(value = "/students/update-Department/{rollNumber}")
    public ResponseEntity<StudentResponseDTO> updateDepartment(@Positive(message = "Roll Number can't be negative / zero!!")@PathVariable Long rollNumber, @Valid @RequestBody DepartmentUpdateRequestDTO dto){
        StudentResponseDTO updatedDepartment = this.service.updateStudentByDepartment(rollNumber, dto);
        return ResponseEntity.ok(updatedDepartment);
    }
    @GetMapping(value = "/students/by-department/{departmentId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByDepartment(@Positive(message = "Department id can't be negative / zero!!")@PathVariable Integer departmentId){
        List<StudentResponseDTO> students = this.service.getStudentsByDepartment(departmentId);
        if (students.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(students);
    }
    @GetMapping(value = "/students/paginated")
    public ResponseEntity<Page<StudentResponseDTO>> allPagedStudents(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int pageSize){
        Page<StudentResponseDTO> pages = this.service.getAllStudentsPaginated(pageNumber, pageSize);
        if (pages.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(pages);
    }
    @GetMapping(value = "/students/sorted")
    public ResponseEntity<List<StudentResponseDTO>> allSortedStudents(@RequestParam(defaultValue = "name") @NotBlank(message = "Sort by value should be blank/empty!!") String sortBy, @RequestParam(defaultValue = "asc") @NotBlank(message = "Sort direction value should be blank/empty!!")String sortDir){
        List<StudentResponseDTO> sortedList = this.service.getSortedStudents(sortBy, sortDir);
        if (sortedList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(sortedList);
    }
    @GetMapping(value = "/students/search-by-name")
    public ResponseEntity<List<StudentResponseDTO>> searchByName(@NotBlank(message = "Student name can't be blank/empty!!!")@RequestParam String studentName){
        List<StudentResponseDTO> exists = this.service.searchByStudentName(studentName);
        if (exists.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(exists);
    }
}
