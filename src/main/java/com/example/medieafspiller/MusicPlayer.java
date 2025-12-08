package com.example.medieafspiller;

import javafx.beans.value.ChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;
    private Song currentSong;
    private double volume = 0.5;
    private Playlist playlistSource = null;

    private Runnable onEndOfMedia = null;
    private ChangeListener<Duration> listener = null;

    public void play(Song song, Playlist playlist) {
        if (song == null || !song.isValidSong()) return;
        this.playlistSource = playlist;

        if (currentSong != null && song.getSongURI().equals(currentSong.getSongURI()) && mediaPlayer != null) {
            mediaPlayer.play();
            return;
        }

        stop();

        currentSong = song;
        Media media = new Media(song.getSongURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volume);

        mediaPlayer.setOnReady(() -> mediaPlayer.play());
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.stop();

            if (onEndOfMedia != null) {
                onEndOfMedia.run();
            }
        });

        if (listener != null) {
            mediaPlayer.currentTimeProperty().addListener(listener);
        }

        mediaPlayer.setOnError(() -> {
            System.out.println("Playback error: " + mediaPlayer.getError());
        });
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
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
            volume = clamp(volume, 0.0, 1.0);
            this.volume = volume;
            mediaPlayer.setVolume(volume);
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

    public void addListener(ChangeListener<Duration> listener) {
        this.listener = listener;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public Playlist getPlaylistSource() {
        return playlistSource;
    }

    public Duration getTotalDuration() {
        return mediaPlayer != null ? mediaPlayer.getTotalDuration() : Duration.ZERO;
    }

    public void setCurrentTime(double t) {
        if (mediaPlayer != null && currentSong != null) {
            mediaPlayer.seek(Duration.seconds(currentSong.getRawSongLength() * t));
        }
    }

    public void setOnEndOfMedia(Runnable runnable) {
        onEndOfMedia = runnable;
    }

    public boolean isPlaying() {
        return mediaPlayer != null &&
                mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public boolean isPaused() {
        return mediaPlayer != null &&
                mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED;
    }
}
