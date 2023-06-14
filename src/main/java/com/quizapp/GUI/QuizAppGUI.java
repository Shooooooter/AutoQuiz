package com.quizapp.GUI;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import com.quizapp.service.QuizLoader;
import com.quizapp.utils.QuizDifficulty;
import com.quizapp.utils.QuizType;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.MapChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;


import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.quizapp.service.QuizGeneratorService.generateQuiz;

public class QuizAppGUI extends Application {

    private List<Question> questions;
    private Iterator<Question> questionIterator;
    private TableView<Quiz> quizTableView;

    private static final String ANIMATION_FILE = "src/main/resources/GUI/Abyss.mp4";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Play the animation
        playAnimation();

        // Create the UI components
        quizTableView = new TableView<>();
        TableColumn<Quiz, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Quiz, String> topicColumn = new TableColumn<>("Topic");
        Label numQuestionsLabel = new Label("Number of Questions:");
        ComboBox<Integer> numQuestionsComboBox = new ComboBox<>();
        Label quizTypeLabel = new Label("Quiz Type:");
        ComboBox<QuizType> quizTypeComboBox = new ComboBox<>();
        Label topicLabel = new Label("Topic:");
        TextField topicTextField = new TextField();
        Label difficultyLabel = new Label("Difficulty:");
        ComboBox<QuizDifficulty> difficultyComboBox = new ComboBox<>();
        Button generateButton = new Button("Generate Quiz");
        Button refreshButton = new Button("Refresh");
        QuizLoader quizLoader = new QuizLoader();
        List<Quiz> quizzes = quizLoader.loadQuizzes();
        quizTableView.getItems().addAll(quizzes);

        // Configure the UI components
        quizTableView.setPrefWidth(200);
        quizTableView.setPrefHeight(400);
        numQuestionsComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        numQuestionsComboBox.setValue(10);
        quizTypeComboBox.getItems().addAll(QuizType.MULTIPLE_CHOICE, QuizType.TRUE_FALSE);
        quizTypeComboBox.setValue(QuizType.MULTIPLE_CHOICE);
        difficultyComboBox.getItems().addAll(QuizDifficulty.EASY, QuizDifficulty.MEDIUM, QuizDifficulty.HARD);
        difficultyComboBox.setValue(QuizDifficulty.MEDIUM);

        // Create the layout
        BorderPane root = new BorderPane();

        // Create the "Take Quiz" tab
        VBox takeQuizLayout = new VBox(10);
        takeQuizLayout.setPadding(new Insets(10));
        takeQuizLayout.getStyleClass().add("container");
        takeQuizLayout.getChildren().add(quizTableView);
        root.setCenter(takeQuizLayout);

        // Create the "Make Quiz" tab
        VBox makeQuizLayout = new VBox(10);
        makeQuizLayout.setPadding(new Insets(10));
        makeQuizLayout.getStyleClass().add("container");
        makeQuizLayout.getChildren().addAll(numQuestionsLabel, numQuestionsComboBox, quizTypeLabel,
                quizTypeComboBox, topicLabel, topicTextField, difficultyLabel, difficultyComboBox, generateButton);
        root.setRight(makeQuizLayout);

        // Set the selected tab to "Take Quiz" by default

        // Create the scene and set it on the stage
        Scene scene = new Scene(root, 600, 400);
        String cssPath = "GUI/styles.css";
        scene.getStylesheets().add(cssPath);

        primaryStage.setTitle("Abyssal Quiz App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Define cell value factories for the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<>("topic"));

        // Add the columns to the table view
        quizTableView.getColumns().addAll(idColumn, topicColumn);

        // Handle generateButton click event
        generateButton.setOnAction(event -> {
            int numQuestions = numQuestionsComboBox.getValue();
            QuizType quizType = quizTypeComboBox.getValue();
            String topic = topicTextField.getText();
            QuizDifficulty difficulty = difficultyComboBox.getValue();

            if (topic.isEmpty()) {
                showErrorMessage("Please enter a topic.");
            } else {
                generateQuiz(numQuestions, quizType, topic, difficulty);
            }
        });

        // Handle quizTableView selection event
        quizTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                questions = newValue.getQuestions();
            }
        });

        // Handle refreshButton click event
        refreshButton.setOnAction(event -> refreshQuizList());

        // Add refreshButton to takeQuizLayout
        takeQuizLayout.getChildren().add(refreshButton);

        // Create the "Launch Quiz" button
        Button launchButton = new Button("Launch Quiz");
        launchButton.setOnAction(event -> launchSelectedQuiz());

        // Add the "Launch Quiz" button to takeQuizLayout
        takeQuizLayout.getChildren().add(launchButton);

        // Apply fade-in animation
        applyFadeInAnimation(root);
    }

    private void launchSelectedQuiz() {
        Quiz selectedQuiz = quizTableView.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            List<Question> selectedQuestions = selectedQuiz.getQuestions();
            startQuiz(selectedQuestions);
        } else {
            showErrorMessage("Please select a quiz.");
        }
    }

    private void startQuiz(List<Question> questions) {
        VBox quizLayout = new VBox(10);
        quizLayout.getStyleClass().add("quiz-popup");
        quizLayout.getChildren().clear();
        questionIterator = questions.iterator();

        showNextQuestion(quizLayout);

        Stage quizStage = new Stage();
        quizStage.setTitle("Deep Quiz");
        Scene scene = new Scene(quizLayout, 600, 400);
        String cssPath = "GUI/styles.css";
        scene.getStylesheets().add(cssPath);
        quizStage.setScene(scene);
        quizStage.show();
    }


    private void showNextQuestion(VBox quizLayout) {
        if (questionIterator != null && questionIterator.hasNext()) {
            Question question = questionIterator.next();

            quizLayout.getChildren().clear();

            Label questionLabel = new Label(question.getPrompt());
            questionLabel.getStyleClass().add("question-label"); // Add style class for question label
            ToggleGroup answerGroup = new ToggleGroup();

            if (question.getType() == QuizType.MULTIPLE_CHOICE) {
                List<RadioButton> answerRadioButtons = Arrays.stream(question.getOptions())
                        .map(option -> {
                            RadioButton radioButton = new RadioButton(option);
                            radioButton.setToggleGroup(answerGroup);
                            return radioButton;
                        })
                        .toList();
                quizLayout.getChildren().add(questionLabel);
                quizLayout.getChildren().addAll(answerRadioButtons);
            } else if (question.getType() == QuizType.TRUE_FALSE) {
                RadioButton trueRadioButton = new RadioButton("True");
                RadioButton falseRadioButton = new RadioButton("False");
                trueRadioButton.setToggleGroup(answerGroup);
                falseRadioButton.setToggleGroup(answerGroup);
                quizLayout.getChildren().add(questionLabel);
                quizLayout.getChildren().addAll(trueRadioButton, falseRadioButton);
            }

            Button submitButton = new Button("Submit Question");
            submitButton.getStyleClass().add("submit-button"); // Add style class for submit button
            submitButton.setOnAction(event -> processQuestion(question, answerGroup, quizLayout));
            quizLayout.getChildren().add(submitButton);
        } else {
            processQuiz(questions);
        }
    }


    private void processQuestion(Question question, ToggleGroup answerGroup, VBox quizLayout) {
        RadioButton selectedRadioButton = (RadioButton) answerGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            String selectedAnswer = selectedRadioButton.getText();
            question.setAnswer(selectedAnswer);
            showNextQuestion(quizLayout);
        } else {
            showErrorMessage("Please select an answer.");
        }
    }

    private void playAnimation() {
        // Create a Media object with the path to your MP4 animation file
        Media media = new Media(new File(ANIMATION_FILE).toURI().toString());

        // Create a MediaPlayer with the Media
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        // Create a MediaView and associate it with the MediaPlayer
        MediaView mediaView = new MediaView(mediaPlayer);

        // Define the desired maximum dimensions for your animation
        int maxWidth = 1280;
        int maxHeight = 720;

        // Bind the MediaView's fitWidth and fitHeight properties to the maximum dimensions
        mediaView.fitWidthProperty().bind(Bindings.min(media.widthProperty(), maxWidth));
        mediaView.fitHeightProperty().bind(Bindings.min(media.heightProperty(), maxHeight));

        // Create a layout for the application, e.g., StackPane
        StackPane root = new StackPane();
        root.getChildren().add(mediaView);

        // Create a Scene with the layout using the maximum dimensions
        Scene scene = new Scene(root, maxWidth, maxHeight);

        // Set the Scene to a temporary Stage
        Stage tempStage = new Stage();
        tempStage.initStyle(StageStyle.UNDECORATED);
        tempStage.setScene(scene);
        tempStage.setAlwaysOnTop(true);
        tempStage.show();

        // Play the animation
        mediaPlayer.play();

        // Close the temporary Stage after the animation finishes
        mediaPlayer.setOnEndOfMedia(tempStage::close);
    }

    private void processQuiz(List<Question> questions) {
        int numCorrectAnswers = (int) questions.stream()
                .filter(Question::isCorrect)
                .count();

        showQuizResult(numCorrectAnswers, questions.size());
    }

    private void showQuizResult(int numCorrectAnswers, int totalQuestions) {
        String resultMessage = String.format("Quiz Result: %d/%d", numCorrectAnswers, totalQuestions);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Result");
        alert.setHeaderText(resultMessage);
        alert.showAndWait();
    }

    private void refreshQuizList() {
        QuizLoader quizLoader = new QuizLoader();
        List<Quiz> quizzes = quizLoader.loadQuizzes();
        quizTableView.getItems().clear();
        quizTableView.getItems().addAll(quizzes);
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void applyFadeInAnimation(BorderPane root) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), root);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }
}

