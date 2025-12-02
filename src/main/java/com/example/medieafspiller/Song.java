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
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.logging.Logger;


public class Song {
    private String songName = "";
    private String artistName = "";
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
            len += (minutes < 10 ? "0" + minutes : minutes) + ":";
        } else {
            len += minutes + ":";
        }

        len += (seconds < 10 ? "0" + seconds : seconds);

        this.songLength = len;
        return len;
    }

    public String getSongLength() {
        return getLength();
    }

    public boolean isValidSong() {
        return songFile.exists() && songFile.isFile();
    }

    public void setSongFile(File songFile) {
        this.songFile = songFile;

        // Find egenskaber af mp3/wav fil

        if (!isValidSong()) return;
        setSongLength(getAudioLength(this.songFile));

        if (this.songFile.getName().toLowerCase().endsWith(".wav")) { // .wav filer har ikke altid metadata og det giver fejl
            setSongName("Unknown Title");
            setArtistName("Unknown Artist");
            return;
        }

        try {
            AudioFile audioFile = AudioFileIO.read(songFile);
            Tag tag = audioFile.getTag();

            String title = (tag != null) ? tag.getFirst(FieldKey.TITLE) : null;
            String artist = (tag != null) ? tag.getFirst(FieldKey.ARTIST) : null;

            setSongName(title != null && !title.isEmpty() ? title : "Unknown Title");
            setArtistName(artist != null && !artist.isEmpty() ? artist : "Unknown Artist");
        } catch (Exception e) {
            System.out.println("Couldn't read metadata: " + e.getMessage());

            setSongName("Unknown Title");
            setArtistName("Unknown Artist");
        }
    }

    private long getAudioLength(File file) {
        if (file.getName().toLowerCase().endsWith(".wav")) {
            return getAudioLengthFromStream(file);
        }

        try {
            AudioFile audioFile = AudioFileIO.read(file);
            return audioFile.getAudioHeader().getTrackLength();
        } catch (Exception e) {
            return getAudioLengthFromStream(file);
        }
    }

    private long getAudioLengthFromStream(File file) {
        AudioInputStream stream = null;

        try {
            stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();
            long frames = stream.getFrameLength();
            return frames / (long) format.getFrameRate();
        } catch (Exception e) {
            return 0;
        } finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException ignored) {}
        }
    }

    private void setSongLength(long length) {
        this.length = length;
    }

    public URI getSongURI() {
        return songFile.toURI();
    }

    public String getFilePath() {
        return songFile != null ? songFile.getAbsolutePath() : "";
    }
}
