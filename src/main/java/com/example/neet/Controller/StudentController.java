package com.example.neet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.neet.Entity.Student;
import com.example.neet.Repo.StudentRepo;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepo studentRepo;

    // ✅ REGISTER
    @PostMapping("/register")
    public Student register(@RequestBody Student student) {

        student.setApproved(true); // optional

        return studentRepo.save(student);
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public Student login(@RequestBody Student student) {

        return studentRepo
                .findByEmailAndPassword(student.getEmail(), student.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid Email or Password"));
    }
}