module com.example.medieafspiller {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.medieafspiller to javafx.fxml;
    exports com.example.medieafspiller;
}