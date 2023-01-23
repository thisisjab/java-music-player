package com.example.behnamplayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class MediaUtilities {
    private final ArrayList<Path> musicsList;
    private MediaPlayer mediaPlayer;
    private int currentSongNumber = 0;
    private double whichSecondPaused;

    public MediaUtilities(ArrayList<Path> musicsList) {
        this.musicsList = musicsList;
    }

    public Mp3File play(int whichMusic, double fromSecond) throws InvalidDataException, UnsupportedTagException, IOException {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        currentSongNumber = whichMusic;
        Mp3File currentSong = new Mp3File(musicsList.get(currentSongNumber).toAbsolutePath().toString());
        if (fromSecond < 0 || fromSecond > currentSong.getLengthInSeconds()) {
            throw new InvalidDataException();
        } else {
            String songPath = new File(musicsList.get(currentSongNumber).toString()).toURI().toString();
            System.out.println("Playing " + songPath);
            this.mediaPlayer = new MediaPlayer(new Media(songPath));
            this.mediaPlayer.setStartTime(new Duration(fromSecond * 1000));
            this.mediaPlayer.play();
        }
        return currentSong;
    }

    public Mp3File play(int whichMusic) throws InvalidDataException, UnsupportedTagException, IOException {
        if (whichSecondPaused > 0) {
            System.out.println("Playing from second " + whichSecondPaused);
            return play(whichMusic, whichSecondPaused);
        }
        return play(whichMusic, 0);
    }

    public void stop() {
        this.mediaPlayer.stop();
    }

    public void pause() {
        this.whichSecondPaused = this.mediaPlayer.getCurrentTime().toSeconds();
        this.mediaPlayer.pause();
    }

    public void setVolume(double percentage) {
        this.mediaPlayer.setVolume(percentage / 100);
    }

    public Mp3File next() throws InvalidDataException, UnsupportedTagException, IOException {
        if (currentSongNumber < musicsList.size() - 1) {
            return play(currentSongNumber + 1);
        } else {
            return play(0);
        }
    }

    public Mp3File previous() throws InvalidDataException, UnsupportedTagException, IOException {
        if (currentSongNumber > 0) {
            return play(currentSongNumber - 1);
        } else {
            return play(musicsList.size() - 1);
        }
    }
}
