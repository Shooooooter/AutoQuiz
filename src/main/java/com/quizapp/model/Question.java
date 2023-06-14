package com.quizapp.model;

import com.quizapp.utils.QuizType;

import java.util.Arrays;
import java.util.Objects;

public class Question {
    private String prompt;
    private String[] options;
    private String answer;
    private String clientAnswer;
    private QuizType type;

    public Question(String prompt, String[] options, String answer) {
        this.prompt = prompt;
        this.options = options;
        this.answer = answer;
    }

    public Question() {
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String[] getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public boolean isCorrectAnswer(String clientAnswer) {
        return Objects.equals(answer, clientAnswer);
    }

    public String getClientAnswer() {
        return clientAnswer;
    }

    public QuizType getType() {
        return type;
    }

    public boolean isCorrect() {
        return Objects.equals(answer, clientAnswer);
    }

    public void setType(QuizType type) {
        this.type = type;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "prompt='" + prompt + '\'' +
                ", options=" + Arrays.toString(options) +
                ", answer='" + answer + '\'' +
                '}';
    }
}
