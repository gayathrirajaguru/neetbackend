package com.example.neet.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.neet.Entity.TestResult;

public interface TestResultRepo extends JpaRepository<TestResult, Integer> {
}