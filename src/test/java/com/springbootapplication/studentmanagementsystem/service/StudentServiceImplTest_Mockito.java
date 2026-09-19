package com.springbootapplication.studentmanagementsystem.service;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.DepartmentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.entity.Department;
import com.springbootapplication.studentmanagementsystem.entity.Student;
import com.springbootapplication.studentmanagementsystem.exception.DepartmentNotFound;
import com.springbootapplication.studentmanagementsystem.exception.DuplicateStudentFound;
import com.springbootapplication.studentmanagementsystem.exception.StudentNotFound;
import com.springbootapplication.studentmanagementsystem.repository.DepartmentRepo;
import com.springbootapplication.studentmanagementsystem.repository.StudentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest_Mockito {
    @Mock
    private StudentRepo stdRepo;
    @Mock
    private ModelMapper mapper;
    @Mock
    private DepartmentRepo depRepo;

    @InjectMocks
    private StudentServiceImpl studentService;

    Student student;
    StudentRequestDTO requestDTO;
    StudentResponseDTO responseDTO;
    Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1);
        department.setDepartmentName("Computer Science");
        student = new Student();
        student.setRollNumber(10151002L);
        student.setName("Abhinav Srivastava");
        student.setCollegeName("IMS Engineering College");
        student.setDepartment(department);

        requestDTO = new StudentRequestDTO();
        requestDTO.setRollNumber(10151002L);
        requestDTO.setName("Abhinav Srivastava");
        requestDTO.setCollegeName("IMS Engineering College");
        requestDTO.setDepartmentId(department.getId());

        responseDTO = new StudentResponseDTO();
        responseDTO.setRollNumber(10151002L);
        responseDTO.setName("Abhinav Srivastava");
        responseDTO.setCollegeName("IMS Engineering College");
        responseDTO.setDepartment(new DepartmentResponseDTO(department.getId(), department.getDepartmentName()));

    }
    @Test
    void getAllStudents_shouldReturnList(){
        when(stdRepo.findAllStudents()).thenReturn(List.of(student));
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(responseDTO);

        List<StudentResponseDTO> list = studentService.getAllStudents();

        assertEquals(1, list.size());
        assertEquals(responseDTO.getName(), list.getFirst().getName());
        verify(stdRepo, times(1)).findAllStudents();
    }
    @Test
    void getAllStudents_whenListIsEmpty(){
        List<StudentResponseDTO> list = studentService.getAllStudents();

        assertTrue(list.isEmpty());
    }
    @Test
    void addStudentWithDepartment_shouldSaveAndReturn(){
        Student mappedStudent = new Student();
        mappedStudent.setRollNumber(requestDTO.getRollNumber());
        mappedStudent.setName(requestDTO.getName());
        mappedStudent.setCollegeName(requestDTO.getCollegeName());

        when(stdRepo.findByRollNumber(requestDTO.getRollNumber())).thenReturn(Optional.empty());
        when(depRepo.findById(requestDTO.getDepartmentId())).thenReturn(Optional.of(department));
        when(mapper.map(requestDTO, Student.class)).thenReturn(mappedStudent);
        when(stdRepo.save(any(Student.class))).thenReturn(mappedStudent);
        when(mapper.map(mappedStudent, StudentResponseDTO.class)).thenReturn(responseDTO);

        StudentResponseDTO saved = studentService.addStudentByDepartment(requestDTO);
        assertNotNull(saved);
        assertEquals(responseDTO.getName(), saved.getName());
        assertEquals(department, mappedStudent.getDepartment());
        verify(stdRepo, times(1)).save(mappedStudent);
    }
    @Test
    void addStudentWithDepartment_whenDuplicateRollNumberExists(){
        when(stdRepo.findByRollNumber(requestDTO.getRollNumber())).thenReturn(Optional.of(student));
        assertThrows(DuplicateStudentFound.class, ()-> studentService.addStudentByDepartment(requestDTO));

        verify(depRepo, never()).findById(anyInt());
        verify(stdRepo, never()).save(any());
    }
    @Test
    void addStudentWithDepartment_whenDepartmentNotFound(){
        when(stdRepo.findByRollNumber(requestDTO.getRollNumber())).thenReturn(Optional.empty());
        when(depRepo.findById(requestDTO.getDepartmentId())).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFound.class, ()-> studentService.addStudentByDepartment(requestDTO));

        verify(stdRepo, never()).save(any());

    }
    @Test
    void updateStudent_shouldUpdateDetails_whenRollNumberFound(){
        StudentUpdateRequestDTO updateDTO = new StudentUpdateRequestDTO();
        updateDTO.setName("Abhilasha Trivedi");
        updateDTO.setCollegeName("ABESIT");

        when(stdRepo.findByRollNumber(student.getRollNumber())).thenReturn(Optional.of(student));
        doAnswer(invocation -> {
           student.setName(updateDTO.getName());
           student.setCollegeName(updateDTO.getCollegeName());
           return null;
        }).when(mapper).map(updateDTO, student);
        when(stdRepo.save(student)).thenReturn(student);

        StudentResponseDTO updatedResponse = new StudentResponseDTO();
        updatedResponse.setRollNumber(student.getRollNumber());
        updatedResponse.setName(updateDTO.getName());
        updatedResponse.setCollegeName(updateDTO.getCollegeName());
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(updatedResponse);

        StudentResponseDTO studentUpdated = studentService.updateStudentDetails(student.getRollNumber(), updateDTO);

        assertNotNull(studentUpdated);

        assertEquals(updateDTO.getName(), studentUpdated.getName());
        assertEquals(updateDTO.getCollegeName(), studentUpdated.getCollegeName());

        verify(mapper, times(1)).map(updateDTO, student);
        verify(stdRepo, times(1)).save(student);
    }
    @Test
    void updateStudent_shouldThrowException_whenRollNumberNotFound(){
        StudentUpdateRequestDTO updateDTO = new StudentUpdateRequestDTO();
        when(stdRepo.findByRollNumber(999L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFound.class, ()-> studentService.updateStudentDetails(999L,updateDTO));

        verify(stdRepo, never()).save(any());
    }
    @Test
    void deleteStudentDetails_whenFound(){
        when(stdRepo.findByRollNumber(student.getRollNumber())).thenReturn(Optional.of(student));
        studentService.deleteStudentDetails(student.getRollNumber());

        verify(stdRepo, times(1)).delete(student);
        verify(stdRepo, times(1)).findByRollNumber(student.getRollNumber());
    }
    @Test
    void deleteStudentDetails_whenNotFound(){
        when(stdRepo.findByRollNumber(9999L)).thenReturn(Optional.empty());
        assertThrows(StudentNotFound.class, ()-> studentService.deleteStudentDetails(9999L));

        verify(stdRepo, never()).delete(any());
    }
    @Test
    void getStudentByRollNumber_shouldReturnDetails_whenExists(){
        when(stdRepo.findByRollNumber(student.getRollNumber())).thenReturn(Optional.of(student));
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(responseDTO);

        StudentResponseDTO found = studentService.getStudentByRollNumber(student.getRollNumber());

        assertNotNull(found);
        assertEquals(student.getName(), found.getName());
        assertEquals(student.getRollNumber(), found.getRollNumber());

        verify(stdRepo, times(1)).findByRollNumber(student.getRollNumber());
    }
    @Test
    void getStudentByRollNumber_shouldThrowException_whenNotFound(){
        when(stdRepo.findByRollNumber(9999L)).thenReturn(Optional.empty());

        assertThrows(StudentNotFound.class, ()-> studentService.getStudentByRollNumber(9999L));

        verify(stdRepo, times(1)).findByRollNumber(9999L);

    }
    @Test
    void updateStudentByDepartment_shouldUpdateStudentDepartment_whenStudentFound() {
        Department updatedDepartment = new Department();
        updatedDepartment.setId(2);
        updatedDepartment.setDepartmentName("Information Technology");

        DepartmentUpdateRequestDTO updateDTO = new DepartmentUpdateRequestDTO();
        updateDTO.setDepartmentId(updatedDepartment.getId());

        when(stdRepo.findByRollNumber(student.getRollNumber())).thenReturn(Optional.of(student));
        when(depRepo.findById(updatedDepartment.getId())).thenReturn(Optional.of(updatedDepartment));
        when(stdRepo.save(student)).thenReturn(student);
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(responseDTO);

        StudentResponseDTO updated = studentService.updateStudentByDepartment(student.getRollNumber(), updateDTO);
        assertNotNull(updated);
        assertEquals(updatedDepartment, student.getDepartment());
        verify(stdRepo, times(1)).save(student);
    }
    @Test
    void updateStudentByDepartment_shouldThrowException_whenStudentNotFound(){
        DepartmentUpdateRequestDTO updateDto = new DepartmentUpdateRequestDTO();
        updateDto.setDepartmentId(1);

        when(stdRepo.findByRollNumber(999L)).thenReturn(Optional.empty());
        assertThrows(StudentNotFound.class, ()-> studentService.updateStudentByDepartment(999L, updateDto));

        verify(depRepo, never()).findById(any());
        verify(stdRepo, never()).save(any());
    }
    @Test
    void getStudentByDepartment_shouldReturnStudent_whenDepartmentExists(){
        when(depRepo.findById(department.getId())).thenReturn(Optional.of(department));
        when(stdRepo.findByDepartment_Id(department.getId())).thenReturn(List.of(student));
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(responseDTO);

        List<StudentResponseDTO> list = studentService.getStudentsByDepartment(department.getId());

        assertEquals(1, list.size());
        assertEquals(responseDTO.getName(), list.getFirst().getName());
    }
    @Test
    void getStudentByDepartment_shouldReturnStudent_whenDepartmentIdNotExists(){
        when(depRepo.findById(9999)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFound.class, ()-> studentService.getStudentsByDepartment(9999));
        verify(stdRepo, never()).findByDepartment_Id(any());
    }
    @Test
    void searchByStudentName_whenExists_shouldReturnStudent(){
        when(stdRepo.findByNameContainingIgnoreCase("abhi")).thenReturn(List.of(student));
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(responseDTO);

        List<StudentResponseDTO> list = studentService.searchByStudentName("abhi");

        assertEquals(1, list.size());
        assertEquals(responseDTO.getName(), list.getFirst().getName());
    }
    @Test
    void searchByStudentName_whenNotExists(){
        when(stdRepo.findByNameContainingIgnoreCase("xyz")).thenReturn(List.of());

        List<StudentResponseDTO> list = studentService.searchByStudentName("xyz");
        assertTrue(list.isEmpty());
        verify(mapper, never()).map(any(), eq(StudentResponseDTO.class));
    }
    @Test
    void getAllStudentsPaginated_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Student> studentPage = new PageImpl<>(List.of(student), pageable, 1);

        when(stdRepo.findAll(pageable)).thenReturn(studentPage);
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(responseDTO);

        Page<StudentResponseDTO> result = studentService.getAllStudentsPaginated(0, 5);

        assertEquals(1, result.getTotalElements());
        assertEquals(responseDTO.getName(), result.getContent().getFirst().getName());
    }
    @Test
    void getSortedStudents_buildsAscendingSortAndReturnsMappedList() {
        when(stdRepo.findAll(any(Sort.class))).thenReturn(List.of(student));
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(responseDTO);

        List<StudentResponseDTO> result = studentService.getSortedStudents("name", "asc");

        assertEquals(1, result.size());

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(stdRepo).findAll(sortCaptor.capture());
        assertEquals(Sort.Direction.ASC, sortCaptor.getValue().getOrderFor("name").getDirection());
    }
    @Test
    void getSortedStudents_buildsDescendingSort() {
        when(stdRepo.findAll(any(Sort.class))).thenReturn(List.of(student));
        when(mapper.map(student, StudentResponseDTO.class)).thenReturn(responseDTO);

        studentService.getSortedStudents("name", "desc");

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(stdRepo).findAll(sortCaptor.capture());
        assertEquals(Sort.Direction.DESC, sortCaptor.getValue().getOrderFor("name").getDirection());
    }
}