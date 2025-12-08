package com.example.medieafspiller;

import java.util.ArrayList;

public class Playlist {
    private String name;
    private ArrayList<Song> songs = new ArrayList<>();
    private int numberOfSongsOnPlaylist = 0;
    private long lengthOfPlaylist = 0;
    private String playlistLength = "0:00";

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        this.songs.add(song);
        this.numberOfSongsOnPlaylist++;
        this.setLengthOfPlaylist(lengthOfPlaylist + song.getRawSongLength());
    }

    public void removeSong(Song song) {
        this.songs.remove(song);
        this.numberOfSongsOnPlaylist--;
        this.setLengthOfPlaylist(lengthOfPlaylist - song.getRawSongLength());
    }

    public String getLengthOfPlaylist() { // Lav tid i sekunder om til HH:MM:SS format
        long hours = lengthOfPlaylist / 3600;
        long minutes = lengthOfPlaylist % 3600 / 60;
        long seconds = lengthOfPlaylist % 3600 % 60;

        String len = "";

        if (hours > 0) {
            len = (hours < 10 ? "0" + hours : hours) + ":";
            len += (minutes < 10 ? "0" + minutes : minutes) + ":";
        } else {
            len += minutes + ":";
        }

        len += (seconds < 10 ? "0" + seconds : seconds);

        this.playlistLength = len;
        return len;
    }

    public void setLengthOfPlaylist(long lengthOfPlaylist) {
        this.lengthOfPlaylist = lengthOfPlaylist;
        this.playlistLength = getLengthOfPlaylist();
    }

    public int getNumberOfSongsOnPlaylist() {
        return numberOfSongsOnPlaylist;
    }

    public void setNumberOfSongsOnPlaylist(int numberOfSongsOnPlaylist) {
        this.numberOfSongsOnPlaylist = numberOfSongsOnPlaylist;
    }
}
