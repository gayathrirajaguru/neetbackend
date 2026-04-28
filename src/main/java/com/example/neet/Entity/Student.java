package com.example.neet.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private String password;
    private String phone;

    private boolean approved;
    private String institute;  

    @Column(name = "target_exam")
    private String targetExam;

    @Column(name = "preparation_level")
    private String preparationLevel;

    @Column(name = "target_year")
    private String targetYear;

    @Column(name = "preferred_subjects")
    private String preferredSubjects;// "Physics,Math,Biology"

    // ===== GETTERS & SETTERS =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getTargetExam() {
        return targetExam;
    }

    public void setTargetExam(String targetExam) {
        this.targetExam = targetExam;
    }

    public String getPreparationLevel() {
        return preparationLevel;
    }

    public void setPreparationLevel(String preparationLevel) {
        this.preparationLevel = preparationLevel;
    }

    public String getTargetYear() {
        return targetYear;
    }

    public void setTargetYear(String targetYear) {
        this.targetYear = targetYear;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getPreferredSubjects() {
        return preferredSubjects;
    }

    public void setPreferredSubjects(String preferredSubjects) {
        this.preferredSubjects = preferredSubjects;
    }
}