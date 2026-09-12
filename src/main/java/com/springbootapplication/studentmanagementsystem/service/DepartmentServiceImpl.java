package com.springbootapplication.studentmanagementsystem.service;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentStudentCountDTO;
import com.springbootapplication.studentmanagementsystem.entity.Department;
import com.springbootapplication.studentmanagementsystem.exception.DepartmentNotFound;
import com.springbootapplication.studentmanagementsystem.exception.DuplicateDepartmentFound;
import com.springbootapplication.studentmanagementsystem.interfaces.DepartmentServices;
import com.springbootapplication.studentmanagementsystem.repository.DepartmentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentServiceImpl implements DepartmentServices {
    private final ModelMapper mapper;
    private final DepartmentRepo depRepo;

    public DepartmentServiceImpl(ModelMapper mapper, DepartmentRepo depRepo) {
        this.mapper = mapper;
        this.depRepo = depRepo;
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return depRepo.findAll().stream().map(department -> this.mapper.map(department, DepartmentResponseDTO.class)).toList();
    }

    @Override
    public DepartmentResponseDTO addDepartment(DepartmentRequestDTO dto) {
        depRepo.findByDepartmentNameContaining(dto.getDepartmentName()).ifPresent((department -> {
            throw new DuplicateDepartmentFound("Department already exists with department name: "+ dto.getDepartmentName());
        }));
        Department department = this.mapper.map(dto, Department.class);
        Department saved = depRepo.save(department);
        return this.mapper.map(saved, DepartmentResponseDTO.class);
    }

    @Override
    public DepartmentResponseDTO updateDepartment(Integer Id, DepartmentRequestDTO dto) {
        Department found = depRepo.findById(Id).orElseThrow(()-> new DepartmentNotFound("Department not found with department id: "+ Id));
        this.mapper.map(dto, found);
        Department update = this.depRepo.save(found);
        return this.mapper.map(update, DepartmentResponseDTO.class);
    }

    @Override
    public void deleteDepartment(Integer Id) {
        if (!depRepo.existsById(Id)){
            throw new DepartmentNotFound("Department not found with department id: "+ Id);
        }
        depRepo.deleteById(Id);
    }

    @Override
    public DepartmentResponseDTO getDepartmentById(Integer Id) {
        Department exists = depRepo.findById(Id).orElseThrow(()-> new DepartmentNotFound("Department not found with department id: "+ Id));
        return this.mapper.map(exists, DepartmentResponseDTO.class);
    }

    @Override
    public List<DepartmentStudentCountDTO> getAllStudentCountByDepartment() {
        return depRepo.getStudentCountPerDepartment();
    }

    @Override
    public DepartmentStudentCountDTO getStudentCountByDepartmentId(Integer departmentId) {
        DepartmentStudentCountDTO result = depRepo.getStudentCountByDepartmentId(departmentId);
        if (result== null){
            throw new DepartmentNotFound("Department not found with id: "+ departmentId);
        }
        return result;
    }

    @Override
    public Page<DepartmentResponseDTO> getDepartmentsPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Department> departmentPage = depRepo.findAll(pageable);
        return departmentPage.map(department -> this.mapper.map(department, DepartmentResponseDTO.class));
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartmentsSorted(String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        List<Department> departmentList = depRepo.findAll(sort);
        return departmentList.stream().map(department -> this.mapper.map(department, DepartmentResponseDTO.class)).toList();
    }

    @Override
    public DepartmentResponseDTO searchByDepartmentName(String departmentName) {
        Department existsByName = depRepo.findByDepartmentNameContainingIgnoreCase(departmentName).orElseThrow(()-> new DepartmentNotFound("Department doesn't exists with name: "+ departmentName));
        return this.mapper.map(existsByName, DepartmentResponseDTO.class);
    }
}
