package com.springbootapplication.studentmanagementsystem.exception;

public class DuplicateDepartmentFound extends RuntimeException {
    public DuplicateDepartmentFound(String message) {
        super(message);
    }
}
