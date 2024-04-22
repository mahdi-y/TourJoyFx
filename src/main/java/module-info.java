module com.example.tourjoy {
    requires javafx.controls;
    requires javafx.fxml;
    exports controllers;
    opens controllers;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
    requires com.google.gson;
    requires java.desktop;

    opens com.example.tourjoy to javafx.fxml, javafx.base;
    opens models to javafx.base;
    exports com.example.tourjoy;


}