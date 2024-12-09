module com.example.aeropuerto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    exports com.example.aeropuerto1.app;
    opens com.example.aeropuerto1.model to javafx.base;
    opens com.example.aeropuerto1.app to javafx.fxml;
    exports com.example.aeropuerto1.controller;
    opens com.example.aeropuerto1.controller to javafx.fxml;
}