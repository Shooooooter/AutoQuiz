package com.quizapp.controller;

import com.quizapp.model.Quiz;
import com.quizapp.service.QuizTakingService;
import com.quizapp.service.QuizGeneratorService;
import com.quizapp.service.QuizLoader;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import java.util.Scanner;
import java.util.List;

public class QuizController {
    private final QuizGeneratorService generatorService;
    private final QuizTakingService takingService;
    private final QuizLoader quizLoader;
    private final Scanner scanner;

    public QuizController() {
        generatorService = new QuizGeneratorService();
        takingService = new QuizTakingService();
        quizLoader = new QuizLoader(); // Instantiate the QuizLoader object
        scanner = new Scanner(System.in);
    }

    public void startQuiz() {
        // Display options for quiz generation
        System.out.println("Welcome to the Intelligent Quiz Generator!");
        System.out.println("Please select the quiz options:");

        // Select quiz type, topic, and difficulty
        QuizType quizType = selectQuizType();
        Topic topic = selectTopic();
        QuizDifficulty quizDifficulty = selectQuizDifficulty();
        int numQuestions = selectNumberOfQuestions();

        // Generate the quizzes
        List<Quiz> quizzes = generatorService.generateQuizzes(numQuestions, quizType, topic, quizDifficulty);

        // Load additional quizzes from CSV files
        List<Quiz> loadedQuizzes = quizLoader.loadQuizzes();
        quizzes.addAll(loadedQuizzes);

        // Iterate over the quizzes and start taking each one
        for (Quiz quiz : quizzes) {
            takingService.takeQuiz(quiz);
        }
    }


    private QuizType selectQuizType() {
        System.out.println("Select quiz type:");
        System.out.println("1. Multiple Choice");
        System.out.println("2. True/False");
        System.out.println("3. Fill In The Blank");
        int choice = scanner.nextInt();
        if (choice == 1) {
            return QuizType.MULTIPLE_CHOICE;
        } else if (choice == 2) {
            return QuizType.TRUE_FALSE;
        }else if (choice == 3) {
            return QuizType.FILL_IN_THE_BLANK;
        }else {
            System.out.println("Invalid choice. Defaulting to Multiple Choice.");
            return QuizType.MULTIPLE_CHOICE;
        }
    }

    private Topic selectTopic() {
        System.out.println("Enter quiz topic:");
        scanner.nextLine(); // Consume the newline character after previous nextInt() call
        String topicName = scanner.nextLine();
        return new Topic(topicName);
    }

    private int selectNumberOfQuestions() {
        System.out.println("Enter the number of questions (maximum 10):");
        int numQuestions = scanner.nextInt();
        if (numQuestions <= 0) {
            System.out.println("Invalid number of questions. Defaulting to 1 question.");
            return 1;
        } else if (numQuestions > 10) {
            System.out.println("Number of questions exceeds the maximum limit. Defaulting to 10 questions.");
            return 10;
        } else {
            return numQuestions;
        }
    }

    private QuizDifficulty selectQuizDifficulty() {
        System.out.println("Select quiz difficulty:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> {
                return QuizDifficulty.EASY;
            }
            case 2 -> {
                return QuizDifficulty.MEDIUM;
            }
            case 3 -> {
                return QuizDifficulty.HARD;
            }
            default -> {
                System.out.println("Invalid choice. Defaulting to Easy.");
                return QuizDifficulty.EASY;
            }
        }
    }

    public static void main(String[] args) {
        QuizController quizController = new QuizController();
        quizController.startQuiz();
    }
}
