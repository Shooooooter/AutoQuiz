package com.quizapp.service;

import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class QuizGeneratorService {
    private static final String PYTHON_INTERPRETER_PATH = "venv/Scripts/python.exe"; // Replace with the actual path to your Python interpreter
    private static final String PYTHON_SCRIPT_PATH = "F:\\Java1\\Management\\AutoQuiz\\src\\main\\java\\com\\quizapp\\GPTcom\\__main__.py"; // Replace with the actual path to your Python script

    public static void generateQuiz(int numQuestions, QuizType quizType, String topic, QuizDifficulty quizDifficulty) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(PYTHON_INTERPRETER_PATH, PYTHON_SCRIPT_PATH,
                    "--num_questions", String.valueOf(numQuestions),
                    "--quiz_type", quizType.name(),
                    "--topic", topic,
                    "--difficulty", quizDifficulty.name());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            String quizJsonFile = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Quiz JSON file:")) {
                    quizJsonFile = new File(line.substring(line.indexOf(":") + 1).trim()).getAbsolutePath();
                    System.out.println("Quiz JSON file: " + quizJsonFile);
                }
            }

            int exitCode = process.waitFor(); // Wait for the Python script to complete
            if (exitCode != 0) {
                // Handle any error or failure case
                throw new RuntimeException("Failed to run the Python script. Exit code: " + exitCode);
            }

            if (quizJsonFile != null) {
                // Process the quiz JSON file as needed (e.g., save it to a database, send it via email, etc.)
                System.out.println("Quiz JSON file generated successfully.");
            } else {
                System.out.println("Failed to retrieve quiz JSON file.");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
