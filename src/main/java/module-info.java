module com.test.tjv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.json; // This line ensures your module can use the javax.json API

    exports Test; // Exportez le package contenant MainFX
    exports Controller;
    opens Controller;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires javafx.web;
    opens Entities to javafx.base;
    requires jakarta.json;
    requires org.json;
   // requires org.apache.httpcomponents.httpcore;
   // requires org.apache.httpcomponents.httpasyncclient;

    requires java.desktop;
    requires google.cloud.texttospeech;
    requires com.google.protobuf;
  //  requires unirest.java;
    requires java.net.http;
    requires org.controlsfx.controls;
    requires org.apache.pdfbox;
    requires com.google.api.client.auth;
    requires com.google.api.services.gmail;
    requires mail;
    requires com.google.api.client;
    requires google.api.client;
    requires com.google.api.client.json.gson;
    requires stripe.java;
    requires org.apache.commons.codec;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    //requires java.net.http; // Corrected line
    exports controllers to javafx.fxml;
    opens controllers to javafx.fxml; // if your controllers use reflection, for example, for @FXML annotations

    opens com.test.tjv2 to javafx.fxml;
    exports com.test.tjv2;
}