package com.quizapp.service;

import com.quizapp.model.Quiz;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuizGeneratorService {
    private static final String PYTHON_SCRIPT_PATH = "path/to/your/python/script.py"; // Replace with the actual path to your Python script

    public List<Quiz> generateQuizzes(int numQuestions, QuizType quizType, Topic topic, QuizDifficulty quizDifficulty) {
        List<Quiz> quizzes = new ArrayList<>();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", PYTHON_SCRIPT_PATH,
                    "--num_questions", String.valueOf(numQuestions),
                    "--quiz_type", quizType.name(),
                    "--topic", topic.getName(),
                    "--difficulty", quizDifficulty.name());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> csvFiles = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                csvFiles.add(line);
            }

            if (process.waitFor(5, TimeUnit.MINUTES)) {
                for (String csvFile : csvFiles) {
                    Quiz quiz = QuizLoader.loadQuizFromCSV(new File(csvFile));
                    quizzes.add(quiz);
                }
            } else {
                System.out.println("Quiz generation process timed out.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("An error occurred during quiz generation.");
        }

        return quizzes;
    }
}
