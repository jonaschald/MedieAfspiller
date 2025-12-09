package com.example.medieafspiller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class myTunesApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(myTunesApplication.class.getResource("myTunes.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("myTunes");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        myTunesController.saveData();
    }
}