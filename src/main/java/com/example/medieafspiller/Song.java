package com.example.medieafspiller;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;


public class Song {
    private String songName = "Unknown";
    private String artistName = "Unknown";
    private long length = 0;
    private String songLength = "0:00";
    private File songFile;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getLength() {
        long hours = length / 3600;
        long minutes = length % 3600 / 60;
        long seconds = length % 3600 % 60;

        String len = "";

        if (hours > 0) {
            len = (hours < 10 ? "0" + hours : hours) + ":";
        }

        len += (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);

        this.songLength = len;
        return this.songLength;
    }

    public boolean isValidSong() {
        return songFile.exists() && songFile.isFile();
    }

    public void setSongFile(File songFile) {
        this.songFile = songFile;

        // Find egenskaber af mp3/wav fil

        if (isValidSong()) {
            try {
                AudioFile audioFile = AudioFileIO.read(songFile);
                Tag tag = audioFile.getTag();

                if (tag != null) {
                    String title = tag.getFirst(FieldKey.TITLE);
                    String artist = tag.getFirst(FieldKey.ARTIST);

                    setSongName(title != null && !title.isEmpty() ? title : "Unknown Title");
                    setArtistName(artist != null && !artist.isEmpty() ? artist : "Unknown Artist");
                } else {
                    setSongName("Unknown Title");
                    setArtistName("Unknown Artist");
                }

                length = audioFile.getAudioHeader().getTrackLength();
                setSongLength(length);
            } catch (CannotReadException | TagException | InvalidAudioFrameException | ReadOnlyFileException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setSongLength(long length) {
        this.length = length;
    }

    public java.net.URI getSongURI() {
        return songFile.toURI();
    }
}
