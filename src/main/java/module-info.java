module com.example.medieafspiller {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires jaudiotagger;
    requires java.logging;
    requires java.sql;

    opens com.example.medieafspiller to javafx.fxml;
    exports com.example.medieafspiller;
}