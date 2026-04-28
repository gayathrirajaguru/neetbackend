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

        // 2️⃣ Generate token
        String token = UUID.randomUUID().toString();
        saved.setApprovalToken(token);
        studentRepo.save(saved);

        // 3️⃣ Create approval link
        String baseUrl = "https://neetbackend-jggh.onrender.com";
        String approvalLink = baseUrl + "/api/student/approve/"
                + saved.getId() + "?token=" + token;

        // 4️⃣ Send mail to user (SAFE)
        emailService.sendMail(
                student.getEmail(),
                "Registration Successful",
                "Hi " + student.getName() + ",\n\n" +
                "Your account is under review.\n\n" +
                "You can login after approval.\n\n" +
                "Regards,\nNEET Team"
        );

        // 5️⃣ Owner email (SAFE HTML)
        String htmlContent =
                "<div style='font-family: Arial; text-align: center;'>"
                + "<h2>New Student Registration</h2>"
                + "<p><b>Name:</b> " + student.getName() + "</p>"
                + "<p><b>Email:</b> " + student.getEmail() + "</p>"
                + "<br>"
                + "<a href='" + approvalLink + "' "
                + "style='background: green; color: white; padding: 12px 20px; "
                + "text-decoration: none; border-radius: 5px;'>"
                + "Approve User"
                + "</a>"
                + "</div>";

        emailService.sendHtmlMail(
                ownerEmail,
                "New Student Registration",
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

        // 🔐 Token validation
        if (student.getApprovalToken() == null ||
            !student.getApprovalToken().equals(token)) {
            return "<h3>Invalid or expired link ❌</h3>";
        }

        student.setApproved(true);
        student.setApprovalToken(null);
        studentRepo.save(student);

        // 📧 Notify user
        emailService.sendMail(
                student.getEmail(),
                "Account Approved ✅",
                "Hi " + student.getName() + ",\n\n" +
                "Your account is approved.\nYou can login now.\n\n" +
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