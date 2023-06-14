package com.quizapp.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            JsonObject jsonData = gson.fromJson(reader, JsonObject.class);

            JsonArray quizArray = jsonData.getAsJsonArray("quiz");
            if (quizArray != null && quizArray.size() > 0) {
                JsonObject quizObject = quizArray.get(0).getAsJsonObject();

                int quizId = quizObject.get("id").getAsInt();
                String quizType = quizObject.get("type").getAsString();
                String difficulty = quizObject.get("difficulty").getAsString();
                String topic = quizObject.get("topic").getAsString();

                Quiz quiz = new Quiz();
                quiz.setId(quizId);
                quiz.setType(QuizType.valueOf(quizType));
                quiz.setDifficulty(QuizDifficulty.valueOf(difficulty));
                quiz.setTopic(topic);

                JsonArray questionArray = quizObject.getAsJsonArray("questions");
                if (questionArray != null && questionArray.size() > 0) {
                    for (JsonElement questionElement : questionArray) {
                        JsonObject questionObject = questionElement.getAsJsonObject();
                        String prompt = questionObject.get("question").getAsString();
                        JsonArray optionsArray = questionObject.getAsJsonArray("options");
                        String answer = questionObject.get("answer").getAsString();

                        List<String> options = new ArrayList<>();
                        for (JsonElement optionElement : optionsArray) {
                            options.add(optionElement.getAsString());
                        }

                        Question question = new Question();
                        question.setPrompt(prompt);
                        question.setOptions(options.toArray(new String[0]));
                        question.setAnswer(answer);
                        question.setType(QuizType.MULTIPLE_CHOICE); // Assuming all questions are multiple choice

                        quiz.addQuestion(question);
                    }
                }

                return quiz;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading quiz from JSON file: " + jsonFile.getAbsolutePath());
        }

        return null; // Return null if the quiz data is not found or is invalid
    }


}
