package com.example.clientside.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import com.example.clientside.models.ClassInfo;


import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import javafx.scene.control.Alert;





public class ClassManagementController {

    private Socket socket;

    @FXML
    private ChoiceBox<String> classNameChoiceBox;

    @FXML
    private TextField roomField;

    @FXML
    private ChoiceBox<String> dayChoiceBox ;

    @FXML
    private TextField timeField;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button displayEarlyLectureRequest;

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
            // initialize socket connection with server
            socket = new Socket("localhost", 1249); // server IP address and port

            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            classNameChoiceBox.getItems().addAll("MA4004", "CS4115", "CS4076", "CS4815", "CS4006");
            dayChoiceBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        } catch (IOException e) {
            e.printStackTrace(); // Handle connection initialization failure
            // display error message to the user
            responseTextArea.setText("Error: Could not connect to the server.");
        }
    }

    // method to handle the Add button click event
    @FXML
    private void handleAddButton() {
        try {
            // get input values from TextFields and ChoiceBoxes
            String className = classNameChoiceBox.getValue();
            String room = roomField.getText();
            String day = dayChoiceBox.getValue();
            String time = timeField.getText();

            // message to send to server
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
            // get input values from TextFields and ChoiceBoxes
            String className = classNameChoiceBox.getValue();
            String room = roomField.getText().trim();
            String day = dayChoiceBox.getValue();
            String time = timeField.getText().trim();

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
    //part 2
    @FXML
    private void displayEarlyLectureRequest() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EarlyLecturesScene.fxml"));
            Parent root = loader.load();

            EarlyLectureRequestController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Early Lecture Request");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
    private Stage scheduleStage; // class-level variable

    private void displayScheduleWindow(String schedule) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TimetableGrid.fxml"));
            Parent root = loader.load();
            TimetableController timetableController = loader.getController();

            // Pass this controller's reference to the TimetableController if needed
            timetableController.setClassManagementController(this);

            List<ClassInfo> classInfos = timetableController.parseSchedule(schedule);
            timetableController.populateTimetable(classInfos);

            if (scheduleStage == null) {
                scheduleStage = new Stage();
                scheduleStage.setTitle("Class Schedule");
            } else if (scheduleStage.isShowing()) {
                scheduleStage.close(); // Ensure only one window is open at a time
            }
            scheduleStage.setScene(new Scene(root));
            scheduleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String fetchLatestSchedule() {
        // Logic to fetch the latest schedule
        // For example:
        String message = "DISPLAY_SCHEDULE";
        writer.println(message);
        StringBuilder schedule = new StringBuilder();
        try {
            String response;
            while ((response = reader.readLine()) != null && !response.isEmpty()) {
                schedule.append(response).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedule.toString();
    }




    /*private void displayScheduleWindow(String schedule) {
        try {
            // check if the scheduleStage is already initialized and showing(so wont open two windows
            if (scheduleStage != null && scheduleStage.isShowing()) {
                // If the stage is already showing, close it
                scheduleStage.close();
            }
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TimetableGrid.fxml"));
            Parent root = loader.load();

            // Set the controller and pass the schedule data
            //ScheduleWindowController controller = loader.getController();
            //controller.setScheduleData(schedule);

            // Set the ClassManagementController reference
            //controller.setClassManagementController(this);

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
    }*/
    @FXML
    public void handleDisplayModuleSchedule() {
        try {
            String moduleName = classNameChoiceBox.getValue(); // Get the selected class name from the choice box
            String message = "DISPLAY_MODULE_SCHEDULE " + moduleName;
            writer.println(message);

            StringBuilder moduleSchedule = new StringBuilder();
            String response = reader.readLine(); // Read the first line of response

            // Check if response is null or empty
            if (response != null && !response.isEmpty()) {
                // Append the first line of response
                moduleSchedule.append(response).append("\n");

                // Read remaining lines of response
                while (reader.ready()) {
                    response = reader.readLine();
                    moduleSchedule.append(response).append("\n");
                }

                // Print the received schedule to the console
                System.out.println("Received Module Schedule:");
                System.out.println(moduleSchedule.toString());
                // Display the schedule in the TableView
                displayModuleScheduleWindow(moduleSchedule.toString());

            } else {
                responseTextArea.setText("Error: Empty response from the server.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Module Selection Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a module.");
                alert.showAndWait();
                return; // Exit the method early
            }
        } catch (IOException e) {
            e.printStackTrace();
            responseTextArea.setText("Error: Communication with the server failed.");
        }
    }

    private Stage moduleScheduleStage; // Add this as a class-level variable

    private void displayModuleScheduleWindow(String schedule) {
        /*try {
            // Check if the moduleScheduleStage is already initialized and showing
            if (moduleScheduleStage != null && moduleScheduleStage.isShowing()) {
                // If the stage is already showing, close it
                moduleScheduleStage.close();
            }

            // Load the FXML file for the module schedule window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModuleScheduleWindow.fxml"));
            Parent root = loader.load();

            // Set the controller and pass the schedule data
            ModuleScheduleController controller = loader.getController();
            controller.setClassManagementController(this); // pass a reference to the ClassManagementController
            controller.setModuleScheduleData(schedule);

            // Create a new stage for the module schedule window
            moduleScheduleStage = new Stage();
            moduleScheduleStage.setTitle("Module Schedule");*/

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TimetableModuleGrid.fxml"));
            Parent root = loader.load();
            TimetableModuleController timetableModuleController = loader.getController();

            // Pass this controller's reference to the TimetableController if needed
            timetableModuleController.setClassManagementController(this);

            List<ClassInfo> classInfos = timetableModuleController.parseSchedule(schedule);
            timetableModuleController.populateTimetable(classInfos);

            if (moduleScheduleStage == null) {
                moduleScheduleStage = new Stage();
                moduleScheduleStage.setTitle("Module Schedule");
            } else if (moduleScheduleStage.isShowing()) {
                moduleScheduleStage.close(); // Ensure only one window is open at a time
            }



            // Set the scene
            Scene scene = new Scene(root);
            moduleScheduleStage.setScene(scene);
            // Show the module schedule window
            moduleScheduleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle the Stop button click event
    @FXML
    private void handleStopButton() {
        try {
            String message = "STOP";
            writer.println(message);

            // receive response from server
            String response = reader.readLine();
            if (response.equals("TERMINATE")) {
                // close socket and update UI
                socket.close();
                responseTextArea.setText("Connection with the server terminated.");
                System.out.println("Connection with the server terminated.");
            } else {
                responseTextArea.setText("Unexpected response from the server: " + response);
                System.out.println("Unexpected response from the server: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            responseTextArea.setText("Error: Communication with the server failed.");
            System.out.println("Error: Communication with the server failed.");
        }
    }


    // Method to append text to the console
    private void appendToConsole(String text) {
        System.out.println(text);
    }

    // Method to handle receiving response from the server
    private void handleServerResponse(String response) {
        // updates responseTextArea with server response
        responseTextArea.setText(response);
        // Print server response to console
        appendToConsole("Server response: " + response);
    }
}