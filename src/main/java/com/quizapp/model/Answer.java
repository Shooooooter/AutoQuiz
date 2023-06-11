package com.quizapp.model;

public class Answer {
    private String text;
    private boolean isCorrect;

    public Answer(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect(String userAnswer) {
        if (this.text == userAnswer){
            return true;
        }
        else {
            return false;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return text;
    }
}
