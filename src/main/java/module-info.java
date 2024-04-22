module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    exports Test;
    exports controller;
    opens controller;
    requires com.dlsc.formsfx;
    requires java.sql;
    opens Entities to javafx.base;
    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
}
