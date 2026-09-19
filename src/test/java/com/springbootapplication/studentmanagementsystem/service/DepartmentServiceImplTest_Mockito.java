package com.springbootapplication.studentmanagementsystem.service;

import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentRequestDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentResponseDTO;
import com.springbootapplication.studentmanagementsystem.DTO.DepartmentDTO.DepartmentStudentCountDTO;
import com.springbootapplication.studentmanagementsystem.entity.Department;
import com.springbootapplication.studentmanagementsystem.exception.DepartmentNotFound;
import com.springbootapplication.studentmanagementsystem.exception.DuplicateDepartmentFound;
import com.springbootapplication.studentmanagementsystem.interfaces.DepartmentServices;
import com.springbootapplication.studentmanagementsystem.repository.DepartmentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest_Mockito {

    @Mock
    private DepartmentRepo repo;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    DepartmentServiceImpl departmentService;

    Department department;
    DepartmentRequestDTO requestDTO;
    DepartmentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1);
        department.setDepartmentName("Computer Science");

        requestDTO = new DepartmentRequestDTO();
        requestDTO.setDepartmentName("Computer Science");

        responseDTO = new DepartmentResponseDTO();
        responseDTO.setId(1);
        responseDTO.setDepartmentName("Computer Science");
    }
    @Test
    void getAllDepartments(){
        when(repo.findAll()).thenReturn(List.of(department));
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        List<DepartmentResponseDTO> list = departmentService.getAllDepartments();

        assertEquals(1, list.size());
        assertEquals("Computer Science", list.getFirst().getDepartmentName());
        verify(repo, times(1)).findAll();
    }
    @Test
    void getAllDepartments_shouldReturnEmpty(){
        List<DepartmentResponseDTO> list = departmentService.getAllDepartments();

        assertTrue(list.isEmpty());
    }
    @Test
    void addDepartment_shouldSaveAndReturnDepartment(){
        when(repo.findByDepartmentNameContaining(requestDTO.getDepartmentName())).thenReturn(Optional.empty());
        when(mapper.map(requestDTO, Department.class)).thenReturn(department);
        when(repo.save(department)).thenReturn(department);
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO saved = departmentService.addDepartment(requestDTO);

        assertNotNull(saved);
        assertEquals(responseDTO.getDepartmentName(), saved.getDepartmentName());
        assertEquals(responseDTO.getId(), saved.getId());
        verify(repo, times(1)).save(department);
    }
    @Test
    void addDepartment_shouldThrowException_whenDuplicateDepartment(){
        when(repo.findByDepartmentNameContaining(requestDTO.getDepartmentName())).thenReturn(Optional.of(department));

        assertThrows(DuplicateDepartmentFound.class, ()-> departmentService.addDepartment(requestDTO));
        verify(repo, never()).save(any());
    }
    @Test
    void updateDepartment_shouldUpdateAndReturnDepartment(){
        when(repo.findById(department.getId())).thenReturn(Optional.of(department));
        doNothing().when(mapper).map(requestDTO, department);
        when(repo.save(department)).thenReturn(department);
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO updated = departmentService.updateDepartment(department.getId(), requestDTO);

        assertNotNull(updated);
        verify(mapper, times(1)).map(requestDTO, department);
        verify(repo, times(1)).save(department);
    }
    @Test
    void updateDepartment_shouldThrowException_whenDepartmentIdNotFound(){
        when(repo.findById(9999)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFound.class, ()-> departmentService.updateDepartment(9999, requestDTO));

        verify(repo, never()).save(any());
    }
    @Test
    void deleteDepartment_whenExists(){
        when(repo.existsById(department.getId())).thenReturn(true);
        departmentService.deleteDepartment(department.getId());

        verify(repo, times(1)).deleteById(department.getId());
    }
    @Test
    void deleteDepartment_shouldThrowException_whenNotExists(){
        when(repo.existsById(999)).thenReturn(false);

        assertThrows(DepartmentNotFound.class, ()-> departmentService.deleteDepartment(999));
        verify(repo, never()).deleteById(anyInt());
    }
    @Test
    void getDepartmentById_whenExists(){
        when(repo.findById(department.getId())).thenReturn(Optional.of(department));
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO exists = departmentService.getDepartmentById(department.getId());

        assertNotNull(exists);
        assertEquals(responseDTO.getDepartmentName(), exists.getDepartmentName());
        verify(repo, times(1)).findById(department.getId());
    }
    @Test
    void getDepartmentById_whenNotExists(){
        when(repo.findById(999)).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFound.class, ()-> departmentService.getDepartmentById(999));

        verify(repo, times(1)).findById(999);

    }
    @Test
    void getAllStudentCountByDepartment_returnList(){
        DepartmentStudentCountDTO countDTO = new DepartmentStudentCountDTO();
        when(repo.getStudentCountPerDepartment()).thenReturn(List.of(countDTO));

        List<DepartmentStudentCountDTO> countList = departmentService.getAllStudentCountByDepartment();

        assertEquals(1, countList.size());
        verify(repo, times(1)).getStudentCountPerDepartment();
    }
    @Test
    void getStudentCountByDepartmentId_whenExists(){
        DepartmentStudentCountDTO countDto = new DepartmentStudentCountDTO();
        when(repo.getStudentCountByDepartmentId(department.getId())).thenReturn(countDto);

        DepartmentStudentCountDTO count = departmentService.getStudentCountByDepartmentId(department.getId());
        assertSame(countDto, count);
        verify(repo, times(1)).getStudentCountByDepartmentId(department.getId());
    }
    @Test
    void getStudentCountByDepartmentId_whenNotExists(){
        when(repo.getStudentCountByDepartmentId(999)).thenReturn(null);

        assertThrows(DepartmentNotFound.class, ()-> departmentService.getStudentCountByDepartmentId(999));
        verify(repo, times(1)).getStudentCountByDepartmentId(999);

    }
    @Test
    void searchDepartmentByName_whenExists(){
        when(repo.findByDepartmentNameContainingIgnoreCase("comp")).thenReturn(Optional.of(department));
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        DepartmentResponseDTO foundByName = departmentService.searchByDepartmentName("comp");

        assertEquals(department.getDepartmentName(), foundByName.getDepartmentName());
        verify(repo, times(1)).findByDepartmentNameContainingIgnoreCase("comp");

    }
    @Test
    void searchDepartmentByName_wheNotExists(){
        when(repo.findByDepartmentNameContainingIgnoreCase("Information")).thenReturn(Optional.empty());
        assertThrows(DepartmentNotFound.class, ()-> departmentService.searchByDepartmentName("Information"));

        verify(repo, times(1)).findByDepartmentNameContainingIgnoreCase("Information");
    }
    @Test
    void getAllDepartmentSorted_ascendingOrder(){
        when(repo.findAll(any(Sort.class))).thenReturn(List.of(department));
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        List<DepartmentResponseDTO> sortedList = departmentService.getAllDepartmentsSorted("departmentName", "asc");

        assertEquals(1, sortedList.size());
    }
    @Test
    void getAllDepartmentsSorted_descendingOrder(){
        when(repo.findAll(any(Sort.class))).thenReturn(List.of(department));
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        List<DepartmentResponseDTO> sortedList = departmentService.getAllDepartmentsSorted("departmentName", "desc");

        assertEquals(1, sortedList.size());
    }
    @Test
    void getDepartmentsPaginated_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Department> departmentPage = new PageImpl<>(List.of(department), pageable, 1);

        when(repo.findAll(pageable)).thenReturn(departmentPage);
        when(mapper.map(department, DepartmentResponseDTO.class)).thenReturn(responseDTO);

        Page<DepartmentResponseDTO> result = departmentService.getDepartmentsPaginated(0, 5);

        assertEquals(1, result.getTotalElements());
        assertEquals("Computer Science", result.getContent().get(0).getDepartmentName());
    }
}