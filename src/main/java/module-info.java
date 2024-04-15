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

    opens com.example.tourjoy to javafx.fxml;
    exports com.example.tourjoy;


}