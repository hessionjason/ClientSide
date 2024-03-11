package com.example.clientside.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class ClassManagementController {

    private Socket socket;

    @FXML
    private TextField classNameField;

    @FXML
    private TextField roomField;

    @FXML
    private TextField dayField;

    @FXML
    private TextField timeField;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button displayButton;

    @FXML
    private Button stopButton;

    @FXML
    private TextArea responseTextArea;
    private PrintWriter writer;
    private BufferedReader reader;


    @FXML
    private void initialize() {
        try {
            // Initialize socket connection with server
            socket = new Socket("localhost", 1236); // (Change) Example server IP address and port
            // You might want to initialize input/output streams here as well
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace(); // Handle connection initialization failure
            // Display error message to the user
            responseTextArea.setText("Error: Could not connect to the server.");
        }
    }

    // Method to handle the Add button click event
    @FXML
    private void handleAddButton() {
        // Implement the logic to send add class request to the server
        // Update responseTextArea with server response
        try {
            // Get input values from TextFields
            String className = classNameField.getText();
            String room = roomField.getText();
            String day = dayField.getText();
            String time = timeField.getText();

            // Construct message to send to server
            String message = "ADD_CLASS " + className + " " + room + " " + day + " " + time;
            writer.println(message);

            // Receive response from server
            String response = reader.readLine();
            // Handle server response
            handleServerResponse(response);
        } catch (Exception e) {
            e.printStackTrace(); // Handle communication error
            // Display error message to the user
            responseTextArea.setText("Error: Communication with the server failed.");


        }
    }

    // Method to handle the Remove button click event
    @FXML
    private void handleRemoveButton() {
        try {
            String className = classNameField.getText().trim(); // Get the class name from the text field
            String room = roomField.getText().trim(); // Get the room from the text field
            String day = dayField.getText().trim(); // Get the day from the text field
            String time = timeField.getText().trim(); // Get the time from the text field

            // Create the message to send to the server
            String message = "REMOVE_CLASS " + className + " " + room + " " + day + " " + time;
            writer.println(message);

            String response = reader.readLine();
            handleServerResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            responseTextArea.setText("Error: Communication with the server failed.");
        }
    }


    // Method to handle the Display button click event
    @FXML
    public void handleDisplayButton() {
        try {
            String message = "DISPLAY_SCHEDULE";
            writer.println(message);

            StringBuilder schedule = new StringBuilder();
            String response = reader.readLine(); // Read the first line of response

            // Check if response is null or empty
            if (response != null && !response.isEmpty()) {
                // Append the first line of response
                schedule.append(response).append("\n");

                // Read remaining lines of response
                while (reader.ready()) {
                    response = reader.readLine();
                    schedule.append(response).append("\n");
                }

                // Print the received schedule to the console
                System.out.println("Received Class Schedule:");
                System.out.println(schedule.toString());

                // Display the schedule in the TableView
                displayScheduleWindow(schedule.toString());
            } else {
                System.out.println("No response received from the server.");
                responseTextArea.setText("Error: Empty response from the server.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            responseTextArea.setText("Error: Communication with the server failed.");
        }
    }
    private Stage scheduleStage; // Add this as a class-level variable

    private void displayScheduleWindow(String schedule) {
        try {
            // Check if the scheduleStage is already initialized and showing
            if (scheduleStage != null && scheduleStage.isShowing()) {
                // If the stage is already showing, close it
                scheduleStage.close();
            }

            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ScheduleWindow.fxml"));
            Parent root = loader.load();

            // Set the controller and pass the schedule data
            ScheduleWindowController controller = loader.getController();
            controller.setScheduleData(schedule);

            // Set the ClassManagementController reference
            controller.setClassManagementController(this);

            // Create a new stage
            scheduleStage = new Stage();
            scheduleStage.setTitle("Class Schedule");

            // Set the scene
            Scene scene = new Scene(root);
            scheduleStage.setScene(scene);

            // Show the window
            scheduleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleDisplayModuleButton() {
        String message = "DISPLAY_MODULE_SCHEDULE";
        writer.println(message);

    }

    // Method to handle the Stop button click event
    @FXML
    private void handleStopButton() {
        try {
            String message = "STOP";
            writer.println(message);
            socket.close();
            responseTextArea.setText("Connection with the server terminated.");
        } catch (IOException e) {
            e.printStackTrace();
            responseTextArea.setText("Error: Communication with the server failed.");
        }
    }

    // Method to append text to the console
    private void appendToConsole(String text) {
        System.out.println(text);
    }

    // Method to handle receiving response from the server
    private void handleServerResponse(String response) {
        // Update responseTextArea with server response
        responseTextArea.setText(response);
        // Print server response to console
        appendToConsole("Server response: " + response);
    }
}