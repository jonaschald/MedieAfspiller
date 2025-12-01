module com.example.medieafspiller {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires jaudiotagger;


    opens com.example.medieafspiller to javafx.fxml;
    exports com.example.medieafspiller;
}