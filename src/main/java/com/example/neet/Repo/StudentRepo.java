package com.example.neet.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.neet.Entity.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {

    Student findByEmailAndPassword(String email, String password);

    Student findByEmail(String email);

}