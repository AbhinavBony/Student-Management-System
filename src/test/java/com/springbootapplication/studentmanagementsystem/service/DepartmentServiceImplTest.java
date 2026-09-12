package com.springbootapplication.studentmanagementsystem.service;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentStudentCountDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.StudentDTO.StudentResponseDTO;
import com.springbootapplication.studentmanagementsystem.entity.Department;
import com.springbootapplication.studentmanagementsystem.exception.DepartmentNotFound;
import com.springbootapplication.studentmanagementsystem.exception.DuplicateDepartmentFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DepartmentServiceImplTest {
    @Autowired
    private DepartmentServiceImpl service;
    @Autowired
    private StudentServiceImpl studentService;
    DepartmentRequestDTO requestDTO;

    @BeforeEach
    void setUp(){
        requestDTO = new DepartmentRequestDTO();
        requestDTO.setDepartmentName("Computer Science");
    }
    @Test
    void addDepartment_shouldSaveAndReturn_successfully(){
        DepartmentResponseDTO saved = service.addDepartment(requestDTO);

        assertNotNull(saved.getId());
        assertEquals(requestDTO.getDepartmentName(), saved.getDepartmentName());
    }
    @Test
    void addDepartment_shouldThrowException_whenDuplicateDepartment(){
        service.addDepartment(requestDTO);

        DepartmentRequestDTO duplicate = new DepartmentRequestDTO();
        duplicate.setDepartmentName("Computer Science");

        assertThrows(DuplicateDepartmentFound.class, ()-> service.addDepartment(duplicate));
    }
    @Test
    void getAllDepartments(){
        DepartmentResponseDTO dep1 = service.addDepartment(requestDTO);
        DepartmentResponseDTO dep2 = service.addDepartment(new DepartmentRequestDTO("Information Technology"));
        List<DepartmentResponseDTO> list = service.getAllDepartments();

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(d-> d.getDepartmentName().equals("Computer Science")));
        assertTrue(list.stream().anyMatch(d-> d.getDepartmentName().equals("Information Technology")));
    }
    @Test
    void getDepartment_whenExists(){
        DepartmentResponseDTO saved = service.addDepartment(requestDTO);
        DepartmentResponseDTO found = service.getDepartmentById(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getDepartmentName(), found.getDepartmentName());
    }
    @Test
    void getDepartment_shouldThrowsException_whenNotExists(){
        assertThrows(DepartmentNotFound.class, ()-> service.getDepartmentById(9999));
    }
    @Test
    void updateDepartment_whenFound(){
        DepartmentResponseDTO saved = service.addDepartment(requestDTO);

        DepartmentRequestDTO updateDepartment = new DepartmentRequestDTO();
        updateDepartment.setDepartmentName("Computer Science Engineering");
        DepartmentResponseDTO updated = service.updateDepartment(saved.getId(), updateDepartment);

        assertEquals(saved.getId(), updated.getId());
        assertEquals(updateDepartment.getDepartmentName(), updated.getDepartmentName());
    }
    @Test
    void updateDepartment_shouldThrowException_whenDepartmentNotFound(){
        assertThrows(DepartmentNotFound.class, ()-> service.updateDepartment(9999, requestDTO));
    }
    @Test
    void deleteDepartment_whenExists(){
        DepartmentResponseDTO saved = service.addDepartment(requestDTO);
        service.deleteDepartment(saved.getId());
        assertThrows(DepartmentNotFound.class, ()-> service.getDepartmentById(saved.getId()));

    }
    @Test
    void deleteDepartment_shouldThrowException_whenNotExists(){
        assertThrows(DepartmentNotFound.class, ()-> service.deleteDepartment(9999));
    }
    @Test
    void getStudentCountByDepartmentId_whenStudentExistsWithDepartmentId(){
        DepartmentResponseDTO savedDepartment = service.addDepartment(requestDTO);
        studentService.addStudentByDepartment(new StudentRequestDTO(1514331002L,"Abhinav", "IMS", savedDepartment.getId()));
        studentService.addStudentByDepartment(new StudentRequestDTO(1514331001L,"Abhilasha", "IMS", savedDepartment.getId()));
        DepartmentStudentCountDTO count = service.getStudentCountByDepartmentId(savedDepartment.getId());

        assertEquals(2, count.getStudentCount());
    }
    @Test
    void getStudentCountByDepartmentId_shouldThrowException_whenDepartmentIdNotExists(){
        assertThrows(DepartmentNotFound.class, ()->  service.getStudentCountByDepartmentId(999));
    }
    @Test
    void getAllStudentCountByDepartment_whenStudentExistsByDepartment(){
        DepartmentResponseDTO department1= service.addDepartment(requestDTO);
        DepartmentResponseDTO department2 = service.addDepartment(new DepartmentRequestDTO("Information Technology"));

        studentService.addStudentByDepartment(new StudentRequestDTO(1514331002L,"Abhinav Srivastava", "IMS", department1.getId()));
        studentService.addStudentByDepartment(new StudentRequestDTO(1514331001L,"Abhilasha Trivedi", "IMS", department1.getId()));
        studentService.addStudentByDepartment(new StudentRequestDTO(1514331025L,"Manvendra", "IMS", department2.getId()));
        studentService.addStudentByDepartment(new StudentRequestDTO(15143310011L,"Asraa Ahmed", "IMS", department1.getId()));

        List<DepartmentStudentCountDTO> count = service.getAllStudentCountByDepartment();

        assertEquals(2, count.size());

        DepartmentStudentCountDTO csCount = count.stream().filter(d-> d.getDepartmentName().equals("Computer Science")).findFirst().orElseThrow();
        DepartmentStudentCountDTO itCount = count.stream().filter(d-> d.getDepartmentName().equals("Information Technology")).findFirst().orElseThrow();

        assertEquals(3, csCount.getStudentCount());
        assertEquals(1, itCount.getStudentCount());
    }
    @Test
    void searchDepartmentName_whenExists(){
        DepartmentResponseDTO saved = service.addDepartment(requestDTO);
        DepartmentResponseDTO existsByName = service.searchByDepartmentName(saved.getDepartmentName());

        assertEquals(saved.getDepartmentName(), existsByName.getDepartmentName());
    }
    @Test
    void searchDepartmentName_shouldThrowException_whenNotExists(){
        assertThrows(DepartmentNotFound.class, ()-> service.searchByDepartmentName("Information Technology"));
    }
    @Test
    void searchDepartmentName_PartialSearchName(){

        DepartmentResponseDTO saved = service.addDepartment(requestDTO);

        DepartmentResponseDTO partialFound = service.searchByDepartmentName("computer");

        assertEquals(saved.getDepartmentName(), partialFound.getDepartmentName());
    }
    @Test
    void getDepartmentsPaginated_shouldReturnCorrectPageSize_andTotalCounts() {
        for (int i = 1; i <= 5; i++) {
            service.addDepartment(new DepartmentRequestDTO("Department " + i));
        }

        Page<DepartmentResponseDTO> firstPage = service.getDepartmentsPaginated(0, 3);

        assertEquals(3, firstPage.getContent().size());
        assertEquals(5, firstPage.getTotalElements());
        assertEquals(2, firstPage.getTotalPages());
        assertTrue(firstPage.isFirst());
        assertFalse(firstPage.isLast());
    }

    @Test
    void getDepartmentsPaginated_shouldReturnRemainingItems_onLastPage() {
        for (int i = 1; i <= 5; i++) {
            service.addDepartment(new DepartmentRequestDTO("Department " + i));
        }

        Page<DepartmentResponseDTO> secondPage = service.getDepartmentsPaginated(1, 3);

        assertEquals(2, secondPage.getContent().size()); // 5 total, 3 on page 0, 2 remain
        assertTrue(secondPage.isLast());
    }

    @Test
    void getDepartmentsPaginated_shouldReturnEmptyPage_whenNoDepartmentsExist() {
        Page<DepartmentResponseDTO> page = service.getDepartmentsPaginated(0, 5);

        assertTrue(page.getContent().isEmpty());
        assertEquals(0, page.getTotalElements());
    }
    @Test
    void getAllDepartmentsSorted_shouldReturnInAscendingOrder_byDepartmentName(){
        service.addDepartment(new DepartmentRequestDTO("Electrical"));
        service.addDepartment(new DepartmentRequestDTO("Biotechnology"));
        service.addDepartment(new DepartmentRequestDTO("Computer Science"));
        service.addDepartment(new DepartmentRequestDTO("Information Technology"));

        List<DepartmentResponseDTO> ascendingList = service.getAllDepartmentsSorted("departmentName", "asc");

        assertEquals("Biotechnology", ascendingList.get(0).getDepartmentName());
        assertEquals("Computer Science", ascendingList.get(1).getDepartmentName());
        assertEquals("Electrical", ascendingList.get(2).getDepartmentName());
    }
    @Test
    void getAllDepartmentsSorted_shouldReturnInDescendingOrder_byDepartmentName(){
        service.addDepartment(new DepartmentRequestDTO("Electrical"));
        service.addDepartment(new DepartmentRequestDTO("Biotechnology"));
        service.addDepartment(new DepartmentRequestDTO("Computer Science"));
        service.addDepartment(new DepartmentRequestDTO("Information Technology"));

        List<DepartmentResponseDTO> sorted = service.getAllDepartmentsSorted("departmentName", "desc");

        assertEquals("Information Technology", sorted.get(0).getDepartmentName());
        assertEquals("Electrical", sorted.get(1).getDepartmentName());
        assertEquals("Computer Science", sorted.get(2).getDepartmentName());
    }
}