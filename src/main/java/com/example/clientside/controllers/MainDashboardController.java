package com.example.clientside.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class MainDashboardController {

    @FXML
    private void handleClassManagement(ActionEvent event) {
        try {
            // Load ClassManagement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClassManagement.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene to ClassManagement
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Class Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
