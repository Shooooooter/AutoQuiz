package com.quizapp.GUI;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.service.QuizGeneratorService;
import com.quizapp.service.QuizLoader;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class QuizAppGUI extends Application {
    private VBox quizLayout;
    private List<Question> questions; // Declare the questions variable

    @Override
    public void start(Stage primaryStage) {
        // Create the UI components
        ListView<Quiz> quizListView = new ListView<>();
        Label numQuestionsLabel = new Label("Number of Questions:");
        ComboBox<Integer> numQuestionsComboBox = new ComboBox<>();
        Label quizTypeLabel = new Label("Quiz Type:");
        ComboBox<QuizType> quizTypeComboBox = new ComboBox<>();
        Label topicLabel = new Label("Topic:");
        TextField topicTextField = new TextField();
        Label difficultyLabel = new Label("Difficulty:");
        ComboBox<QuizDifficulty> difficultyComboBox = new ComboBox<>();
        Button generateButton = new Button("Generate Quiz");
        QuizLoader quizLoader = new QuizLoader();
        List<Quiz> quizzes = quizLoader.loadQuizzes();
        quizListView.getItems().addAll(quizzes);

        // Configure the UI components
        quizListView.setPrefWidth(200);
        quizListView.setPrefHeight(400);
        numQuestionsComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        numQuestionsComboBox.setValue(10);
        quizTypeComboBox.getItems().addAll(QuizType.MULTIPLE_CHOICE, QuizType.TRUE_FALSE);
        quizTypeComboBox.setValue(QuizType.MULTIPLE_CHOICE);
        difficultyComboBox.getItems().addAll(QuizDifficulty.EASY, QuizDifficulty.MEDIUM, QuizDifficulty.HARD);
        difficultyComboBox.setValue(QuizDifficulty.MEDIUM);

        // Create the layout
        TabPane tabPane = new TabPane();

        // Create the "Take Quiz" tab
        Tab takeQuizTab = new Tab("Take Quiz");
        VBox takeQuizLayout = new VBox(10);
        takeQuizLayout.setPadding(new Insets(10));
        takeQuizLayout.getChildren().add(quizListView);
        takeQuizTab.setContent(takeQuizLayout);
        tabPane.getTabs().add(takeQuizTab);

        // Create the "Make Quiz" tab
        Tab makeQuizTab = new Tab("Make Quiz");
        VBox makeQuizLayout = new VBox(10);
        makeQuizLayout.setPadding(new Insets(10));
        makeQuizLayout.getChildren().addAll(numQuestionsLabel, numQuestionsComboBox, quizTypeLabel,
                quizTypeComboBox, topicLabel, topicTextField, difficultyLabel, difficultyComboBox, generateButton);
        makeQuizTab.setContent(makeQuizLayout);
        tabPane.getTabs().add(makeQuizTab);

        // Set the selected tab to "Take Quiz" by default
        tabPane.getSelectionModel().select(takeQuizTab);

        // Create the scene and set it on the stage
        Scene scene = new Scene(tabPane, 600, 400);
        primaryStage.setTitle("Quiz App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Handle generateButton click event
        generateButton.setOnAction(event -> {
            int numQuestions = numQuestionsComboBox.getValue();
            QuizType quizType = quizTypeComboBox.getValue();
            String topic = topicTextField.getText(); // Convert the String to a Topic object
            QuizDifficulty difficulty = difficultyComboBox.getValue();

            // Generate the quiz based on the selected options
            questions = QuizGeneratorService.generateQuiz(numQuestions, quizType, topic, difficulty).getQuestions();

            // Display the generated quiz
            startQuiz(questions);
        });

        // Handle quizListView selection event
        quizListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                questions = newValue.getQuestions();
                startQuiz(questions);
            }
        });
    }

    private void startQuiz(List<Question> questions) {
        quizLayout.getChildren().clear();

        for (Question question : questions) {
            VBox questionLayout = new VBox(10);
            Label questionLabel = new Label(question.getPrompt());
            ToggleGroup answerGroup = new ToggleGroup();

            if (question.getType() == QuizType.MULTIPLE_CHOICE) {
                List<RadioButton> answerRadioButtons = Arrays.stream(question.getOptions())
                        .map(option -> {
                            RadioButton radioButton = new RadioButton(option);
                            radioButton.setToggleGroup(answerGroup);
                            return radioButton;
                        })
                        .toList();
                questionLayout.getChildren().add(questionLabel);
                questionLayout.getChildren().addAll(answerRadioButtons);
            } else if (question.getType() == QuizType.TRUE_FALSE) {
                RadioButton trueRadioButton = new RadioButton("True");
                RadioButton falseRadioButton = new RadioButton("False");
                trueRadioButton.setToggleGroup(answerGroup);
                falseRadioButton.setToggleGroup(answerGroup);
                questionLayout.getChildren().add(questionLabel);
                questionLayout.getChildren().addAll(trueRadioButton, falseRadioButton);
            }

            quizLayout.getChildren().add(questionLayout);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
