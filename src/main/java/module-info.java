module com.tourjoy.ihebtransportt {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;
    requires com.gluonhq.charm.glisten;
    requires com.gluonhq.attach.util;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires okhttp3;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires freetts;
    opens Controller to javafx.fxml; // This line opens the Controller package to the javafx.fxml module
    exports com.tourjoy.ihebtransportt;
    opens Entities to javafx.base;
}