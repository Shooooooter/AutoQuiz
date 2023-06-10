package com.quizapp.service;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizGeneratorService {

    private static final String PYTHON_SCRIPT_PATH = "F:\\Java1\\Management\\AutoQuiz\\src\\main\\java\\com\\quizapp\\service\\quiz_generator.py";

    public static Quiz generateQuiz(int noOfQuestions, QuizType quizType, Topic topic, QuizDifficulty quizDifficulty) throws IOException, InterruptedException {
        try {
            // create command to run Python script with the required arguments
            List<String> command = new ArrayList<>();
            command.add("python");
            command.add(PYTHON_SCRIPT_PATH);
            command.add("--no-of-questions");
            command.add(String.valueOf(noOfQuestions));
            command.add("--quiz-type");
            command.add(quizType.name());
            command.add("--topic");
            command.add(topic.getName());
            command.add("--quiz-difficulty");
            command.add(quizDifficulty.name());

            // start the process
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            // read output from Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String questionsText = reader.readLine();
            String answersText = reader.readLine();
            String[] questions = questionsText.split("\n");
            String[] answers = answersText.split("\n");
            List<Question> questionList = new ArrayList<>();

            for (int i = 0; i < questions.length; i++) {
                Question question = new Question(String.valueOf(i + 1), answers[i]);
                question.setPrompt(questions[i]);
                questionList.add(question);
            }

            // wait for the process to finish and check if there were any errors
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // read error output from Python script
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Error running command: ").append(command).append("\n");
                String line;
                while ((line = errorReader.readLine()) != null) {
                    errorMessage.append(line).append("\n");
                }
                throw new IOException(errorMessage.toString());
            }

            // create and return quiz object
            return new Quiz(0, quizType, quizDifficulty, topic, questionList);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating quiz.");
        }
    }
}
