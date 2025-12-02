package com.example.medieafspiller;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String name;
    private List<Song> songListe = new ArrayList<Song>();
    private int numberOfSongsOnPlaylist = 0;
    private long lengthOfPlaylist = 0;
    private String playlistLength = "0:00";

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Song> getSongListe() {
        return songListe;
    }

    public void addSong(Song song) {
       songListe.add(song);
    }

    public void removeSong(Song song) {
        this.songListe.remove(song);
    }

    public String getLengthOfPlaylist() {
        long hours = lengthOfPlaylist / 3600;
        long minutes = lengthOfPlaylist % 3600 / 60;
        long seconds = lengthOfPlaylist % 3600 % 60;

        String len = "";

        if (hours > 0) {
            len = (hours < 10 ? "0" + hours : hours) + ":";
        }

        len += (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
        playlistLength = len;

        return playlistLength;
    }

    public void setLengthOfPlaylist(long lengthOfPlaylist) {
        this.lengthOfPlaylist = lengthOfPlaylist;
    }

    public int getNumberOfSongsOnPlaylist() {
        return numberOfSongsOnPlaylist;
    }

    public void setNumberOfSongsOnPlaylist(int numberOfSongsOnPlaylist) {
        this.numberOfSongsOnPlaylist = numberOfSongsOnPlaylist;
    }

    public List<Song> getSongsOnPlaylist(){return songListe;}
}
