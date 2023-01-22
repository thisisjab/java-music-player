package com.example.behnamplayer;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class FileUtilities {
    private FileUtilities() {
    }

    public static List<Path> getAllMusicsInDirectory(String directoryPath) {
        // This method gets a directory path and finds all the mp3 files within that folder
        ArrayList<Path> result = new ArrayList<Path>();
        File musicDirectory = new File(directoryPath);
        try (Stream<Path> stream = Files.walk(Paths.get(musicDirectory.toURI()))) {
            stream.filter(file -> file.toString().endsWith(".mp3"))
                    .forEach(result::add);
        } catch (Exception error) {
            System.out.println("We got a problem when finding files!");
            System.out.println(error.getMessage());
        }
        return result;
    }

    public static String getDirectoryFromUserDialog(Stage stage) {
        // This method opens a directory chooser and returns chosen directory path
        // returns "" if no directory is selected
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        String dirPath = "";
        if (selectedDirectory == null) {
            System.out.println("No directory selected!");
            return "";
        } else {
            dirPath = selectedDirectory.getAbsolutePath();
        }

        return dirPath;
    }
}
