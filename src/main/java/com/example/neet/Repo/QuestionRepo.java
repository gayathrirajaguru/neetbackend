package com.example.neet.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.neet.Entity.Question;
import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Long> {

	@Query(value = "SELECT * FROM neet_100_questions ORDER BY RAND() LIMIT 50", nativeQuery = true)
	List<Question> getRandom50();

	@Query(value = "SELECT * FROM neet_100_questions WHERE id NOT IN (:ids) ORDER BY RAND() LIMIT 50", nativeQuery = true)
	List<Question> getRandom50Excluding(@Param("ids") List<Long> ids);
}