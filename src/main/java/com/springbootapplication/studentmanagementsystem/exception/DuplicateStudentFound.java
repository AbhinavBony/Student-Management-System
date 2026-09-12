package com.springbootapplication.studentmanagementsystem.exception;

public class DuplicateStudentFound extends RuntimeException {
    public DuplicateStudentFound(String message) {
        super(message);
    }
}
