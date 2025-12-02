package com.example.medieafspiller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    private Song currentSong;

    public void play(Song song) {
        if (song == null || !song.isValidSong()) return;

        if (song.equals(currentSong) && mediaPlayer != null) {
            mediaPlayer.play();
            return;
        }

        stop();

        currentSong = song;
        Media media = new Media(song.getSongURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> mediaPlayer.play());
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();
        });

        mediaPlayer.setOnError(() -> {
            System.out.println("Playback error: " + mediaPlayer.getError());
        });
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            currentSong = null;
        }
    }

    public void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(clamp(volume, 0.0, 1.0));
        }
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public double getVolume() {
        return mediaPlayer != null ? mediaPlayer.getVolume() : 0;
    }

    public Duration getCurrentTime() {
        return mediaPlayer != null ? mediaPlayer.getCurrentTime() : Duration.ZERO;
    }

    public Duration getTotalDuration() {
        return mediaPlayer != null ? mediaPlayer.getTotalDuration() : Duration.ZERO;
    }

    public boolean isPlaying() {
        return mediaPlayer != null &&
                mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
