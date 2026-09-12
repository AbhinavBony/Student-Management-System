package com.springbootapplication.studentmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "College_Students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    @Column(name = "student_rollNumber", unique = true, nullable = false)
    private Long rollNumber;
    @Column(name = "student_name", nullable = false)
    private String name;
    @Column(name = "college_name", nullable = false)
    private String collegeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
