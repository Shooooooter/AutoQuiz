package com.quizapp.model;

import com.quizapp.utils.QuizType;
import com.quizapp.utils.QuizDifficulty;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int id;
    private QuizType type;
    private QuizDifficulty difficulty;
    private String topic;
    private List<Question> questions;
    private int correctAnswers;

    public Quiz(int id, QuizType type, QuizDifficulty difficulty, String topic, List<Question> questions) {
        this.id = id;
        this.type = type;
        this.difficulty = difficulty;
        this.topic = topic;
        this.questions = questions;
        this.correctAnswers = 0;
    }

    public Quiz(int quizId, List<Question> questions) {
        this.id = quizId;
        this.questions = questions;
        this.correctAnswers = 0;
    }

    public Quiz(List<Question> questions) {
        this.questions = questions;
        this.correctAnswers = 0;
    }

    public Quiz(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public QuizType getType() {
        return type;
    }

    public void setType(QuizType type) {
        this.type = type;
    }

    public QuizDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuizDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void addQuestion(Question question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        questions.add(question);
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public void incrementCorrectAnswers() {
        correctAnswers++;
    }

    public double getScore() {
        return (double) correctAnswers / questions.size() * 100;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", type=" + type +
                ", difficulty=" + difficulty +
                ", topic=" + topic +
                ", questions=" + questions +
                '}';
    }
}
