package com.example.behnamplayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class MainWindowController {
    public TextField directoryField;
    public Button chooseDirectoryButton;
    private MediaUtilities media;

    public void onChooseDirectoryButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        String chosenDirectoryPath = FileUtilities.getDirectoryFromUserDialog(new Stage());
        if (!chosenDirectoryPath.equals("")) {
            ArrayList<Path> musicsList = (ArrayList<Path>) FileUtilities.getAllMusicsInDirectory(chosenDirectoryPath);
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
        this.media.previous();
    }

    public void onPlayButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        this.media.play(0);
    }

    public void onNextButtonClicked(ActionEvent actionEvent) throws InvalidDataException, UnsupportedTagException, IOException {
        this.media.next();
    }

    public void onPauseButtonClicked(ActionEvent actionEvent) {
        this.media.pause();
    }

    public void onStopButtonClicked(ActionEvent actionEvent) {
        this.media.stop();
    }
}
