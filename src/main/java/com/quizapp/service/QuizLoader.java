package com.quizapp.service;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizLoader {
    private static final String CSV_FILE_DIRECTORY = "src/Res/";
    private static final String CSV_FILE_EXTENSION = ".csv";

    public List<Quiz> loadQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();

        // Get all the CSV files in the specified directory
        File csvDirectory = new File(CSV_FILE_DIRECTORY);
        File[] csvFiles = csvDirectory.listFiles((dir, name) -> name.endsWith(CSV_FILE_EXTENSION));

        // Check if csvFiles is not null before iterating over it
        if (csvFiles != null) {
            // Iterate over each CSV file and load the quiz
            for (File csvFile : csvFiles) {
                Quiz quiz = loadQuizFromCSV(csvFile);
                quizzes.add(quiz);
            }
        }

        return quizzes;
    }

    public static Quiz loadQuizFromCSV(File csvFile) {
        List<Question> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // Skip the header line
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length == 2) {
                    String prompt = data[0];
                    String answer = data[1];
                    Question question = new Question(prompt, answer);
                    questions.add(question);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading quiz from CSV file: " + csvFile.getAbsolutePath());
        }

        // Extract quiz information from the CSV file name
        String fileName = csvFile.getName();
        String[] fileNameParts = fileName.split("_");
        if (fileNameParts.length < 4) {
            throw new RuntimeException("Invalid CSV file name: " + fileName);
        }

        QuizType quizType = QuizType.valueOf(fileNameParts[1]);
        QuizDifficulty quizDifficulty = QuizDifficulty.valueOf(fileNameParts[2]);

        // Create the Quiz object
        return new Quiz(0, quizType, quizDifficulty, Topic.DEFAULT_TOPIC, questions);
    }
}
