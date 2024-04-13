module com.example.tourjoy {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.tourjoy to javafx.fxml;
    exports com.example.tourjoy;
}