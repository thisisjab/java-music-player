package com.example.behnamplayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
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

    public static ArrayList<Path> sortPathList(ArrayList<Path> musicsPathList, String basedOn) throws InvalidDataException, UnsupportedTagException, IOException {
        basedOn = basedOn.toLowerCase().trim();
        ArrayList<String> generatedList = new ArrayList<String>();
        if (basedOn.equals("artist")) {
            for (Path path : musicsPathList) {
                Mp3File currentSong = new Mp3File(path.toAbsolutePath().toString());
                if (!currentSong.getId3v2Tag().getArtist().equals("")) {
                    generatedList.add(currentSong.getId3v2Tag().getArtist());
                } else {
                    generatedList.add("No Artist");
                }
            }
        } else if (basedOn.equals("album")) {
            for (Path path : musicsPathList) {
                Mp3File currentSong = new Mp3File(path.toAbsolutePath().toString());
                if (!currentSong.getId3v2Tag().getAlbum().equals("")) {
                    generatedList.add(currentSong.getId3v2Tag().getAlbum());
                } else {
                    generatedList.add("No Album");
                }
            }
        } else {
            for (Path path : musicsPathList) {
                Mp3File currentSong = new Mp3File(path.toAbsolutePath().toString());
                if (!currentSong.getId3v2Tag().getTitle().equals("")) {
                    generatedList.add(currentSong.getId3v2Tag().getTitle());
                } else {
                    generatedList.add("No Title");
                }
            }
        }

        for (int i = 0; i < generatedList.size(); i++) {
            for (int j = i; j < generatedList.size(); j++) {
                if (generatedList.get(i).compareToIgnoreCase(generatedList.get(j)) > 0) {
                    String tempValue = generatedList.get(i);
                    Path tempPath = musicsPathList.get(i);
                    generatedList.set(i, generatedList.get(j));
                    musicsPathList.set(i, musicsPathList.get(j));
                    generatedList.set(j, tempValue);
                    musicsPathList.set(j, tempPath);
                }
            }
        }

        return musicsPathList;
    }
}
