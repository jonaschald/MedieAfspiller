package com.example.medieafspiller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class myTunesController {

    @FXML
    private ImageView back;

    @FXML
    private TableColumn<?, ?> lenghtOfPlaylist;

    @FXML
    private TableColumn<?, ?> nameOfPlaylist;

    @FXML
    private ImageView next;

    @FXML
    private Label nowPlaying;

    @FXML
    private TableColumn<?, ?> numberOfSongsOnPlaylist;

    @FXML
    private ImageView playPause;

    @FXML
    private TableView<?> playlister;

    @FXML
    private TableColumn<?, ?> sOPArtist;

    @FXML
    private TableColumn<?, ?> sOPLenght;

    @FXML
    private TableColumn<?, ?> sOPName;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<?, ?> songArtist;

    @FXML
    private TableColumn<?, ?> songLenght;

    @FXML
    private TableView<?> songListe;

    @FXML
    private TableColumn<?, ?> songName;

    @FXML
    private TableView<?> songsOnPlaylist;

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

