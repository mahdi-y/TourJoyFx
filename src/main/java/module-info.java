module com.example.tourjoy {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires charm.glisten;
    requires java.mail;

    opens com.example.tourjoy to javafx.fxml;
    exports com.example.tourjoy;
    exports controllers;
    opens controllers to javafx.fxml;
}