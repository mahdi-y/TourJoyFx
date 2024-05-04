module com.example.tourjoy {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires charm.glisten;
    requires java.mail;
    requires java.net.http;
    requires com.google.gson;
    requires org.testng;
    requires org.junit.jupiter.api;

    opens com.example.tourjoy to javafx.fxml;
    exports com.example.tourjoy;
    exports controllers;
    opens controllers to javafx.fxml;
}