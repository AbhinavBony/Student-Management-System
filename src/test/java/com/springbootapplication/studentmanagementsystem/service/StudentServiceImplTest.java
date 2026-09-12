package com.springbootapplication.studentmanagementsystem.service;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.DepartmentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentUpdateRequestDTO;
import com.springbootapplication.studentmanagementsystem.exception.DepartmentNotFound;
import com.springbootapplication.studentmanagementsystem.exception.DuplicateStudentFound;
import com.springbootapplication.studentmanagementsystem.exception.StudentNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class StudentServiceImplTest {
    @Autowired
    private StudentServiceImpl stdService;
    @Autowired
    private DepartmentServiceImpl depService;
    private StudentRequestDTO requestDTO;
    private DepartmentResponseDTO savedDepartment;

    @BeforeEach
    void setUp() {
        savedDepartment = depService.addDepartment(new DepartmentRequestDTO("Computer Science"));
        requestDTO = new StudentRequestDTO();
        requestDTO.setRollNumber(151431002L);
        requestDTO.setName("Abhinav Srivastava");
        requestDTO.setCollegeName("IMS");
        requestDTO.setDepartmentId(savedDepartment.getId());
    }
    @Test
    void addStudent_shouldSaveAndReturnDetails(){
        StudentResponseDTO saved = stdService.addStudentByDepartment(requestDTO);

        assertNotNull(saved.getId());
        assertEquals(requestDTO.getName(), saved.getName());
    }
    @Test
    void addStudent_shouldThrowException_whenDuplicateStudentExists(){
        stdService.addStudentByDepartment(requestDTO);
        StudentRequestDTO duplicate = new StudentRequestDTO();
        duplicate.setRollNumber(151431002L);
        duplicate.setName("Abhilasha Trivedi");
        duplicate.setCollegeName("IMS");
        duplicate.setDepartmentId(savedDepartment.getId());

        assertThrows(DuplicateStudentFound.class, ()-> stdService.addStudentByDepartment(duplicate));
    }
    @Test
    void addStudent_shouldThrowException_whenDepartmentNotFound(){
        requestDTO.setDepartmentId(999);
        assertThrows(DepartmentNotFound.class, ()-> stdService.addStudentByDepartment(requestDTO));
    }
    @Test
    void getAllStudents_shouldReturnListOfStudents(){
        stdService.addStudentByDepartment(requestDTO);
        StudentRequestDTO student2 = new StudentRequestDTO();
        student2.setRollNumber(151431001L);
        student2.setName("Abhilasha Trivedi");
        student2.setCollegeName("IMS");
        student2.setDepartmentId(savedDepartment.getId());
        stdService.addStudentByDepartment(student2);

        List<StudentResponseDTO> list = stdService.getAllStudents();

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(std-> std.getName().equals("Abhinav Srivastava")));
        assertTrue(list.stream().anyMatch(std-> std.getName().equals("Abhilasha Trivedi")));
    }

    @Test
    void getAllStudents_shouldThrowEmptyList_whenNoStudentsExists(){

        List<StudentResponseDTO> list = stdService.getAllStudents();
        assertTrue(list.isEmpty());
    }

    @Test
    void getStudentByRollNumber_shouldReturnStudent_whenRollNumberExists(){
        StudentResponseDTO saved = stdService.addStudentByDepartment(requestDTO);
        StudentResponseDTO foundByRollNNumber = stdService.getStudentByRollNumber(saved.getRollNumber());

        assertEquals(saved.getRollNumber(), foundByRollNNumber.getRollNumber());
        assertEquals(saved.getName(), foundByRollNNumber.getName());
    }
    @Test
    void getStudentByRollNumber_shouldThrowException_whenRollNumberNotFound(){
        assertThrows(StudentNotFound.class, ()-> stdService.getStudentByRollNumber(1514310023L));
    }
    @Test
    void updateStudentDetails_shouldUpdateAndReturn_withoutDepartmentId(){
        StudentResponseDTO saved = stdService.addStudentByDepartment(requestDTO);
        StudentUpdateRequestDTO updateDetails = new StudentUpdateRequestDTO();
        updateDetails.setName("Abhinav");
        updateDetails.setCollegeName("IMS Engineering College");

        StudentResponseDTO updated = stdService.updateStudentDetails(saved.getRollNumber(), updateDetails);

        assertEquals(requestDTO.getRollNumber(), updated.getRollNumber());
        assertEquals(updateDetails.getName(), updated.getName());
    }
    @Test
    void updateStudentDetails_shouldThrowException_whenStudentNotFound(){
        StudentUpdateRequestDTO updateDetails = new StudentUpdateRequestDTO();
        updateDetails.setName("Abhinav");
        updateDetails.setCollegeName("IMS Engineering College");
        assertThrows(StudentNotFound.class, ()-> stdService.updateStudentDetails(1514310025L, updateDetails));
    }
    @Test
    void deleteStudent_shouldRemoveStudent(){
        StudentResponseDTO saved = stdService.addStudentByDepartment(requestDTO);
        stdService.deleteStudentDetails(saved.getRollNumber());

        assertThrows(StudentNotFound.class, ()-> stdService.getStudentByRollNumber(saved.getRollNumber()));

    }
    @Test
    void deleteStudent_shouldThrowException_whenStudentNotFound(){
        assertThrows(StudentNotFound.class, ()-> stdService.deleteStudentDetails(1514310029L));
    }
    @Test
    void updateStudentByDepartmentId_shouldUpdateStudentDepartment_whenRollNumberFound(){
        StudentResponseDTO saved = stdService.addStudentByDepartment(requestDTO);
        savedDepartment = depService.addDepartment(new DepartmentRequestDTO("Information Technology"));
        DepartmentUpdateRequestDTO updateDepartment = new DepartmentUpdateRequestDTO();
        updateDepartment.setDepartmentId(savedDepartment.getId());

        StudentResponseDTO updated = stdService.updateStudentByDepartment(saved.getRollNumber(), updateDepartment);

        assertEquals(saved.getId(), updated.getId());
        assertEquals(savedDepartment.getId(), updated.getDepartment().getId());
    }
    @Test
    void updateStudentByDepartmentId_shouldThrowException_whenStudentRollNumberNotFound(){
        DepartmentUpdateRequestDTO updateRequestDTO = new DepartmentUpdateRequestDTO();
        updateRequestDTO.setDepartmentId(savedDepartment.getId());
        assertThrows(StudentNotFound.class, ()-> stdService.updateStudentByDepartment(151431025L, updateRequestDTO));
    }
    @Test
    void updateStudentByDepartmentId_shouldThrowException_whenDepartmentIdNotFound(){
        StudentResponseDTO saved = stdService.addStudentByDepartment(requestDTO);
        DepartmentUpdateRequestDTO updateRequestDTO = new DepartmentUpdateRequestDTO();
        updateRequestDTO.setDepartmentId(9999);
        assertThrows(DepartmentNotFound.class, ()-> stdService.updateStudentByDepartment(saved.getRollNumber(), updateRequestDTO));
    }
    @Test
    void getStudentsByDepartment_shouldReturnStudent_byDepartmentId(){
        StudentResponseDTO std1= stdService.addStudentByDepartment(requestDTO);
        StudentRequestDTO std2 = new StudentRequestDTO();
        std2.setRollNumber(151431003L);
        std2.setName("Abhishek Sharma");
        std2.setCollegeName("IMS");
        std2.setDepartmentId(savedDepartment.getId());
        StudentResponseDTO savedStd2 = stdService.addStudentByDepartment(std2);

        List<StudentResponseDTO> list = stdService.getStudentsByDepartment(savedDepartment.getId());

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(std-> std.getName().equals("Abhinav Srivastava")));
        assertTrue(list.stream().anyMatch(std-> std.getName().equals("Abhishek Sharma")));
    }
    @Test
    void getStudentsByDepartment_shouldThrowException_whenDepartmentIdNotExists(){
        assertThrows(DepartmentNotFound.class, ()-> stdService.getStudentsByDepartment(9999));
    }
    @Test
    void searchStudentName_shouldReturnStudent_whenNameExists(){
        StudentResponseDTO saved = stdService.addStudentByDepartment(requestDTO);

        List<StudentResponseDTO> list = stdService.searchByStudentName("Abhi");

        assertEquals(saved.getName(), list.get(0).getName());
        assertEquals(1, list.size());
    }
    @Test
    void searchStudentName_shouldThrowException_whenNoNameFound(){
        StudentResponseDTO saved = stdService.addStudentByDepartment(requestDTO);

        List<StudentResponseDTO> list = stdService.searchByStudentName("Dinesh");

        assertTrue(list.isEmpty());
    }
    @Test
    void getSortedStudentDetails_ascendingOrder(){
        StudentResponseDTO std1 = stdService.addStudentByDepartment(requestDTO);
        StudentResponseDTO std2 = stdService.addStudentByDepartment(new StudentRequestDTO(151431001L, "Abhilasha Trivedi", "IMS", savedDepartment.getId()));
        StudentResponseDTO std3 = stdService.addStudentByDepartment(new StudentRequestDTO(151431025L, "Charul Singh", "IMS", savedDepartment.getId()));

        List<StudentResponseDTO> ascendingList = stdService.getSortedStudents("name", "asc");

        assertEquals(std2.getName(), ascendingList.get(0).getName());
        assertEquals(std1.getName(), ascendingList.get(1).getName());
        assertEquals(std3.getName(), ascendingList.get(2).getName());
    }
    @Test
    void getSortedStudentDetails_descendingOrder(){
        StudentResponseDTO std1 = stdService.addStudentByDepartment(requestDTO);
        StudentResponseDTO std2 = stdService.addStudentByDepartment(new StudentRequestDTO(151431001L, "Abhilasha Trivedi", "IMS", savedDepartment.getId()));
        StudentResponseDTO std3 = stdService.addStudentByDepartment(new StudentRequestDTO(151431025L, "Charul Singh", "IMS", savedDepartment.getId()));

        List<StudentResponseDTO> descendingList = stdService.getSortedStudents("name", "desc");

        assertEquals(std3.getName(), descendingList.get(0).getName());
        assertEquals(std1.getName(), descendingList.get(1).getName());
        assertEquals(std2.getName(), descendingList.get(2).getName());
    }

}