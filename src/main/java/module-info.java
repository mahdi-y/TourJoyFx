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
    requires java.mail;
    requires com.google.api.client.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;
    requires org.apache.commons.codec;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires jdk.httpserver;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.tourjoy to javafx.fxml, javafx.base;
    opens models to javafx.base;
    exports com.example.tourjoy;


}