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

    // ================= REGISTER =================
    @PostMapping("/register")
    public Student register(@RequestBody Student student) {

        // Prevent duplicate email
        if (studentRepo.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        student.setApproved(true);
        return studentRepo.save(student);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public Student login(@RequestBody Student student) {

        return studentRepo
                .findByEmailAndPassword(student.getEmail(), student.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid Email or Password"));
    }

    // ================= UPDATE PROFILE =================
    @PutMapping("/update")
    public Student update(@RequestBody Student student) {

        Student existing = studentRepo.findById(student.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setName(student.getName());
        existing.setPhone(student.getPhone());
        existing.setTargetExam(student.getTargetExam());
        existing.setPreparationLevel(student.getPreparationLevel());
        existing.setTargetYear(student.getTargetYear());
        existing.setInstitute(student.getInstitute());
        existing.setPreferredSubjects(student.getPreferredSubjects());

        return studentRepo.save(existing);
    }

    // ================= GET PROFILE =================
    @GetMapping("/{id}")
    public Student getProfile(@PathVariable int id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}