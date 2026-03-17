module com.example.assignment3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires java.desktop;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.assignment3.presentation to javafx.fxml;
    opens com.example.assignment3.start to javafx.fxml;
    exports com.example.assignment3.presentation;
    exports com.example.assignment3.start;
    exports com.example.assignment3.model;
    exports com.example.assignment3.dao;
    exports com.example.assignment3.connection;
    exports com.example.assignment3.bll;
}
