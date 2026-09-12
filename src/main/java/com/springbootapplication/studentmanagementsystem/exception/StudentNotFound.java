package com.springbootapplication.studentmanagementsystem.exception;

public class StudentNotFound extends RuntimeException {
    public StudentNotFound(String message) {
        super(message);
    }
}
