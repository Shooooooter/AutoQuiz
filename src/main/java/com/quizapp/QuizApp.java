package com.quizapp;

import com.quizapp.controller.QuizController;

public class QuizApp {
    public static void main(String[] args) {
        QuizController quizController = new QuizController();
        quizController.startQuiz();
    }
}
