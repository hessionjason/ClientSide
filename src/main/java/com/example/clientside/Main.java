package com.example.clientside;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;

public class Main extends Application {

    private static Main instance;
    private Stage primaryStage;
    private Stage scheduleStage; // Declare a stage field to hold the reference to the schedule window stage


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        instance = this;
        showMainLogin(); // Show the MainLogin screen initially
    }

    public static Main getInstance() {
        return instance;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showMainLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainLogin.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Main Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainDashboard.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Main Dashboard");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showClassManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClassManagement.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = Main.getInstance().getPrimaryStage();

            // Set the scene to ClassManagement
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Class Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showClassSchedule() {
        try {
            // Load ScheduleWindow.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ScheduleWindow.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = Main.getInstance().getPrimaryStage();

            // Set the scene to ScheduleWindow
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Class Schedule");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
