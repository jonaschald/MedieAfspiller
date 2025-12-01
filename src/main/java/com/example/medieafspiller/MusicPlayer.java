package com.example.medieafspiller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    private Song currentSong;

    private boolean playing;
    private int time = 0;
    private int volume = 1;

    public void play(Song song) {
        if (!song.isValidSong()) return;
        currentSong = song;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        Media media = new Media(currentSong.getSongURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        playing = true;
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            playing = false;
        }
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }
}
