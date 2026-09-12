package com.springbootapplication.studentmanagementsystem.utils;

import com.springbootapplication.studentmanagementsystem.exception.DepartmentNotFound;
import com.springbootapplication.studentmanagementsystem.exception.DuplicateDepartmentFound;
import com.springbootapplication.studentmanagementsystem.exception.DuplicateStudentFound;
import com.springbootapplication.studentmanagementsystem.exception.StudentNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandling {
    @ExceptionHandler(DuplicateStudentFound.class)
    public ResponseEntity<ErrorResponse> handleDuplicateStudent(DuplicateStudentFound e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(StudentNotFound.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFound(StudentNotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(DuplicateDepartmentFound.class)
    public ResponseEntity<ErrorResponse> handleDuplicateDepartment(DuplicateDepartmentFound e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONTINUE.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(DepartmentNotFound.class)
    public ResponseEntity<ErrorResponse> handleDepartmentNotFound(DepartmentNotFound e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgsNotValid(MethodArgumentNotValidException ex){
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }));
        response.put("errorCode", HttpStatus.BAD_REQUEST.value());
        response.put("message", errors);
        response.put("timeStamp", LocalDate.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),exception.getMessage(), LocalDateTime.now()));
    }
}