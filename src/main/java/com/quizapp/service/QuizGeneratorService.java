package com.quizapp.service;

import com.quizapp.model.Quiz;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class QuizGeneratorService {
    private static final String PYTHON_INTERPRETER_PATH = "F:\\Java1\\Management\\AutoQuiz\\venv\\Scripts\\python.exe"; // Replace with the actual path to your Python interpreter
    private static final String PYTHON_SCRIPT_PATH = "F:\\Java1\\Management\\AutoQuiz\\src\\main\\java\\com\\quizapp\\GPTcom\\__main__.py"; // Replace with the actual path to your Python script

    public static Quiz generateQuiz(int numQuestions, QuizType quizType, Topic topic, QuizDifficulty quizDifficulty) {
        Quiz quiz = null;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(PYTHON_INTERPRETER_PATH, PYTHON_SCRIPT_PATH,
                    "--num_questions", String.valueOf(numQuestions),
                    "--quiz_type", quizType.name(),
                    "--topic", topic.getName(),
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

            if (process.waitFor(5, TimeUnit.MINUTES)) {
                if (quizJsonFile != null) {
                    Gson gson = new Gson();
                    quiz = gson.fromJson(new FileReader(quizJsonFile), Quiz.class);
                } else {
                    System.out.println("Failed to retrieve quiz JSON file.");
                }
            } else {
                System.out.println("Quiz generation process timed out.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("An error occurred during quiz generation.");
        }

        return quiz;
    }
}
