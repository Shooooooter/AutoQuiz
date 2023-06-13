package com.quizapp.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;

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
                        Quiz loadedQuiz = loadQuizFromJSON(quizFile);
                        quizzes.add(loadedQuiz);
                    }
                }
            }
        }

        return quizzes;
    }

    private static Quiz loadQuizFromJSON(File jsonFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> quizData = gson.fromJson(reader, type);

            Quiz quiz = new Quiz();

            List<Map<String, Object>> quizList = gson.fromJson(gson.toJsonTree(quizData.get("quiz")), new TypeToken<List<Map<String, Object>>>() {}.getType());
            for (Map<String, Object> quizMap : quizList) {
                double id = (double) quizMap.get("id"); // Parse as double
                int quizId = (int) id; // Cast to int

                String quizType = (String) quizMap.get("type");
                String difficulty = (String) quizMap.get("difficulty");
                String topic = (String) quizMap.get("topic");

                quiz.setId(quizId); // Set the quiz ID as an int
                quiz.setType(QuizType.valueOf(quizType));
                quiz.setDifficulty(QuizDifficulty.valueOf(difficulty));
                quiz.setTopic(topic);

                List<Map<String, Object>> questionList = gson.fromJson(gson.toJsonTree(quizMap.get("questions")), new TypeToken<List<Map<String, Object>>>() {}.getType());
                for (Map<String, Object> questionMap : questionList) {
                    String prompt = (String) questionMap.get("question");
                    List<String> options = gson.fromJson(gson.toJsonTree(questionMap.get("options")), new TypeToken<List<String>>() {}.getType());
                    String answer = (String) questionMap.get("answer");

                    Question question = new Question();
                    question.setPrompt(prompt);
                    question.setOptions(options.toArray(new String[0]));
                    question.setAnswer(answer);

                    quiz.addQuestion(question);
                }
            }

            return quiz;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading quiz from JSON file: " + jsonFile.getAbsolutePath());
        }
    }
}
