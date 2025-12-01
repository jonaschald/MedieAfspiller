package com.example.medieafspiller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javax.swing.*;
import java.util.Optional;

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

    public void initialize() {
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
    }

    @FXML
    private TableColumn<Song, String> songName;

    @FXML
    private TableView<Song> songsOnPlaylist;

    @FXML
    private Slider volume;

    @FXML
    void addToPlaylist(ActionEvent event) {

    }

    @FXML
    void deletePlaylist(ActionEvent event) {
        if (playlister.getSelectionModel().getSelectedItem() == null) return;

        Playlist p = playlister.getSelectionModel().getSelectedItem();
        playlistData.remove(p);
        playlister.refresh();
    }

    @FXML
    void deleteSong(ActionEvent event) {

    }

    @FXML
    void editPlaylist(ActionEvent event) {
        if (playlister.getSelectionModel().getSelectedItem() == null) return;
        Playlist playlist = playlister.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Playlist");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);

        Label txt = new Label("Name:");
        TextField txtf = new TextField(playlist.getName());
        txtf.setPromptText("Playlist name here...");

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(txt, txtf);

        dialog.getDialogPane().setContent(hbox);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            if (txtf.getText().isEmpty()) return;

            playlist.setName(txtf.getText());
            playlister.refresh();
        }
    }

    @FXML
    void editSong(ActionEvent event) {

    }

    @FXML
    void museKlik(MouseEvent event) {

    }

    @FXML
    void newPlaylist(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("New Playlist");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);

        Label txt = new Label("Name:");
        TextField txtf = new TextField();
        txtf.setPromptText("Playlist name here...");

        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(txt, txtf);

        dialog.getDialogPane().setContent(hbox);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.APPLY) {
            if (txtf.getText().isEmpty()) return;

            String name = txtf.getText();
            Playlist playlist = new Playlist();
            playlist.setName(name);

            playlistData.add(playlist);
        }
    }

    @FXML
    void newSong(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }

}


