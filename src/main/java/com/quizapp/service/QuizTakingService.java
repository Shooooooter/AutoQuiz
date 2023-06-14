package com.quizapp.service;

import com.quizapp.model.Question;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;

import java.util.List;

public class QuizTakingService {
    public void takeQuiz(List<Question> questions) {
        int totalQuestions = questions.size();

        // Display each question and prompt the client for an answer
        int numCorrectAnswers = 0;
        for (Question question : questions) {
            String prompt = question.getPrompt();
            String answer = question.getAnswer();

            // Display the question in a dialog box and get the user's answer
            String clientAnswer = showQuestionDialog(prompt);

            if (question.isCorrectAnswer(clientAnswer.toLowerCase())) { // Compare case-insensitive answers
                showInfoDialog("Correct!");
                numCorrectAnswers++;
            } else {
                showInfoDialog("Incorrect. The correct answer is: " + answer);
            }
        }

        // Display quiz results and feedback
        showInfoDialog("You answered " + numCorrectAnswers + " out of " + totalQuestions + " questions correctly.");

        if (numCorrectAnswers == totalQuestions) {
            showInfoDialog("Congratulations! You got a perfect score!");
        } else {
            StringBuilder feedback = new StringBuilder("Here are the correct answers for the questions you answered incorrectly:\n");
            for (Question question : questions) {
                if (!question.isCorrectAnswer(question.getClientAnswer())) {
                    feedback.append(question.getPrompt()).append(" Correct answer: ").append(question.getAnswer()).append("\n");
                }
            }
            showInfoDialog(feedback.toString());
        }
    }

    private String showQuestionDialog(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Question");
        dialog.setHeaderText(null);
        dialog.setContentText(prompt);

        dialog.initModality(Modality.APPLICATION_MODAL);

        // Show the dialog and wait for the user's response
        return dialog.showAndWait().orElse("");
    }

    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.initModality(Modality.APPLICATION_MODAL);

        // Show the alert dialog
        alert.showAndWait();
    }
}
