package com.example.neet.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "neet_100_questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String question;

    @Column(name = "optiona", columnDefinition = "TEXT")
    private String optionA;

    @Column(name = "optionb", columnDefinition = "TEXT")
    private String optionB;

    @Column(name = "optionc", columnDefinition = "TEXT")
    private String optionC;

    @Column(name = "optiond", columnDefinition = "TEXT")
    private String optionD;

    @Column(name = "correct_answer_text")
    private String correctAnswer;

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }

    public String getSubject() { return subject; }

    public String getQuestion() { return question; }

    public String getOptionA() { return optionA; }

    public String getOptionB() { return optionB; }

    public String getOptionC() { return optionC; }

    public String getOptionD() { return optionD; }

    public String getCorrectAnswer() { return correctAnswer; }

    public void setId(Long id) { this.id = id; }

    public void setSubject(String subject) { this.subject = subject; }

    public void setQuestion(String question) { this.question = question; }

    public void setOptionA(String optionA) { this.optionA = optionA; }

    public void setOptionB(String optionB) { this.optionB = optionB; }

    public void setOptionC(String optionC) { this.optionC = optionC; }

    public void setOptionD(String optionD) { this.optionD = optionD; }

    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}