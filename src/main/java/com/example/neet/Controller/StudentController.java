package com.example.neet.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.neet.Entity.Student;
import com.example.neet.Repo.StudentRepo;
import com.example.neet.Service.EmailService;
import com.example.neet.dto.LoginRequest;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private EmailService emailService;

    // ✅ Owner email from properties
    @Value("${app.owner.email}")
    private String ownerEmail;

    // ================= REGISTER =================
    @PostMapping("/register")
    public Student register(@RequestBody Student student) {

        student.setApproved(false);

        // 1️⃣ Save student
        Student saved = studentRepo.save(student);

        // 2️⃣ Generate approval token (SECURITY)
        String token = UUID.randomUUID().toString();
        saved.setApprovalToken(token);
        studentRepo.save(saved);

        // 3️⃣ Create approval link
        String baseUrl = "https://neetbackend-jggh.onrender.com";
        String approvalLink = baseUrl + "/api/student/approve/" 
                + saved.getId() + "?token=" + token;

        // 4️⃣ Send email to user
        emailService.sendMail(
                student.getEmail(),
                "Registration Successful",
                "Hi " + student.getName() + ",\n\n" +
                "Your account has been created successfully.\n" +
                "Your account is under review.\n\n" +
                "You can login after approval.\n\n" +
                "Regards,\nNEET Team"
        );

        // 5️⃣ Send email to owner (HTML button)
        String htmlContent =
                "<div style='font-family: Arial; text-align: center;'>"
                + "<h2>New Student Registration</h2>"
                + "<p><b>Name:</b> " + student.getName() + "</p>"
                + "<p><b>Email:</b> " + student.getEmail() + "</p>"
                + "<br>"
                + "<a href='" + approvalLink + "' "
                + "style='background: linear-gradient(45deg, #28a745, #218838);"
                + "color: white; padding: 14px 24px; font-size: 16px;"
                + "text-decoration: none; border-radius: 8px;'>"
                + "✅ Approve User"
                + "</a>"
                + "</div>";

        emailService.sendHtmlMail(
                ownerEmail,
                "New Student Registration - Approval Required",
                htmlContent
        );

        return saved;
    }

    // ================= APPROVE =================
    @GetMapping("/approve/{id}")
    public String approveStudent(
            @PathVariable Integer id,
            @RequestParam String token) {

        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 Validate token
        if (student.getApprovalToken() == null ||
            !student.getApprovalToken().equals(token)) {
            return "<h3>Invalid or expired approval link ❌</h3>";
        }

        student.setApproved(true);
        student.setApprovalToken(null);
        studentRepo.save(student);

        // 📧 Notify user
        emailService.sendMail(
                student.getEmail(),
                "Account Approved ✅",
                "Hi " + student.getName() + ",\n\n" +
                "Your account has been approved.\n" +
                "You can now login.\n\n" +
                "Regards,\nNEET Team"
        );

        return "<h2>User Approved Successfully ✅</h2>";
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<Student> optionalStudent = studentRepo.findByEmail(request.getEmail());

        if (optionalStudent.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        Student student = optionalStudent.get();

        if (!student.isApproved()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Your account is not approved yet");
        }

        return ResponseEntity.ok("Login successful");
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
    public Student getProfile(@PathVariable Integer id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}