package com.example.behnamplayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class MainWindowController {
    public TextField directoryField;
    public Button chooseDirectoryButton;
    public Button shuffleButton;
    public Button repeatButton;
    public Slider musicTimeSlider;
    public Label musicTimeLabel;
    private MediaUtilities media;
    private Mp3File currentMusic;
    private int currentSongNumber = 0;
    private int musicsCount;

    public void onChooseDirectoryButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        String chosenDirectoryPath = FileUtilities.getDirectoryFromUserDialog(new Stage());
        if (!chosenDirectoryPath.equals("")) {
            ArrayList<Path> musicsList = (ArrayList<Path>) FileUtilities.getAllMusicsInDirectory(chosenDirectoryPath);
            this.musicsCount = musicsList.size();
            if (musicsList.size() == 0) {
                new Alert(Alert.AlertType.WARNING, "Directory has no songs", ButtonType.OK).showAndWait();
            } else {
                directoryField.setText(chosenDirectoryPath);
                this.media = new MediaUtilities(musicsList);
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "No directory has been chosen!", ButtonType.OK).showAndWait();
        }
    }

    public void onPreviousButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
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
    }

    public void onNextButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        this.musicTimeSlider.setValue(0);
        if (this.currentSongNumber < this.musicsCount - 1) {
            this.currentSongNumber += 1;
        } else {
            this.currentSongNumber = 0;
        }
        this.media.next();
    }

    public void onPauseButtonClicked(ActionEvent actionEvent) {
        this.media.pause();
    }

    public void onStopButtonClicked(ActionEvent actionEvent) {
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
        this.currentMusic = this.media.play(currentSongNumber, Duration.millis(musicTimeSlider.getValue() * currentMusic.getLengthInSeconds() * 1000).toSeconds());
    }
}
