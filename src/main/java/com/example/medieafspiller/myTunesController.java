package com.example.medieafspiller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class myTunesController {

    @FXML
    private TableColumn<?, ?> lenghtOfPlaylist;

    @FXML
    private TableColumn<?, ?> nameOfPlaylist;

    @FXML
    private TableColumn<?, ?> numberOfSongsOnPlaylist;

    @FXML
    private TableView<?> playlister;

    @FXML
    private TableColumn<?, ?> sOPArtist;

    @FXML
    private TableColumn<?, ?> sOPLenght;

    @FXML
    private TableColumn<?, ?> sOPName;

    @FXML
    private TableColumn<?, ?> songArtist;

    @FXML
    private TableColumn<?, ?> songLenght;

    @FXML
    private TableView<?> songListe;

    @FXML
    private TableColumn<?, ?> songName;

    @FXML
    void Search(ActionEvent event) {

    }

}