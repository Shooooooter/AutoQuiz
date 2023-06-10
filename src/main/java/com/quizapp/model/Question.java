package com.quizapp.model;

public class Question {
    private String prompt;
    private String answer;
    private String clientAnswer;

    public Question(String prompt, String answer) {
        this.prompt = prompt;
        this.answer = answer;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getClientAnswer() {
        return clientAnswer;
    }

    public void setClientAnswer(String clientAnswer) {
        this.clientAnswer = clientAnswer;
    }

    public boolean checkAnswer(String clientAnswer) {
        return answer.equalsIgnoreCase(clientAnswer);
    }

    @Override
    public String toString() {
        return "Question{" +
                "prompt='" + prompt + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
