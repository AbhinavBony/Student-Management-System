package com.springbootapplication.studentmanagementsystem.controller;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentStudentCountDTO;
import com.springbootapplication.studentmanagementsystem.interfaces.DepartmentServices;
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
public class DepartmentController {
    private final DepartmentServices service;

    public DepartmentController(DepartmentServices service) {
        this.service = service;
    }
    @GetMapping(value = "/departments")
    public ResponseEntity<List<DepartmentResponseDTO>> getAll(){
        List<DepartmentResponseDTO> list = this.service.getAllDepartments();
        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(list);
    }
    @PostMapping(value = "/departments")
    public ResponseEntity<DepartmentResponseDTO> add(@Valid @RequestBody DepartmentRequestDTO dto){
        DepartmentResponseDTO saved = this.service.addDepartment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @PutMapping(value = "/departments/{Id}")
    public ResponseEntity<DepartmentResponseDTO> update(@Positive(message = "Id can't be negative") @PathVariable Integer Id, @Valid @RequestBody DepartmentRequestDTO dto){
        DepartmentResponseDTO updated = this.service.updateDepartment(Id, dto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping(value = "/departments/{Id}")
    public ResponseEntity<?> delete(@Positive(message = "Id can't be negative") @PathVariable Integer Id){
        this.service.deleteDepartment(Id);
        return ResponseEntity.ok("Department Details Deleted Successfully!!");
    }
    @GetMapping(value = "/departments/{Id}")
    public ResponseEntity<DepartmentResponseDTO> getById(@Positive(message = "Id can't be negative") @PathVariable Integer Id){
        DepartmentResponseDTO foundById = this.service.getDepartmentById(Id);
        return ResponseEntity.status(HttpStatus.FOUND).body(foundById);
    }
    @GetMapping(value = "/departments/get-all-students-by-departments")
    public ResponseEntity<List<DepartmentStudentCountDTO>> getStudentByDepartment(){
        List<DepartmentStudentCountDTO> list = this.service.getAllStudentCountByDepartment();
        if (list.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(list);
    }
    @GetMapping(value = "/departments/get-students-by-departmentId/{departmentId}")
    public ResponseEntity<DepartmentStudentCountDTO> getStudentByDepartmentId(@Positive(message = "Id can't be negative") @PathVariable Integer departmentId){
        DepartmentStudentCountDTO countById = this.service.getStudentCountByDepartmentId(departmentId);
        return ResponseEntity.ok(countById);
    }
    @GetMapping(value = "/departments/paginated")
    public ResponseEntity<Page<DepartmentResponseDTO>> getPaginatedDepartment(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "3") int pageSize){
        Page<DepartmentResponseDTO> pages = this.service.getDepartmentsPaginated(pageNumber, pageSize);
        return ResponseEntity.ok(pages);
    }
    @GetMapping(value = "/departments/sorted")
    public ResponseEntity<List<DepartmentResponseDTO>> getSortedDepartments(@RequestParam(defaultValue = "departmentName") String sortBy, @RequestParam(defaultValue = "asc") String sortDirection){
        List<DepartmentResponseDTO> list = this.service.getAllDepartmentsSorted(sortBy, sortDirection);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/departments/searchByDepartmentName")
    public ResponseEntity<DepartmentResponseDTO> searchByDepName(@NotBlank(message = "Department name can't be blank empty") @RequestParam String departmentName){
        DepartmentResponseDTO foundByName = this.service.searchByDepartmentName(departmentName);
        return ResponseEntity.ok(foundByName);
    }
}