package com.quizapp.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizLoader {
    private static final String JSON_FILE_DIRECTORY = "src/Res/"; // Replace with the actual directory path where your JSON files are located
    private static final String JSON_FILE_EXTENSION = ".json";

    public List<Quiz> loadQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();

        // Load quizzes from JSON files
        File quizzesDir = new File(JSON_FILE_DIRECTORY);
        if (quizzesDir.isDirectory()) {
            File[] quizFiles = quizzesDir.listFiles();
            if (quizFiles != null) {
                for (File quizFile : quizFiles) {
                    if (quizFile.getName().endsWith(JSON_FILE_EXTENSION)) {
                        List<Quiz> loadedQuizzes = loadQuizzesFromJSON(quizFile);
                        quizzes.addAll(loadedQuizzes);
                    }
                }
            }
        }

        return quizzes;
    }

    private static List<Quiz> loadQuizzesFromJSON(File jsonFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Map<String, String>>>() {}.getType();
            List<Map<String, String>> quizList = gson.fromJson(reader, type);

            List<Quiz> quizzes = new ArrayList<>();
            for (Map<String, String> quizMap : quizList) {
                String prompt = quizMap.get("prompt");
                String answer = quizMap.get("answer");

                Question question = new Question(prompt, answer);
                Quiz quiz = new Quiz(questions);
                quiz.addQuestion(question);
                quizzes.add(quiz);
            }

            return quizzes;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading quizzes from JSON file: " + jsonFile.getAbsolutePath());
        }
    }
}
