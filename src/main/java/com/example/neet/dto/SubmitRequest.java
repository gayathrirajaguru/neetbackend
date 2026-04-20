package com.example.neet.dto;

import java.util.List;
import java.util.Map;

public class SubmitRequest {

    private Map<String, String> answers; // 🔥 IMPORTANT: String keys
    private int studentId;
    private List<Integer> questionIds;

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public List<Integer> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Integer> questionIds) {
        this.questionIds = questionIds;
    }
}
