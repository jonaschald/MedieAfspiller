package com.example.medieafspiller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationHelper {

    private static final String playlistData = "playlister.dat";
    private static final String songData = "sange.dat";

    public static void savePlaylists(List<Playlist> playlists) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(playlistData))) {
            oos.writeObject(playlists);
        } catch (IOException e) {
            System.out.println("Error saving playlists: " + e.getMessage());
        }
    }

    public static void saveSongs(List<Song> songs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(songData))) {
            oos.writeObject(songs);
        } catch (IOException e) {
            System.out.println("Error saving songs: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Playlist> loadPlaylists() throws FileNotFoundException {
        File file = new File(playlistData);
        if (!file.exists()) {
            throw new FileNotFoundException(playlistData + " not found");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(playlistData))) {
            return (ArrayList<Playlist>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading playlists: " + e.getMessage());
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Song> loadSongs() throws FileNotFoundException {
        File file = new File(songData);
        if (!file.exists()) {
            throw new FileNotFoundException(songData + " not found");
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(songData))) {
            return (ArrayList<Song>) ois.readObject();
        } catch (IOException |ClassNotFoundException e) {
            System.out.println("Error loading songs: " + e.getMessage());
        }

        return null;
    }
}
