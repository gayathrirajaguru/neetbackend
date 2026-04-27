package com.example.neet.Repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.neet.Entity.Question;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Long> {

    // ===== RANDOM FULL TEST =====
    @Query(value = "SELECT * FROM neet_100_questions ORDER BY RAND() LIMIT 180", nativeQuery = true)
    List<Question> getRandom180();

    @Query(value = "SELECT * FROM neet_100_questions WHERE id >= (SELECT FLOOR(RAND() * (SELECT MAX(id) FROM neet_100_questions))) LIMIT 180", nativeQuery = true)
    List<Question> getFastRandom180();

    // ===== EXCLUDING USED =====
    @Query(value = "SELECT * FROM neet_100_questions WHERE (:ids IS NULL OR id NOT IN (:ids)) ORDER BY RAND() LIMIT 180", nativeQuery = true)
    List<Question> getRandom180Excluding(@Param("ids") List<Long> ids);

    // ===== SUBJECT FILTER (IMPORTANT) =====
    @Query(
        value = "SELECT * FROM neet_100_questions WHERE subject = :subject AND (:ids IS NULL OR id NOT IN (:ids)) ORDER BY RAND()",
        countQuery = "SELECT count(*) FROM neet_100_questions WHERE subject = :subject",
        nativeQuery = true
    )
    List<Question> getBySubjectExcluding(
            @Param("subject") String subject,
            @Param("ids") List<Long> ids,
            Pageable pageable
    );
}