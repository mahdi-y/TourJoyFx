module com.example.tourjoy {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    //requires java.json; // This line ensures your module can use the javax.json API
    exports Test;
    exports Controller;
    opens Controller;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;
   // requires com.google.gson;
    opens Entities to javafx.base;
    requires jakarta.json;
    requires org.json;
    // requires org.apache.httpcomponents.httpcore;
    // requires org.apache.httpcomponents.httpasyncclient;
    requires java.desktop;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;


    requires java.mail;
//requires google.cloud.texttospeech;
    requires com.google.protobuf;
    //  requires unirest.java;
    requires java.net.http;
    requires org.controlsfx.controls;
    requires com.google.api.services.gmail;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires org.apache.commons.codec;
    requires jdk.httpserver;
    requires twilio;
//    requires charm.glisten;
    requires org.testng;
    requires org.junit.jupiter.api;
//    requires mail;
    requires stripe.java;
    requires java.json;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires bcrypt;

    opens com.example.tourjoy to javafx.fxml, javafx.base;
    opens models to javafx.base;
    exports com.example.tourjoy;
    exports controllers;
    opens controllers to javafx.fxml;
}



