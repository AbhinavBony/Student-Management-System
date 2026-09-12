package com.springbootapplication.studentmanagementsystem.exception;

public class DepartmentNotFound extends RuntimeException {
    public DepartmentNotFound(String message) {
        super(message);
    }
}
