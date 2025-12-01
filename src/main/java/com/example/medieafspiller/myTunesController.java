package com.example.medieafspiller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.swing.*;

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

    private void initialize() {
        songName.setCellValueFactory(new PropertyValueFactory<Song, String>("songName"));
        songArtist.setCellValueFactory(new PropertyValueFactory<Song, String>("songArtist"));
        songLength.setCellValueFactory(new PropertyValueFactory<Song, String>("songLength"));

        nameOfPlaylist.setCellValueFactory(new PropertyValueFactory<Playlist, String>("nameOfPlaylist"));
        numberOfSongsOnPlaylist.setCellValueFactory(new PropertyValueFactory<Playlist, String>("numberOfSongsOnPlaylist"));
        lengthOfPlaylist.setCellValueFactory(new PropertyValueFactory<Playlist, String>("lengthOgPlaylist"));

        sOPName.setCellValueFactory(new PropertyValueFactory<Song, String>("songName"));
        sOPArtist.setCellValueFactory(new PropertyValueFactory<Song, String>("songArtist"));
        sOPLength.setCellValueFactory(new PropertyValueFactory<Song, String>("songLength"));
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
    void delete(ActionEvent event) {

    }

    @FXML
    void editPlaylist(ActionEvent event) {

    }

    @FXML
    void editSong(ActionEvent event) {

    }

    @FXML
    void museKlik(MouseEvent event) {

    }

    @FXML
    void newPlaylist(ActionEvent event) {

    }

    @FXML
    void newSong(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }

}


