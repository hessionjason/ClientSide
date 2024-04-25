package com.example.clientside.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;


public class MainLoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessageLabel;

    private BorderPane rootLayout;

    // Method to set the root layout (BorderPane) of the MainLogin.fxml
    public void setRootLayout(BorderPane rootLayout) {
        this.rootLayout = rootLayout;
    }

    // Event handler for the login button
    @FXML
    private void handleLoginButton(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate username and password (You can implement your validation logic here)
        if (isValidCredentials(username, password)) {
            // If credentials are valid, navigate to Main Dashboard screen
            navigateToMainDashboard(event);
        } else {
            // If credentials are invalid, display error message
            errorMessageLabel.setText("Invalid username or password.");
        }
    }

    // Method to validate user credentials (dummy implementation)
    private boolean isValidCredentials(String username, String password) {
        // Dummy validation logic (replace with actual authentication)
        return username.equals("user") && password.equals("12345");
    }

    // Method to navigate to Main Dashboard screen
    // Method to navigate to Main Dashboard screen
    private void navigateToMainDashboard(ActionEvent event) {
        try {
            // Load MainDashboard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainDashboard.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene to MainDashboard
            Scene scene = new Scene(root);

            // Add CSS stylesheet to the scene
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Main Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

