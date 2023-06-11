package com.quizapp.GUI;

import com.quizapp.service.QuizLoader;
import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.controller.QuizController;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuizAppGUI extends Application {
    private QuizController quizController;
    private ListView<Quiz> quizListView;

    private Stage primaryStage;
    private VBox quizLayout;
    private Label questionLabel;
    private ToggleGroup optionsToggleGroup;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the QuizController
        quizController = new QuizController(this);

        this.primaryStage = primaryStage;

        // Create GUI components
        Label numQuestionsLabel = new Label("Number of Questions:");
        ComboBox<Integer> numQuestionsComboBox = new ComboBox<>();
        numQuestionsComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Label quizTypeLabel = new Label("Quiz Type:");
        ComboBox<QuizType> quizTypeComboBox = new ComboBox<>();
        quizTypeComboBox.getItems().addAll(QuizType.values());

        Label topicLabel = new Label("Topic:");
        TextField topicTextField = new TextField();

        Label difficultyLabel = new Label("Difficulty:");
        ComboBox<QuizDifficulty> difficultyComboBox = new ComboBox<>();
        difficultyComboBox.getItems().addAll(QuizDifficulty.values());

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(event -> {
            int numQuestions = numQuestionsComboBox.getValue();
            QuizType quizType = quizTypeComboBox.getValue();
            Topic topic = new Topic(topicTextField.getText());
            QuizDifficulty difficulty = difficultyComboBox.getValue();
            Quiz generatedQuiz = quizController.generateQuiz(numQuestions, quizType, topic, difficulty);
            displayQuizzes(List.of(generatedQuiz)); // Display the newly generated quiz in the sidebar
        });

        // Create the sidebar for displaying existing quizzes
        quizListView = new ListView<>();

        // Load quizzes from JSON files and display buttons on the sidebar
        QuizLoader quizLoader = new QuizLoader();
        List<Quiz> quizzes = quizLoader.loadQuizzes();
        displayQuizzes(quizzes);

        // Create layout and add components
        VBox sidebarLayout = new VBox(10, quizListView);
        sidebarLayout.setPadding(new Insets(10));
        sidebarLayout.setAlignment(Pos.TOP_LEFT);

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
                    openQuizWindow(selectedQuiz);
                }
            }
        });
    }

    // Method to display quizzes in the sidebar
    private void displayQuizzes(List<Quiz> quizzes) {
        quizListView.getItems().clear();
        quizListView.getItems().addAll(quizzes);
        quizListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
                if (selectedQuiz != null) {
                    openQuizWindow(selectedQuiz);
                }
            }
        });
    }



    private void openQuizWindow(Quiz quiz) {
        Stage quizStage = new Stage();
        quizStage.setTitle("Quiz: " + quiz.getId());

        GridPane quizLayout = new GridPane();
        quizLayout.setPadding(new Insets(10));
        quizLayout.setHgap(10);
        quizLayout.setVgap(10);

        // Display each question with radio buttons
        int rowIndex = 0;
        for (Question question : quiz.getQuestions()) {
            Label questionLabel = new Label(question.getPrompt());

            ToggleGroup optionsToggleGroup = new ToggleGroup();

            List<RadioButton> optionRadioButtons = getOptionRadioButtons(question.getOptions(), optionsToggleGroup);
            quizLayout.addRow(rowIndex++, questionLabel);
            for (int i = 0; i < optionRadioButtons.size(); i++) {
                quizLayout.addRow(rowIndex++, optionRadioButtons.get(i));
            }
        }

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            int correctAnswers = 0;
            int totalQuestions = quiz.getQuestions().size();

            // Iterate through the questions and check the selected answer
            for (Question question : quiz.getQuestions()) {
                RadioButton selectedOption = (RadioButton) optionsToggleGroup.getSelectedToggle();
                if (selectedOption != null && selectedOption.getUserData().equals(question.getAnswer())) {
                    correctAnswers++;
                }
            }

            // Display the quiz result
            displayQuizResults(correctAnswers, totalQuestions);
            quizStage.close();
        });

        quizLayout.add(submitButton, 0, rowIndex);

        Scene scene = new Scene(quizLayout);
        quizStage.setScene(scene);
        quizStage.show();
    }

    private List<RadioButton> getOptionRadioButtons(String[] options, ToggleGroup toggleGroup) {
        return Arrays.stream(options)
                .map(option -> {
                    RadioButton radioButton = new RadioButton(option);
                    radioButton.setUserData(option);
                    radioButton.setToggleGroup(toggleGroup);
                    return radioButton;
                })
                .collect(Collectors.toList());
    }
    // Method to display a question and its options
    public void displayQuestion(Question question) {
        questionLabel = new Label(question.getPrompt());
        quizLayout.getChildren().clear();
        quizLayout.getChildren().add(questionLabel);

        optionsToggleGroup = new ToggleGroup();

        List<RadioButton> optionRadioButtons = getOptionRadioButtons(question.getOptions());
        quizLayout.getChildren().addAll(optionRadioButtons);
    }

    private List<RadioButton> getOptionRadioButtons(String[] options) {
        return Arrays.stream(options)
                .map(this::createOptionRadioButton)
                .collect(Collectors.toList());
    }


    private RadioButton createOptionRadioButton(String option) {
        RadioButton radioButton = new RadioButton(option);
        radioButton.setUserData(option);
        radioButton.setToggleGroup(optionsToggleGroup);
        return radioButton;
    }

    // Method to update the displayed question and options
    public void updateQuestion(Question question) {
        questionLabel.setText(question.getPrompt());

        List<RadioButton> optionRadioButtons = getOptionRadioButtons(question.getOptions());
        quizLayout.getChildren().removeAll(optionRadioButtons);
        quizLayout.getChildren().addAll(optionRadioButtons);
    }

    // Method to display the quiz result
    // Method to display the quiz result
    // Method to display the quiz result
    public void displayQuizResult(Quiz quiz, int correctAnswers) {
        quizLayout.getChildren().clear();
        Label resultLabel = new Label("Quiz Result");
        Label quizNameLabel = new Label("Quiz: " + quiz.getId());
        Label correctAnswersLabel = new Label("Correct Answers: " + correctAnswers);

        quizLayout.getChildren().addAll(resultLabel, quizNameLabel, correctAnswersLabel);
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void displayQuizResults(int correctAnswer, int totalQuestions) {
    }
}
