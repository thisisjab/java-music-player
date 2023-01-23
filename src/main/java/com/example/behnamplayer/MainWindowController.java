package com.example.behnamplayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Timer;

public class MainWindowController {
    public TextField directoryField;
    public Button chooseDirectoryButton;
    public Button shuffleButton;
    public Button repeatButton;
    public Slider musicTimeSlider;
    public Label musicTimeLabel;
    public TextField searchField;
    private MediaUtilities media;
    private Mp3File currentMusic;
    private int currentSongNumber = 0;
    private int musicsCount;
    private Timer musicTimer;
    private long currentSecond = 0;
    private ArrayList<Path> musicsList;
    private KeyFrame updater = new KeyFrame(Duration.seconds(1.0), event -> notifySlider());
    private Timeline appTimer;

    public void onChooseDirectoryButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        String chosenDirectoryPath = FileUtilities.getDirectoryFromUserDialog(new Stage());
        if (!chosenDirectoryPath.equals("")) {
            this.musicsList = (ArrayList<Path>) FileUtilities.getAllMusicsInDirectory(chosenDirectoryPath);
            this.musicsList = FileUtilities.sortPathList(this.musicsList, "track");
            this.musicsCount = this.musicsList.size();
            if (this.musicsList.size() == 0) {
                new Alert(Alert.AlertType.WARNING, "Directory has no songs", ButtonType.OK).showAndWait();
            } else {
                directoryField.setText(chosenDirectoryPath);
                this.media = new MediaUtilities(this.musicsList);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "No directory has been chosen!", ButtonType.OK).showAndWait();
        }
    }

    public void onPreviousButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        musicTimeLabel.setText("0:0");
        currentSecond = 0;
        appTimer = new Timeline(updater);
        appTimer.play();
        this.musicTimeSlider.setValue(0);
        if (currentSongNumber > 0) {
            this.currentSongNumber -= 1;
        } else {
            this.currentSongNumber = musicsCount - 1;
        }
        this.media.previous();
    }

    public void onPlayButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        this.currentMusic = this.media.play(currentSongNumber);
        this.musicTimeLabel.setText(secondsToTimeString(currentSecond, currentMusic.getLengthInSeconds()));
        this.musicTimer = new Timer();
        // TODO: fix currentSecond limit
        musicTimeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                musicTimeLabel.setText(secondsToTimeString(Long.parseLong(newValue.toString()), currentMusic.getLengthInSeconds())));
       appTimer = new Timeline(updater);
       appTimer.setCycleCount(Timeline.INDEFINITE);
       appTimer.play();
    }

    private void notifySlider() {
        currentSecond += 1;
        this.musicTimeLabel.setText(secondsToTimeString(currentSecond, currentMusic.getLengthInSeconds()));
    }

    public void onNextButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        currentSecond = 0;
        musicTimeLabel.setText("0:0");
        appTimer = new Timeline(updater);
        appTimer.play();
        this.musicTimeSlider.setValue(0);
        if (this.currentSongNumber < this.musicsCount - 1) {
            this.currentSongNumber += 1;
        } else {
            this.currentSongNumber = 0;
        }
        this.media.next();
    }

    public void onPauseButtonClicked(ActionEvent actionEvent) {
        appTimer.pause();
        this.musicTimer.cancel();
        this.media.pause();
    }

    public void onStopButtonClicked(ActionEvent actionEvent) {
        musicTimeLabel.setText("0:0");
        currentSecond = 0;
        appTimer.stop();
        this.musicTimer.cancel();
        this.musicTimeSlider.setValue(0);
        this.media.stop();
    }

    public void onShuffleButtonClicked(ActionEvent actionEvent) {
        shuffleButton.setText("Shuffle: " + media.toggleShuffle());
    }

    public void onRepeatButtonClicked(ActionEvent actionEvent) {
        repeatButton.setText("Repeat: " + media.toggleRepeat());
    }

    public void onMusicTimeSliderDragged(MouseEvent mouseDragEvent) throws InvalidDataException, UnsupportedTagException, IOException {
//        System.out.println("Slider says: " + musicTimeSlider.getValue());
//        System.out.println("Music total time is: " + currentMusic.getLengthInSeconds());
//        System.out.println("Now I am going to play from " + Duration.millis(musicTimeSlider.getValue() * currentMusic.getLengthInSeconds() * 1000).toSeconds());
//        this.currentMusic = this.media.play(currentSongNumber, );
    }

    private String secondsToTimeString(long seconds, long totalSeconds) {
        return seconds / 60 + ":" + seconds % 60 + " | " + totalSeconds / 60 + ":" + totalSeconds % 60;
    }

    public void onFindButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        String itemToSearch = searchField.getText();
        System.out.println("Searching for " + itemToSearch);
        System.out.println("Our songs are " + this.musicsList.toString());
        ArrayList<Path> foundSongs = FileUtilities.search(this.musicsList, itemToSearch);
        System.out.println("I found songs below: ");
        System.out.println(foundSongs.toString());
    }
}
