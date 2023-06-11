package com.quizapp.model;

import java.util.Arrays;
import java.util.Objects;

public class Question {
    private String prompt;
    private String[] options;
    private String answer;
    private String clientAnswer;

    public Question(String prompt, String[] options, String answer) {
        this.prompt = prompt;
        this.options = options;
        this.answer = answer;
    }

    public Question(String prompt, String answer){
        this.prompt = prompt;
        this.answer = answer;
    }

    public String getPrompt() {
        return prompt;
    }

    public String[] getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String[] Options) {
        this.options = Options;
    }

    public boolean isCorrectAnswer(String clientAnswer) {
        return Objects.equals(answer, clientAnswer);
    }

    public String getClientAnswer() {
        return clientAnswer;
    }

    public void setClientAnswer(String clientAnswer) {
        this.clientAnswer = clientAnswer;
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
