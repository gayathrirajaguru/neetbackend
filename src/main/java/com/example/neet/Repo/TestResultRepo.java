package com.example.neet.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.neet.Entity.TestResult;

public interface TestResultRepo extends JpaRepository<TestResult, Integer> {
	
	   List<TestResult> findByStudentId(Integer studentId);
}