package com.quizapp.controller;

import com.quizapp.GUI.QuizAppGUI;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.service.QuizGeneratorService;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import java.util.List;

public class QuizController {
    private final QuizAppGUI quizAppGUI;

    public QuizController(QuizAppGUI quizAppGUI) {
        this.quizAppGUI = quizAppGUI;
    }

    public Quiz generateQuiz(int numQuestions, QuizType quizType, Topic topic, QuizDifficulty difficulty) {
        // Generate quiz questions based on the quiz type, topic, and difficulty
        Quiz quiz = QuizGeneratorService.generateQuiz(numQuestions, quizType, topic, difficulty);
        List<Question> quizQuestions = quiz.getQuestions();

        // Initialize score and other relevant variables
        int totalQuestions = quizQuestions.size();
        int correctAnswers = 0;
        int currentQuestionIndex = 0;

        // Loop through the quiz questions and display them to the user
        while (currentQuestionIndex < totalQuestions) {
            Question currentQuestion = quizQuestions.get(currentQuestionIndex);

            // Display the current question to the user using the GUI
            quizAppGUI.displayQuestion(currentQuestion);

            // Wait for the user to provide an answer and retrieve it using the GUI
            String userAnswer = currentQuestion.getClientAnswer();

            // Check if the user's answer is correct and update the score
            if (currentQuestion.isCorrectAnswer(String.valueOf(userAnswer))) {
                correctAnswers++;
            }

            // Move to the next question
            currentQuestionIndex++;
        }

        // Display the quiz results to the user using the GUI
        quizAppGUI.displayQuizResults(quiz, correctAnswers, totalQuestions);
        return quiz;
    }
}
