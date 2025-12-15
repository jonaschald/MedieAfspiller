package com.example.medieafspiller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

record SongData(ArrayList<Song> validSongs, ArrayList<String> invalidSongs) {}

public class myTunesController {

    @FXML
    private ImageView back;

    @FXML
    private TableColumn<Playlist, String> lengthOfPlaylist;

    @FXML
    private TableColumn<Playlist, String> nameOfPlaylist;

    @FXML
    private ImageView next;

    @FXML
    private Label nowPlaying;

    @FXML
    private TableColumn<Playlist, String> numberOfSongsOnPlaylist;

    @FXML
    private ImageView playPause;

    @FXML
    private TableView<Playlist> playlister;

    @FXML
    private TableColumn<Song, String> sOPArtist;

    @FXML
    private TableColumn<Song, String> sOPLength;

    @FXML
    private TableColumn<Song, String> sOPName;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<Song, String> songArtist;

    @FXML
    private TableColumn<Song, String> songLength;

    @FXML
    private TableView<Song> songListe = new TableView<>();

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TableColumn<Song, String> songName;

    @FXML
    private TableView<Song> songsOnPlaylist;

    @FXML
    private Slider volume;

    private static final ObservableList<Song> songData = FXCollections.observableArrayList();
    private static final ObservableList<Playlist> playlistData = FXCollections.observableArrayList();
    private final ObservableList<Song> sOPData = FXCollections.observableArrayList();

    private final MusicPlayer musicPlayer = new MusicPlayer();
    private int index = 0;
    private boolean dragging = false;

    // For at forklare hvorfor nogle filer ikke virker
    private final String nySangFejl = String.format(
            "Sangen er ikke valid. Det kan være fordi:%n" +
                    "*%4sFilen kan ikke findes%n" +
                    "*%4sFilen kan ikke læses%n" +
                    "*%4sFilen er ikke et understøttet medie%n" +
                    "*%4sFilen er en .wav som ikke har:%n" +
                    "%4sa.%2sEn sample størrelse på 16 bit%n" +
                    "%4sb.%2sEn sample rate på 44.1 kHz eller 48 kHz",
            "", "", "", "", "", "", "", ""
    );

    public void initialize() {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);

        songName.setCellValueFactory(new PropertyValueFactory<Song, String>("songName"));
        songArtist.setCellValueFactory(new PropertyValueFactory<Song, String>("artistName"));
        songLength.setCellValueFactory(new PropertyValueFactory<Song, String>("songLength"));

        nameOfPlaylist.setCellValueFactory(new PropertyValueFactory<Playlist, String>("name"));
        numberOfSongsOnPlaylist.setCellValueFactory(new PropertyValueFactory<Playlist, String>("numberOfSongsOnPlaylist"));
        lengthOfPlaylist.setCellValueFactory(new PropertyValueFactory<Playlist, String>("lengthOfPlaylist"));

        sOPName.setCellValueFactory(new PropertyValueFactory<Song, String>("songName"));
        sOPArtist.setCellValueFactory(new PropertyValueFactory<Song, String>("artistName"));
        sOPLength.setCellValueFactory(new PropertyValueFactory<Song, String>("songLength"));

        playlister.setItems(playlistData);
        songListe.setItems(songData);
        songsOnPlaylist.setItems(sOPData);

        songName.setSortType(TableColumn.SortType.ASCENDING);
        songArtist.setSortType(TableColumn.SortType.ASCENDING);
        songListe.getSortOrder().add(songName);
        songListe.getSortOrder().add(songArtist);

        songListe.sort();

        nameOfPlaylist.setSortType(TableColumn.SortType.ASCENDING);
        numberOfSongsOnPlaylist.setSortType(TableColumn.SortType.ASCENDING);
        playlister.getSortOrder().add(nameOfPlaylist);
        playlister.getSortOrder().add(numberOfSongsOnPlaylist);

        playlister.sort();

        songsOnPlaylist.setItems(sOPData);

        songsOnPlaylist.setRowFactory(tview -> {
            TableRow<Song> row = new TableRow<>();

            // Brugeren begynder at trække på en sang
            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    int index = row.getIndex();

                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(Integer.toString(index));
                    db.setContent(content);

                    event.consume();
                }
            });

            // Når en række bliver holdt over af et drag
            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();

                if (db != null && db.hasString()) {
                    int draggedIndex = Integer.parseInt(db.getString());
                    int thisIndex = row.getIndex();

                    if (draggedIndex != thisIndex) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }

                event.consume();
            });

            // Når sangen bliver droppet på en række
            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                Playlist playlist = playlister.getSelectionModel().getSelectedItem();

                if (db != null && db.hasString()) {
                    int draggedIndex = Integer.parseInt(db.getString());
                    Song draggedSong = sOPData.get(draggedIndex);

                    int dropIndex;

                    if (row.isEmpty()) {
                        dropIndex = sOPData.size();
                    } else {
                        dropIndex = row.getIndex();
                    }

                    sOPData.remove(draggedIndex);
                    if (dropIndex > draggedIndex) {
                        dropIndex--;
                    }

                    sOPData.add(dropIndex, draggedSong);

                    playlist.getSongs().remove(draggedSong);
                    playlist.getSongs().add(dropIndex, draggedSong);

                    if (draggedSong == musicPlayer.getCurrentSong() && musicPlayer.getPlaylistSource().equals(playlist)) {
                        index = dropIndex;
                    } else if (draggedSong != musicPlayer.getCurrentSong()) {
                        index = sOPData.indexOf(musicPlayer.getCurrentSong());
                    }

                    event.setDropCompleted(true);
                    songsOnPlaylist.getSelectionModel().select(dropIndex);
                }

                event.consume();
            });

            return row;
        });

        musicPlayer.setOnEndOfMedia(this::playNextSong);
        volume.setOnMouseDragged(event -> {
            double vol = volume.getValue() / 100;
            vol = Math.min(vol, 1.0);

            musicPlayer.setVolume(vol);
            event.consume();
        });

        progressBar.setOnMouseClicked(this::updateAndResume);
        progressBar.setOnMouseDragged(this::updateProgress);
        progressBar.setOnMouseReleased(this::updateAndResume);

        musicPlayer.addListener((obs, oldTime, newTime) -> {
            if (dragging) return;
            double prog = newTime.toSeconds() / musicPlayer.getTotalDuration().toSeconds();

            progressBar.setProgress(prog);
        });

        // Gem og indlæs data
        try {
            ArrayList<SongEntry> loadedSongs = SerializationHelper.loadSongs();

            SongData dat = getSongs(loadedSongs);
            songData.setAll(dat.validSongs());

            if (!dat.invalidSongs().isEmpty()) {
                StringBuilder msg = new StringBuilder("Disse sange kunne ikke blive fundet eller læst:\n");

                for (String s : dat.invalidSongs()) {
                    msg.append(String.format("%n%6s", s));
                }

                errorWindow(msg.toString());
            }
        } catch (FileNotFoundException ignored) {} // Filen med gemte sange er enten slettet eller ikke blevet skabt endnu

        try {
            ArrayList<PlaylistEntry> loadedPlaylists = SerializationHelper.loadPlaylists();

            ArrayList<Playlist> dat = getPlaylists(loadedPlaylists);
            playlistData.setAll(dat);
        } catch (FileNotFoundException ignored) {}
    }

    private SongData getSongs(ArrayList<SongEntry> loadedSongs) {
        ArrayList<Song> dat = new ArrayList<>();
        ArrayList<String> invalidSongs = new ArrayList<>();

        if (loadedSongs != null && !loadedSongs.isEmpty()) {
            for (SongEntry entry : loadedSongs) {
                Song s = new Song();
                s.setSongFile(entry.file());

                if (s.isValidSong()) {
                    s.setSongName(entry.name());
                    s.setArtistName(entry.artist());
                    dat.add(s);
                } else {
                    invalidSongs.add(s.getFilePath());
                }
            }
        }
        return new SongData(dat, invalidSongs);
    }

    private ArrayList<Playlist> getPlaylists(ArrayList<PlaylistEntry> loadedPlaylists) {
        ArrayList<Playlist> dat = new ArrayList<>();

        if (loadedPlaylists != null && !loadedPlaylists.isEmpty()) {
            for (PlaylistEntry entry : loadedPlaylists) {
                Playlist p = new Playlist();
                p.setName(entry.name());

                if (!entry.files().isEmpty()) {
                    for (SongEntry songEntry : entry.files()) {
                        songData.stream()
                                .filter(song -> song.getFilePath().equals(songEntry.file().getAbsolutePath()))
                                .forEach(p::addSong);
                    }
                }

                dat.add(p);
            }
        }

        return dat;
    }

    private static void saveSongs() {
        ArrayList<SongEntry> files = new ArrayList<>();

        for (Song song : songData) {
            files.add(new SongEntry(song));
        }

        SerializationHelper.saveSongs(files);
    }

    private static void savePlaylists() {
        ArrayList<PlaylistEntry> playlistSave = new ArrayList<>();

        for (Playlist p : playlistData) {
            ArrayList<SongEntry> files = new ArrayList<>();

            for (Song song : p.getSongs()) {
                files.add(new SongEntry(song));
            }

            playlistSave.add(new PlaylistEntry(p.getName(), files));
        }

        SerializationHelper.savePlaylists(playlistSave);
    }

    public static void saveData() {
        saveSongs();
        savePlaylists();
    }

    private void updateProgress(MouseEvent event) {
        dragging = true;
        double mouseX = event.getX();
        double widthX = progressBar.getWidth();

        double progress = Math.max(0.0, Math.min(1, mouseX / widthX));
        progressBar.setProgress(progress);
        event.consume();
    }

    private void updateAndResume(MouseEvent event) {
        updateProgress(event);
        musicPlayer.setCurrentTime(progressBar.getProgress());
        dragging = false;

        if (!event.isConsumed())
            event.consume();
    }

    private void playNextSong() {
        List<Song> dat = (sOPData != musicPlayer.getPlaylistSource().getSongs())
                ? musicPlayer.getPlaylistSource().getSongs()
                : sOPData;

        if (dat.isEmpty() || dat.size() == 1) return;
        if (dat.size() - 1 == index) index = -1;

        index++;

        Song s = dat.get(index);

        if (!s.isValidSong()) {
            errorWindow(String.format(nySangFejl + "%n*%4sFilen er blevet slettet", ""));
            removeSong(s);

            index--;
            if (index < 0) index = dat.size() - 1;

            playNextSong();
            return;
        }

        selectSongInUI(s);
        playSong(musicPlayer, s, musicPlayer.getPlaylistSource());
    }

    private void playPreviousSong() {
        List<Song> dat = (sOPData != musicPlayer.getPlaylistSource().getSongs())
                ? musicPlayer.getPlaylistSource().getSongs()
                : sOPData;

        if (dat.size() <= 1) return;

        Song current = musicPlayer.getCurrentSong();
        if (current != null) {
            int idx = dat.indexOf(current);
            if (idx != -1) index = idx;
        }

        if (index < 0) index = dat.size() - 1;
        index--;

        Song s = dat.get(index);

        if (!s.isValidSong()) {
            errorWindow(String.format(nySangFejl + "%n*%4sFilen er blevet slettet", ""));
            removeSong(s);

            if (dat.size() <= 1) return;

            index--;
            if (index < 0) index = dat.size() - 1;

            playPreviousSong();
            return;
        }

        selectSongInUI(s);
        playSong(musicPlayer, s, musicPlayer.getPlaylistSource());
    }

    private void selectSongInUI(Song s) {
        if (songsOnPlaylist.getItems() == sOPData
                || songsOnPlaylist.getItems() == musicPlayer.getPlaylistSource().getSongs()) {

            Platform.runLater(() -> {
                songsOnPlaylist.getSelectionModel().clearSelection();
                songsOnPlaylist.getSelectionModel().select(s);
                songsOnPlaylist.scrollTo(s);
            });
        }
    }

    private void playSong(MusicPlayer mp, Song song, Playlist playlist) {
        if (!song.isValidSong()) {
            errorWindow(String.format(nySangFejl + "%n*%4sFilen er blevet slettet", ""));
            removeSong(song);
            return;
        }

        if (mp.isPlaying() && mp.getCurrentSong() == song && mp.getPlaylistSource() == playlist) {
            mp.pause();
            nowPlaying.setText("");
        } else {
            mp.play(song, playlist);
            nowPlaying.setText(mp.getCurrentSong().getArtistName() + " - " + mp.getCurrentSong().getSongName());
        }
    }

    @FXML
    void next(MouseEvent event) {
        playNextSong();
    }

    @FXML
    void playPause(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Song song = songsOnPlaylist.getSelectionModel().getSelectedItem();
            Playlist playlist = playlister.getSelectionModel().getSelectedItem();

            if (song == null || playlist == null) return;

            index = songsOnPlaylist.getSelectionModel().getSelectedIndex();
            playSong(musicPlayer, song, playlist);
        }
    }

    @FXML
    void previous(MouseEvent event) {
        playPreviousSong();
    }

    @FXML
    void addToPlaylist(ActionEvent event) {
        Song song = songListe.getSelectionModel().getSelectedItem();
        Playlist playlist = playlister.getSelectionModel().getSelectedItem();

        if (song != null && playlist != null) {
            if (!playlist.getSongs().contains(song)) {
                playlist.addSong(song);
                sOPData.setAll(playlist.getSongs());

                playlister.refresh();
                songsOnPlaylist.refresh();
                songsOnPlaylist.sort();
            }
        }
    }

    @FXML
    void deletePlaylist(ActionEvent event) {
        if (playlister.getSelectionModel().getSelectedItem() == null) return;

        Playlist p = playlister.getSelectionModel().getSelectedItem();

        if (sOPData.equals(p.getSongs())) {
            sOPData.clear();

            songsOnPlaylist.refresh();
            songsOnPlaylist.sort();
        }

        if (musicPlayer.getCurrentSong() != null && musicPlayer.isPlaying() && musicPlayer.getPlaylistSource().equals(p)) {
            musicPlayer.stop();
        }
        playlistData.remove(p);

        playlister.refresh();
        playlister.sort();
    }

    @FXML
    void deletePlaylistSong(ActionEvent event) {
        Song song = songsOnPlaylist.getSelectionModel().getSelectedItem();
        Playlist playlist = playlister.getSelectionModel().getSelectedItem();

        if (song != null && playlist != null) {
            playlist.removeSong(song);
            sOPData.remove(song);

            if (musicPlayer.getCurrentSong() != null && musicPlayer.isPlaying() && musicPlayer.getPlaylistSource().equals(playlist)) {
                musicPlayer.stop();
            }

            playlister.refresh();
            songsOnPlaylist.refresh();
            songsOnPlaylist.sort();
        }
    }

    private void removeSong(Song s) {
        Playlist source = musicPlayer.getPlaylistSource();
        List<Song> active;

        if (source != null) {
            active = source.getSongs();
        } else {
            active = sOPData;
        }

        int removedIndex = active.indexOf(s);

        if (musicPlayer.isPlaying()
                && musicPlayer.getCurrentSong().getSongURI().equals(s.getSongURI())) {
            musicPlayer.stop();
        }

        for (Playlist p : playlistData) {
            p.removeSong(s);
        }

        sOPData.remove(s);
        songData.remove(s);

        if (removedIndex != -1 && removedIndex < index) {
            index--;
        }

        if (index >= active.size()) {
            index = active.size() - 1;
        }
        if (index < 0 && !active.isEmpty()) {
            index = 0;
        }

        songsOnPlaylist.refresh();
        playlister.refresh();
        songListe.refresh();

        songsOnPlaylist.sort();
        playlister.sort();
        songListe.sort();
    }


    @FXML
    void deleteSong(ActionEvent event) {
        Song selectedSong = songListe.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            removeSong(selectedSong);
        }
    }

    @FXML
    void editPlaylist(ActionEvent event) {
        if (playlister.getSelectionModel().getSelectedItem() == null) return;
        Playlist playlist = playlister.getSelectionModel().getSelectedItem();

        newPlaylistDialog("Edit Playlist", playlist.getName(),txtf -> {
            if (txtf.getText().isEmpty()) return;

            playlist.setName(txtf.getText());

            playlister.refresh();
            playlister.sort();
        });
    }

    @FXML
    void newPlaylist(ActionEvent event) {
        newPlaylistDialog("New Playlist", "", txtf -> {
            if (txtf.getText().isEmpty()) return;

            String name = txtf.getText();
            Playlist playlist = new Playlist();
            playlist.setName(name);

            playlistData.add(playlist);
            playlister.sort();
        });
    }

    @FXML
    void editSong(ActionEvent event) {
        Song selectedSong = songListe.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            newSongDialog("Edit Song", selectedSong, song -> {
                if (!song.isValidSong()) {
                    songData.remove(selectedSong);
                    errorWindow(nySangFejl);
                    return;
                }

                for (Playlist p : playlistData) {
                    // Playlisten der har sangen vi har ændret ændres automatisk, men hvad så med sange der måske har den samme fil, men ikke er det samme element?
                    if (!p.getSongs().contains(song) && p.getSongs().stream().anyMatch(s -> s.getSongURI().equals(song.getSongURI()))) {
                        for (Song s : p.getSongs()) {
                            if (s != null) {
                                s.setSongName(song.getSongName());
                                s.setArtistName(song.getArtistName());
                                s.setSongFile(new File(song.getFilePath()));
                                p.updateLength();
                            }
                        }
                    }
                }

                if (musicPlayer.isPlaying() && musicPlayer.getCurrentSong().getSongURI().equals(song.getSongURI())) {
                    nowPlaying.setText(song.getArtistName() + " - " + song.getSongName());
                }

                songsOnPlaylist.refresh();
                songsOnPlaylist.sort();

                songListe.refresh();
                songListe.sort();
            });
        }
    }

    @FXML
    void museKlik(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Playlist playlist = playlister.getSelectionModel().getSelectedItem();

            if (playlist != null) {
                sOPData.setAll(playlist.getSongs());

                if (!searchField.getText().trim().isEmpty()) {
                    search(null);
                }
            }
        }
    }

    @FXML
    void newSong(ActionEvent event) {
        newSongDialog("New Song", null, song -> {
            if (!song.isValidSong()) { // filen eksistere ikke
                errorWindow(nySangFejl);
                return;
            }

            if (songData.stream().anyMatch(song1 -> song1.getSongURI().equals(song.getSongURI()))) {
                return; // Vi behøver ikke to af den samme sang
            }

            songData.add(song);
        });
    }

    @FXML
    void search(ActionEvent event) {
        String search = searchField.getText().toLowerCase();

        if (search.isEmpty() || search.trim().isEmpty()) {
            songsOnPlaylist.setItems(sOPData);
            return;
        }

        List<Song> result = sOPData.stream()
                .filter(song -> song.getSongName().toLowerCase().contains(search) || song.getArtistName().toLowerCase().contains(search))
                .toList();

        songsOnPlaylist.setItems(FXCollections.observableList(result));
    }

    private void errorWindow(String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.setTitle("Der skete en fejl...");

        Label label = new Label(message);
        label.setWrapText(true);

        dialog.getDialogPane().setContent(label);
        dialog.showAndWait();
    }

    // Skaber en HBox med noget tekst ved siden af et tekstfelt
    private Pair<TextField, HBox> newTextField(String labelText, String promptText, boolean editable) {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);

        Label label = new Label(labelText);
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setEditable(editable);

        hbox.getChildren().addAll(label, textField);

        return new Pair<>(textField, hbox); // Sikrer sig at tekstfeltet og HBox ikke skifter rundt, når man har brug for dem
    }

    // Skab nyt vindue til at tilføje / ændre sange
    private void newSongDialog(String header, Song song, Consumer<Song> onApply) {
        if (song == null) song = new Song(); // Hvis vi ikke er i gang med at ændre en sang vi har på playlisten

        // Skab ny dialog vindue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(header);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        // Skab tekst felter til sangens egenskaber

        Pair<TextField, HBox> titlePair = newTextField("Title:", "Title here...", true);
        Pair<TextField, HBox> artistPair = newTextField("Artist:", "Artist here...", true);
        Pair<TextField, HBox> timePair = newTextField("Time:", "", false);
        Pair<TextField, HBox> filePair = newTextField("File:", "File here...", false);

        // Få fat på tekstfelterne, der ændres

        TextField titleField = titlePair.getKey();
        TextField artistField = artistPair.getKey();
        TextField timeField = timePair.getKey();
        TextField filePathField = filePair.getKey();

        // Hvis vi har valgt en sang, der allerede eksistere, så skifter vi teksten på sangens egenskaber

        titleField.setText(!song.getSongName().isEmpty() ? song.getSongName() : "");
        artistField.setText(!song.getArtistName().isEmpty() ? song.getArtistName() : "");
        timeField.setText(song.getLength());
        filePathField.setText(!song.getFilePath().isEmpty() ? song.getFilePath() : "");

        // Vælg lydfil

        Button chooseFile = new Button("Choose...");
        filePair.getValue().getChildren().add(chooseFile);

        Song finalSong = song;
        chooseFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose sound file...");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Sound files", "*.mp3", "*.wav", "*.ogg")
            ); // Vi må kun vælge .mp3, .wav, og .ogg filer.

            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                filePathField.setText(selectedFile.getAbsolutePath());
                finalSong.setSongFile(selectedFile);

                if (!finalSong.isValidSong()) {
                    errorWindow(nySangFejl);
                }

                // Skift egenskaber
                titleField.setText(finalSong.getSongName());
                artistField.setText(finalSong.getArtistName());
                timeField.setText(finalSong.getLength());
            }
        });

        // Fremvis vinduet
        vbox.getChildren().addAll(titlePair.getValue(), artistPair.getValue(), timePair.getValue(), filePair.getValue());
        dialog.getDialogPane().setContent(vbox);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            song.setSongName(titleField.getText()); // Skift egenskaber
            song.setArtistName(artistField.getText());

            onApply.accept(song);
        }
    }

    // Skab nyt vindue til at tilføje / ændre playlister
    private void newPlaylistDialog(String header, String playlistName, Consumer<TextField> onApply) {
        // Skab dialog vinduet
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(header);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);

        Pair<TextField, HBox> editPair = newTextField("Name:", "Playlist name here...", true);
        TextField txtf = editPair.getKey();
        txtf.setText(playlistName);

        dialog.getDialogPane().setContent(editPair.getValue());

        Platform.runLater(txtf::requestFocus); // Spørg efter focus på tekstfeltet
        txtf.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                dialog.setResult(ButtonType.APPLY);
                dialog.close();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                dialog.setResult(ButtonType.CANCEL);
                dialog.close();
            }
        }); // For hurtigere ændring hvis der trykkes på ENTER imens du har fokus på tekstfeltet

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            onApply.accept(txtf);
        }
    }
}


