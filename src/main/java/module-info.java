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
    //requires java.net.http; // Corrected line

    opens com.test.tjv2 to javafx.fxml;
    exports com.test.tjv2;
}