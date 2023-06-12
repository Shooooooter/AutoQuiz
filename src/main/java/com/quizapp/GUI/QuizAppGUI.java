package com.quizapp.GUI;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.service.QuizGeneratorService;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;
import com.quizapp.utils.Topic;
import com.quizapp.service.QuizLoader;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

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
        inputGridPane.add(generateButton, 0, 4, 2, 1);

        quizLayout = new VBox(10);
        quizLayout.setPadding(new Insets(10));
        quizLayout.getChildren().add(inputGridPane);

        HBox mainLayout = new HBox(10);
        mainLayout.getChildren().addAll(sidebarLayout, quizLayout);

        // Create the scene and set it on the stage
        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setTitle("Quiz App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Handle generateButton click event
        generateButton.setOnAction(event -> {
            int numQuestions = numQuestionsComboBox.getValue();
            QuizType quizType = quizTypeComboBox.getValue();
            String topicName = topicTextField.getText();
            Topic topic = new Topic(topicName); // Convert the String to a Topic object
            QuizDifficulty difficulty = difficultyComboBox.getValue();

            // Generate the quiz based on the selected options
            QuizGeneratorService quizGenerator = new QuizGeneratorService();
            questions = quizGenerator.generateQuiz(numQuestions, quizType, topic, difficulty);

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
            Label questionLabel = new Label(question.getQuestionText());
            ToggleGroup answerGroup = new ToggleGroup();

            if (question.getType() == QuizType.MULTIPLE_CHOICE) {
                List<RadioButton> answerRadioButtons = Arrays.stream(question.getOptions())
                        .map(option -> {
                            RadioButton radioButton = new RadioButton(option);
                            radioButton.setToggleGroup(answerGroup);
                            return radioButton;
                        })

                        .collect(Collectors.toList());
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
