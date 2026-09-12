package com.springbootapplication.studentmanagementsystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int errorCode;
    private String message;
    private LocalDateTime timeStamp;
}
