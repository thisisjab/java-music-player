package com.example.behnamplayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class HelloController {
    public Slider volumeSlider;
    public Slider musicSecondsSlider;
    @FXML
    private Label welcomeText;
    @FXML
    private Label musicSeconds;
    @FXML
    private TextField secondsField;
    public String musicPath;
//    public String musicPath = new File("/home/jab/Downloads/Hichkas - Ye Rooze Khoob.mp3").toURI().toString();
    // We define one media player throughout the whole application so that makes it easier to work with
    // TODO: make this player private
    public MediaPlayer mediaPlayer = new MediaPlayer(new Media(musicPath));
    // TODO: get rid of this ugly class property
    public long totalMusicSeconds = 0;

    @FXML
    protected void onPlayButtonClicked() {
        // When playing a song, we need to know from which second should music start playing
        // This second must be between 0 and song's total seconds
        long secondFrom = Long.parseLong(secondsField.getText());
        if (secondFrom > totalMusicSeconds) {
            new Alert(Alert.AlertType.WARNING, "Seconds invalid.", ButtonType.OK).showAndWait();
        } else {
            System.out.println("Playing from " + Duration.millis(secondFrom*1000));
            mediaPlayer.setStartTime(Duration.millis(secondFrom*1000));
            mediaPlayer.play();
        }
    }

    public void onStopButtonClicked(ActionEvent actionEvent) {
        mediaPlayer.stop();
    }

    public void onChooseFileClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(new Stage());
//        System.out.println("This file has been chosen: " + selectedFile.getAbsolutePath().toString());
        String extensionType = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
        if (extensionType.equals(".mp3")) {
            mediaPlayer = new MediaPlayer(new Media(selectedFile.toURI().toString()));
            Mp3File mp3File = new Mp3File(selectedFile.getAbsolutePath());
            long musicTime = mp3File.getLengthInSeconds();
            totalMusicSeconds = musicTime;
            welcomeText.setText(selectedFile.getAbsolutePath().toString());
            musicSeconds.setText(String.valueOf(musicTime) + "seconds");
        } else {
            welcomeText.setText("Error: Only mp3 files are allowed!");
        }
    }

    public void onPauseClicked(ActionEvent actionEvent) {
        mediaPlayer.pause();
    }

    public void onSliderDragged(MouseEvent mouseEvent) {
        mediaPlayer.setVolume(volumeSlider.getValue());
    }

    public void onMusicSecondsSliderDragged(MouseEvent mouseEvent) {
        mediaPlayer.stop();
        mediaPlayer.setStartTime(Duration.millis(musicSecondsSlider.getValue() * totalMusicSeconds * 1000));
        mediaPlayer.play();
    }
}