module com.example.behnamplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires mp3agic;

    opens com.example.behnamplayer to javafx.fxml;
    exports com.example.behnamplayer;
}