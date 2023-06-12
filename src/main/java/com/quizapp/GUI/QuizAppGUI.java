package com.quizapp.GUI;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;

public class QuizAppGUI extends Application {
    private VBox quizLayout;
    private ToggleGroup optionsToggleGroup;

    @Override
    public void start(Stage primaryStage) {
        // Create the UI components
        ListView<Quiz> quizListView = new ListView<>();
        Label numQuestionsLabel = new Label("Number of Questions:");
        ComboBox<Integer> numQuestionsComboBox = new ComboBox<>();
        Label quizTypeLabel = new Label("Quiz Type:");
        ComboBox<String> quizTypeComboBox = new ComboBox<>();
        Label topicLabel = new Label("Topic:");
        TextField topicTextField = new TextField();
        Label difficultyLabel = new Label("Difficulty:");
        ComboBox<String> difficultyComboBox = new ComboBox<>();
        Button generateButton = new Button("Generate Quiz");

        // Configure the UI components
        quizListView.setPrefWidth(200);
        quizListView.setPrefHeight(400);
        numQuestionsComboBox.getItems().addAll(1, 2, 3,4,5,6,7,8,9,10);
        numQuestionsComboBox.setValue(10);
        quizTypeComboBox.getItems().addAll("Multiple Choice", "True/False");
        quizTypeComboBox.setValue("Multiple Choice");
        difficultyComboBox.getItems().addAll("Easy", "Medium", "Hard");
        difficultyComboBox.setValue("Medium");

        // Create the layout
        VBox sidebarLayout = new VBox(10);
        sidebarLayout.setPadding(new Insets(10));
        sidebarLayout.getChildren().add(new Label("Quizzes"));
        sidebarLayout.getChildren().add(quizListView);

        GridPane inputGridPane = new GridPane();
        inputGridPane.setPadding(new Insets(10));
        inputGridPane.setHgap(10);
        inputGridPane.setVgap(10);

        inputGridPane.add(numQuestionsLabel, 0, 0);
        inputGridPane.add(numQuestionsComboBox, 1, 0);
        inputGridPane.add(quizTypeLabel, 0, 1);
        inputGridPane.add(quizTypeComboBox, 1, 1);
        inputGridPane.add(topicLabel, 0, 2);
        inputGridPane.add(topicTextField, 1, 2);
        inputGridPane.add(difficultyLabel, 0, 3);
        inputGridPane.add(difficultyComboBox, 1, 3);
        inputGridPane.add(generateButton, 1, 4);

        quizLayout = new VBox();
        quizLayout.setAlignment(Pos.TOP_LEFT);
        quizLayout.setPadding(new Insets(10));
        quizLayout.setSpacing(10);

        HBox mainLayout = new HBox(sidebarLayout, inputGridPane, quizLayout);
        mainLayout.setPadding(new Insets(10));
        mainLayout.setSpacing(10);

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quiz App");
        primaryStage.show();

        // Add double-click event handler for quizListView
        quizListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
                if (selectedQuiz != null) {
                    startQuiz(selectedQuiz);
                }
            }
        });

        // Add event handler for generateButton
        generateButton.setOnAction(event -> {
            int numQuestions = numQuestionsComboBox.getValue();
            String quizType = quizTypeComboBox.getValue();
            String topic = topicTextField.getText();
            String difficulty = difficultyComboBox.getValue();

            // Generate the quiz
            Quiz quiz = generateQuiz(numQuestions, quizType, topic, difficulty);
            if (quiz != null) {
                // Clear previous quiz layout
                quizLayout.getChildren().clear();
                quizListView.getSelectionModel().clearSelection();

                // Add the quiz to the list
                quizListView.getItems().add(quiz);
            }
        });
    }

    private void startQuiz(Quiz quiz) {
        int totalQuestions = quiz.getQuestions().size();
        ObjectProperty<Integer> questionIndex = new SimpleObjectProperty<>(0);

        displayQuestion(quiz.getQuestions().get(questionIndex.get()));

        Button nextButton = new Button("Next");

        nextButton.setOnAction(event -> {
            RadioButton selectedOption = getSelectedOption(questionIndex.get());
            if (selectedOption != null) {
                String selectedAnswer = (String) selectedOption.getUserData();
                Question currentQuestion = quiz.getQuestions().get(questionIndex.get());
                if (currentQuestion.isCorrectAnswer(selectedAnswer)) {
                    System.out.println("Correct!");
                } else {
                    System.out.println("Incorrect!");
                }

                int currentIndex = questionIndex.get();
                questionIndex.set(currentIndex + 1);

                if (currentIndex < totalQuestions) {
                    displayQuestion(quiz.getQuestions().get(currentIndex));
                } else {
                    displayQuizResults(quiz);
                }
            }
        });

        quizLayout.getChildren().add(nextButton);
    }

    private RadioButton getSelectedOption(int questionIndex) {
        List<Node> radioButtons = quizLayout.getChildren().filtered(node -> node instanceof RadioButton);
        for (Node radioButton : radioButtons) {
            RadioButton optionRadioButton = (RadioButton) radioButton;
            if (optionRadioButton.isSelected() && optionRadioButton.getId().startsWith("option-" + questionIndex)) {
                return optionRadioButton;
            }
        }
        return null;
    }

    public void displayQuizResults(Quiz quiz) {
        int totalQuestions = quiz.getQuestions().size();
        int correctAnswers = calculateCorrectAnswers(quiz);

        displayQuizResults(quiz, correctAnswers, totalQuestions);
    }

    public void displayQuizResults(Quiz quiz, int correctAnswers, int totalQuestions) {
        quizLayout.getChildren().clear();
        Label resultLabel = new Label("Quiz Result");
        Label quizNameLabel = new Label("Quiz: " + quiz.getId());
        Label correctAnswersLabel = new Label("Correct Answers: " + correctAnswers + "/" + totalQuestions);

        quizLayout.getChildren().addAll(resultLabel, quizNameLabel, correctAnswersLabel);
    }


    private int calculateCorrectAnswers(Quiz quiz) {
        int correctAnswers = 0;
        for (Question question : quiz.getQuestions()) {
            if (question.isCorrectAnswer(question.getClientAnswer())) {
                correctAnswers++;
            }
        }
        return correctAnswers;
    }



    public void displayQuestion(Question question) {
        Label questionLabel = new Label(question.getPrompt());
        quizLayout.getChildren().clear();
        quizLayout.getChildren().add(questionLabel);

        optionsToggleGroup = new ToggleGroup();

        List<RadioButton> optionRadioButtons = getOptionRadioButtons(optionsToggleGroup, 0, question.getOptions());
        quizLayout.getChildren().addAll(optionRadioButtons);
    }

    private List<RadioButton> getOptionRadioButtons(ToggleGroup toggleGroup, int questionIndex, String[] options) {
        List<RadioButton> radioButtons = new ArrayList<>();

        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                RadioButton radioButton = new RadioButton(options[i]);
                radioButton.setToggleGroup(toggleGroup);
                radioButton.setUserData(options[i]);
                radioButton.setId("option-" + questionIndex + "-" + (char) (i + 65)); // Set an ID for each radio button
                radioButtons.add(radioButton);
            }
        }

        return radioButtons;
    }

    private Quiz generateQuiz(int numQuestions, String quizType, String topic, String difficulty) {
        // Generate the quiz based on the provided parameters
        // Replace this with your own logic to generate quizzes
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
