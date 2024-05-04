module com.example.helloapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    //requires java.json; // This line ensures your module can use the javax.json API

    exports Test; // Exportez le package contenant MainFX
    exports Controller;
    opens Controller;
    requires com.dlsc.formsfx;
    requires java.sql;
    opens Entities to javafx.base;
    requires jakarta.json;
    requires org.json;
    // requires org.apache.httpcomponents.httpcore;
    // requires org.apache.httpcomponents.httpasyncclient;

    requires java.desktop;
//requires google.cloud.texttospeech;
    requires com.google.protobuf;
    //  requires unirest.java;
    requires java.net.http;
    requires org.controlsfx.controls;
    requires org.apache.pdfbox;
    requires com.google.api.services.gmail;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires org.apache.commons.codec;
    requires mail;
    requires stripe.java;
    requires java.json;

    opens com.example.helloapplication to javafx.fxml;
    exports com.example.helloapplication;
}

