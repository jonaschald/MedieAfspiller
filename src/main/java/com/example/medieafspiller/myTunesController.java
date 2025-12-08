package com.example.medieafspiller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private final ObservableList<Song> songData = FXCollections.observableArrayList();
    private final ObservableList<Playlist> playlistData = FXCollections.observableArrayList();
    private final ObservableList<Song> sOPData = FXCollections.observableArrayList();

    private final MusicPlayer musicPlayer = new MusicPlayer();
    private int index = 0;

    // For at forklare hvorfor nogle filer ikke virker
    private final String nySangFejl = String.format(
            "Sangen er ikke valid. Det kan være fordi:%n" +
                    "%4sFilen kan ikke findes%n" +
                    "%4sFilen kan ikke læses%n" +
                    "%4sFilen er ikke et understøttet medie%n" +
                    "%4sFilen har ikke en sample størrelse på 16 eller en sample rate på 44.1 kHz eller 48 kHz",
            "", "", "", ""
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

        musicPlayer.setOnEndOfMedia(this::playNextSong);
    }

    private void playNextSong() {
        if (sOPData.size() - 1 == index) index = -1;

        index++;
        songsOnPlaylist.getSelectionModel().select(index);

        playSong(musicPlayer, songsOnPlaylist.getSelectionModel().getSelectedItem());
    }

    private void playPreviousSong() {
        if (index == 0) index = sOPData.size();

        index--;
        songsOnPlaylist.getSelectionModel().select(index);

        playSong(musicPlayer, songsOnPlaylist.getSelectionModel().getSelectedItem());
    }

    private void playSong(MusicPlayer mp, Song song) {
        if (mp.isPlaying() && mp.getCurrentSong() == song) {
            mp.pause();
        } else if (mp.isPlaying() && mp.getCurrentSong() != song) {
            mp.play(song);
        } else {
            mp.play(song);
        }

        nowPlaying.setText(mp.getCurrentSong().getArtistName() + " - " + mp.getCurrentSong().getSongName());
    }

    @FXML
    private TableColumn<Song, String> songName;

    @FXML
    private TableView<Song> songsOnPlaylist;

    @FXML
    private Slider volume;

    @FXML
    void next(MouseEvent event) {
        playNextSong();
    }

    @FXML
    void playPause(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Song song = songsOnPlaylist.getSelectionModel().getSelectedItem();

            if (song == null) return;

            index = songsOnPlaylist.getSelectionModel().getSelectedIndex();
            playSong(musicPlayer, song);
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

            playlister.refresh();
            songsOnPlaylist.refresh();
            songsOnPlaylist.sort();
        }
    }

    @FXML
    void deleteSong(ActionEvent event) {
        Song selectedSong = songListe.getSelectionModel().getSelectedItem();

        if (selectedSong != null) {
            songData.remove(selectedSong);

            songListe.refresh();
            songListe.sort();
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
                    return;
                }

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
        dialog.show();
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


