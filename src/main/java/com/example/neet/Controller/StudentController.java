package com.example.neet.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.neet.Entity.Student;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:5173")

public class StudentController {

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Student student) {

        // ✅ Hardcoded credentials
        String validEmail = "admin@gmail.com";
        String validPassword = "123456";

        if (student.getEmail().equals(validEmail) &&
            student.getPassword().equals(validPassword)) {

            return ResponseEntity.ok("Login Success");
        } else {
            return ResponseEntity.status(401).body("Invalid Email or Password");
        }
    }

    // ================= REGISTER (OPTIONAL) =================
    @PostMapping("/register")
    public String register(@RequestBody Student student) {
        return "Registration disabled for now";
    }
}