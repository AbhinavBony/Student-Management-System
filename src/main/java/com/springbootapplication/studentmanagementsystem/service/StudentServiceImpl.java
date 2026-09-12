package com.springbootapplication.studentmanagementsystem.service;

import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.DepartmentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.entity.Department;
import com.springbootapplication.studentmanagementsystem.entity.Student;
import com.springbootapplication.studentmanagementsystem.exception.DepartmentNotFound;
import com.springbootapplication.studentmanagementsystem.exception.DuplicateStudentFound;
import com.springbootapplication.studentmanagementsystem.exception.StudentNotFound;
import com.springbootapplication.studentmanagementsystem.interfaces.StudentServices;
import com.springbootapplication.studentmanagementsystem.repository.DepartmentRepo;
import com.springbootapplication.studentmanagementsystem.repository.StudentRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentServices {
    private final ModelMapper mapper;
    private final StudentRepo stdRepo;
    private final DepartmentRepo depRepo;

    public StudentServiceImpl(ModelMapper mapper, StudentRepo stdRepo, DepartmentRepo depRepo) {
        this.mapper = mapper;
        this.stdRepo = stdRepo;
        this.depRepo = depRepo;
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return stdRepo.findAllStudents().stream().map(student-> this.mapper.map(student, StudentResponseDTO.class)).toList();
    }

    @Override
    @Transactional
    public StudentResponseDTO addStudentByDepartment(StudentRequestDTO dto) {
        stdRepo.findByRollNumber(dto.getRollNumber()).ifPresent((student -> {
            throw new DuplicateStudentFound("Student already exists with roll number: "+ dto.getRollNumber());
        }));
        Department department = depRepo.findById(dto.getDepartmentId()).orElseThrow(()-> new DepartmentNotFound("Department not found with department id: "+ dto.getDepartmentId()));
        Student student = this.mapper.map(dto, Student.class);
        student.setDepartment(department);
        Student saved = stdRepo.save(student);
        return this.mapper.map(saved, StudentResponseDTO.class);
    }

    @Override
    @Transactional
    public StudentResponseDTO updateStudentDetails(Long rollNumber, StudentUpdateRequestDTO dto) {
        Student found = stdRepo.findByRollNumber(rollNumber).orElseThrow(()-> new StudentNotFound("Student not found with roll number: "+ rollNumber));
        this.mapper.map(dto, found);
        Student update = stdRepo.save(found);
        return this.mapper.map(update, StudentResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteStudentDetails(Long rollNumber) {
        Student existsById = stdRepo.findByRollNumber(rollNumber).orElseThrow(()-> new StudentNotFound("student not found with roll number: "+ rollNumber));
        stdRepo.delete(existsById);
    }

    @Override
    public StudentResponseDTO getStudentByRollNumber(Long rollNumber) {
        Student foundByRollNumber = stdRepo.findByRollNumber(rollNumber).orElseThrow(()-> new StudentNotFound("Student not found with roll number: "+ rollNumber));
        return this.mapper.map(foundByRollNumber, StudentResponseDTO.class);
    }
    @Override
    @Transactional
    public StudentResponseDTO updateStudentByDepartment(Long rollNumber, DepartmentUpdateRequestDTO dto) {
        Student found = stdRepo.findByRollNumber(rollNumber).orElseThrow(()-> new StudentNotFound("Student not found with roll number: "+ rollNumber));
        Department department = depRepo.findById(dto.getDepartmentId()).orElseThrow(()-> new DepartmentNotFound("Department not found with id: "+ dto.getDepartmentId()));
        found.setDepartment(department);
        Student update = stdRepo.save(found);
        return this.mapper.map(update, StudentResponseDTO.class);
    }

    @Override
    public List<StudentResponseDTO> getStudentsByDepartment(Integer departmentId) {
        depRepo.findById(departmentId).orElseThrow(()-> new DepartmentNotFound("Department not found with Id: "+ departmentId));

        List<Student> students = stdRepo.findByDepartment_Id(departmentId);
        return students.stream().map(student -> this.mapper.map(student, StudentResponseDTO.class)).toList();
    }

    @Override
    public Page<StudentResponseDTO> getAllStudentsPaginated(int pageNumber, int pageSize) {
        Pageable pages = PageRequest.of(pageNumber, pageSize);
        Page<Student> studentPage = stdRepo.findAll(pages);
        return studentPage.map(student -> this.mapper.map(student, StudentResponseDTO.class));
    }

    @Override
    public List<StudentResponseDTO> getSortedStudents(String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        List<Student> list = stdRepo.findAll(sort);
        return list.stream().map(student -> this.mapper.map(student, StudentResponseDTO.class)).toList();
    }

    @Override
    public List<StudentResponseDTO> searchByStudentName(String studentName) {
        return stdRepo.findByNameContainingIgnoreCase(studentName).stream().map(student -> this.mapper.map(student, StudentResponseDTO.class)).toList();
    }
}