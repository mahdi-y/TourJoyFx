module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    exports Test;
    exports controller;
    opens controller;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.json;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    opens Entities to javafx.base;
    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;


}
