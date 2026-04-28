package com.example.neet.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.neet.Entity.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {

    Optional<Student> findByEmailAndPassword(String email, String password);

    Optional<Student> findByEmail(String email);
}