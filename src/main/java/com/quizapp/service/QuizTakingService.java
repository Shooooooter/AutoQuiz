package com.quizapp.service;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizTakingService {
    private Scanner scanner;

    public QuizTakingService() {
        this.scanner = new Scanner(System.in);
    }

    public void takeQuiz(Quiz quiz) {
        int totalQuestions = quiz.getQuestions().size();

        // Ask client how many questions they want to answer
        System.out.println("There are " + totalQuestions + " questions in this quiz. How many questions would you like to answer?");
        int numQuestions = scanner.nextInt();

        if (numQuestions <= 0) {
            System.out.println("Invalid number of questions. Please try again.");
            return;
        }

        if (numQuestions > totalQuestions) {
            System.out.println("The quiz only has " + totalQuestions + " questions. You cannot answer more than that.");
            return;
        }

        // Randomly select the specified number of questions from the quiz
        List<Question> selectedQuestions = new ArrayList<>();
        List<Question> allQuestions = quiz.getQuestions();

        for (int i = 0; i < numQuestions; i++) {
            int randomIndex = (int) (Math.random() * allQuestions.size());
            selectedQuestions.add(allQuestions.get(randomIndex));
            allQuestions.remove(randomIndex);
        }

        // Display each question and prompt the client for an answer
        int numCorrectAnswers = 0;
        for (Question question : selectedQuestions) {
            System.out.println(question.getPrompt());

            String clientAnswer = scanner.nextLine();

            if (question.checkAnswer(clientAnswer)) {
                System.out.println("Correct!");
                numCorrectAnswers++;
            } else {
                System.out.println("Incorrect. The correct answer is: " + question.getAnswer());
            }
        }

        // Display quiz results and feedback
        System.out.println("You answered " + numCorrectAnswers + " out of " + numQuestions + " questions correctly.");

        if (numCorrectAnswers == numQuestions) {
            System.out.println("Congratulations! You got a perfect score!");
        } else {
            System.out.println("Here are the correct answers for the questions you answered incorrectly:");
            for (Question question : selectedQuestions) {
                if (!question.checkAnswer(question.getClientAnswer())) {
                    System.out.println(question.getPrompt() + " Correct answer: " + question.getAnswer());
                }
            }
        }
    }
}
