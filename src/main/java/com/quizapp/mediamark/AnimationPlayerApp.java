package com.quizapp.mediamark;

import com.quizapp.GUI.QuizAppGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

public class AnimationPlayerApp extends Application {

    private static final String ANIMATION_FILE = "GUI/Abyss.mp4";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a Media object with the path to your MP4 animation file
        Media media = new Media(new File(ANIMATION_FILE).toURI().toString());

        // Create a MediaPlayer with the Media
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        // Create a MediaView and associate it with the MediaPlayer
        MediaView mediaView = new MediaView(mediaPlayer);

        // Create a layout for the application, e.g., BorderPane
        BorderPane root = new BorderPane();
        root.setCenter(mediaView);

        // Create a Scene with the layout
        Scene scene = new Scene(root, 800, 600);

        // Set the Scene to the Stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Animation Player");

        // Show the Stage, but hide it initially
        primaryStage.setOpacity(0);
        primaryStage.show();

        // Play the animation once the MediaPlayer is ready
        mediaPlayer.setOnReady(() -> {
            primaryStage.setOpacity(1); // Show the Stage once the animation is ready to play
            mediaPlayer.play(); // Play the animation
        });

        // After the animation finishes playing, launch QuizAppGUI
        mediaPlayer.setOnEndOfMedia(() -> {
            primaryStage.close(); // Close the animation player window
            launchQuizAppGUI();
        });
    }


    private void launchQuizAppGUI() {
        QuizAppGUI.launch(QuizAppGUI.class); // Launch QuizAppGUI
    }
}
