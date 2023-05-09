package com.quizapp.model;

import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import java.util.List;

public class Quiz {
    private int id;
    private QuizType type;
    private QuizDifficulty difficulty;
    private Topic topic;
    private List<Question> questions;

    public Quiz(int id, QuizType type, QuizDifficulty difficulty, Topic topic, List<Question> questions) {
        this.id = id;
        this.type = type;
        this.difficulty = difficulty;
        this.topic = topic;
        this.questions = questions;
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
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
