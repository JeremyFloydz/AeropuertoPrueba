package com.example.aeropuerto1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class aeropuertosControler {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}