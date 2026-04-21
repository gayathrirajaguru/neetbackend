package com.example.neet.controller;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.neet.Entity.Question;
import com.example.neet.Entity.Student;
import com.example.neet.Entity.TestResult;
import com.example.neet.Repo.QuestionRepo;
import com.example.neet.Repo.StudentRepo;
import com.example.neet.Repo.TestResultRepo;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private QuestionRepo repo;

    @Autowired
    private TestResultRepo resultRepo;
    @Autowired
    private StudentRepo studentRepo;
    
    
    private List<Question> getBalancedQuestions(List<Long> usedIds) {

        List<Question> questions = new ArrayList<>();

        // Physics - 45
        questions.addAll(repo.getBySubjectLimitExcluding("Physics", 45, usedIds));

        // Chemistry - 45
        questions.addAll(repo.getBySubjectLimitExcluding("Chemistry", 45, usedIds));

        // Biology - 90
        questions.addAll(repo.getBySubjectLimitExcluding("Biology", 90, usedIds));

        // Shuffle
        java.util.Collections.shuffle(questions);

        return questions;
    }
    
    
    @GetMapping("/questions")
    public List<Question> getQuestionsGet(@RequestParam(required = false) Integer studentId) {

        List<Long> usedIds = new ArrayList<>();

        if (studentId != null) {
            usedIds = getUsedQuestionIds(studentId);
        }

        if (usedIds.size() >= repo.count()) {
            usedIds.clear();
        }

        return getBalancedQuestions(usedIds);
    }
    @PostMapping("/questions")
    public List<Question> getQuestionsPost(@RequestBody(required = false) List<Long> usedIds) {

        if (usedIds == null) {
            usedIds = new ArrayList<>();
        }

        return getBalancedQuestions(usedIds);
    }
    private List<Long> getUsedQuestionIds(Integer studentId) {

        List<TestResult> results = resultRepo.findAll();
        List<Long> usedIds = new ArrayList<>();

        for (TestResult r : results) {

            if (r.getStudent() != null 
                && r.getStudent().getId() == studentId 
                && r.getQuestionIds() != null) {

                String[] ids = r.getQuestionIds().split(",");

                for (String id : ids) {
                    if (!id.isEmpty()) {
                        usedIds.add(Long.valueOf(id));
                    }
                }
            }
        }

        return usedIds;
    }
    // Submit test
    @PostMapping("/submit")
    public Map<String, Object> submit(@RequestBody Map<String, Object> data) {

        Map<String, String> answers = (Map<String, String>) data.get("answers");
        List<Integer> questionIds = (List<Integer>) data.get("questionIds");

        if (questionIds == null || questionIds.isEmpty()) {
            throw new RuntimeException("Question IDs missing from request");
        }

        List<Long> ids = questionIds.stream()
                .map(Long::valueOf)
                .toList();

        List<Question> questions = repo.findAllById(ids);

        Number studentIdNum = (Number) data.get("studentId");
        Integer studentId = studentIdNum != null ? studentIdNum.intValue() : null;

        Student student = studentRepo.findById(studentId).orElse(null);

        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        int correct = 0;
        int wrong = 0;
        int unanswered = 0;

        List<Map<String, Object>> analysis = new ArrayList<>();

        for (Question q : questions) {

            String qId = String.valueOf(q.getId());
            String userAnswer = answers != null ? answers.get(qId) : null;

            Map<String, Object> row = new HashMap<>();

            row.put("questionNo", q.getId());
            row.put("question", q.getQuestion());
            row.put("subject", q.getSubject());
            row.put("correctAnswer", q.getCorrectAnswer());

            if (userAnswer == null || userAnswer.trim().isEmpty()) {
                unanswered++;
                row.put("userAnswer", "Not Attempted");
                row.put("result", "⚠");
            } 
            else if (userAnswer.equals(q.getCorrectAnswer())) {
                correct++;
                row.put("userAnswer", userAnswer);
                row.put("result", "✔");
            } 
            else {
                wrong++;
                row.put("userAnswer", userAnswer);
                row.put("result", "✖");
            }

            analysis.add(row);
        }

        int score = correct;

        TestResult result = new TestResult();
        result.setStudent(student);
        result.setScore(score);
        result.setCorrectAnswers(correct);
        result.setWrongAnswers(wrong);
        result.setTotalQuestions(questions.size());

        // ✅ 180 minutes = 10800 seconds
        result.setTimeTaken(10800);

        // ✅ STORE QUESTION IDS (IMPORTANT FOR NO-REPEAT LOGIC)
        String questionIdsStr = questionIds.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        result.setQuestionIds(questionIdsStr);

        resultRepo.save(result);

        Map<String, Object> response = new HashMap<>();
        response.put("score", score);
        response.put("correct", correct);
        response.put("wrong", wrong);
        response.put("unanswered", unanswered);
        response.put("analysis", analysis);

        return response;
    }
    // Get test history
    @GetMapping("/history")
    public List<TestResult> getHistory() {
        return resultRepo.findAll();
    }
    
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<TestResult> results = resultRepo.findAll();

        int totalTests = results.size();
        int bestScore = results.stream().mapToInt(TestResult::getScore).max().orElse(0);
        double avgScore = results.stream().mapToInt(TestResult::getScore).average().orElse(0);

        Map<String, Object> map = new HashMap<>();
        map.put("totalTests", totalTests);
        map.put("bestScore", bestScore);
        map.put("avgScore", avgScore);

        return map;
    }
}