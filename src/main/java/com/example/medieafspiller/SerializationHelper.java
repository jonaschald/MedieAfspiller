package com.example.medieafspiller;

import java.io.*;
import java.util.ArrayList;

record SongEntry(File file, String name, String artist) implements Serializable { // Så data typerne ikke er så lange
    SongEntry(Song s) {
        this(new File(s.getFilePath()), s.getSongName(), s.getArtistName());
    }
}
record PlaylistEntry(String name, ArrayList<SongEntry> files) implements Serializable {}

public class SerializationHelper {

    private static final String playlistData = "playlister.dat";
    private static final String songData = "sange.dat";

    public static void savePlaylists(ArrayList<PlaylistEntry> playlists) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(playlistData))) {
            oos.writeObject(playlists);
        } catch (IOException e) {
            System.out.println("Error saving playlists: " + e.getMessage());
        }
    }

    public static void saveSongs(ArrayList<SongEntry> songs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(songData))) {
            oos.writeObject(songs);
        } catch (IOException e) {
            System.out.println("Error saving songs: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<PlaylistEntry> loadPlaylists() throws FileNotFoundException {
        File file = new File(playlistData);
        if (!file.exists()) {
            throw new FileNotFoundException(playlistData + " not found");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(playlistData))) {
            return (ArrayList<PlaylistEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading playlists: " + e.getMessage());
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<SongEntry> loadSongs() throws FileNotFoundException {
        File file = new File(songData);
        if (!file.exists()) {
            throw new FileNotFoundException(songData + " not found");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(songData))) {
            return (ArrayList<SongEntry>) ois.readObject();
        } catch (IOException |ClassNotFoundException e) {
            System.out.println("Error loading songs: " + e.getMessage());
        }

        return null;
    }
}
