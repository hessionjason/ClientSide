package com.example.clientside.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;
import javafx.application.Platform;


public class EarlyLectureRequestController {

    @FXML
    private TextField dayTextField;

    @FXML
    private Button submitRequest;

    private PrintWriter writer;

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    @FXML
    private void submitRequest() {
        String day = dayTextField.getText().trim();
        if (!day.isEmpty()) {
            try {
                Socket socket = new Socket("localhost", 1249);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Send the early lecture request to the server
                out.println("EARLY_LECTURE_REQUEST " + day);

                // Await response from the server
                String response = in.readLine();
                Platform.runLater(() -> {
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                    infoAlert.setTitle("Server Response");
                    infoAlert.setHeaderText(null);
                    infoAlert.setContentText(response);
                    infoAlert.showAndWait();
                });

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Connection Error");
                    errorAlert.setHeaderText("Failed to connect to the server");
                    errorAlert.setContentText("Please check your connection settings.");
                    errorAlert.showAndWait();
                });
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid day.");
            alert.showAndWait();
        }
    }


    @FXML
    private void cancelRequest() {
        // Perform actions to cancel the early lecture request
        System.out.println("Early lecture request canceled");
        // You can add more logic here, such as closing the scene
    }
}
