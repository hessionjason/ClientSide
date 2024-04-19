package com.example.clientside.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.example.clientside.models.ClassInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ScheduleWindowController {

    private ClassManagementController classManagementController; // create instance of ClassManagementController

    @FXML
    private TableView<ClassInfo> classTable;

    @FXML
    private TableColumn<ClassInfo, Integer> classIdCol;

    @FXML
    private TableColumn<ClassInfo, String> classNameCol;

    @FXML
    private TableColumn<ClassInfo, String> roomCol;

    @FXML
    private TableColumn<ClassInfo, String> dayCol;

    @FXML
    private TableColumn<ClassInfo, String> timeCol;

    // Setter method for classManagementController
    public void setClassManagementController(ClassManagementController classManagementController) {
        this.classManagementController = classManagementController;
    }

    public void UpdateTable(){
        // Set cell value factories for each column
        classIdCol.setCellValueFactory(cellData -> cellData.getValue().classIdProperty().asObject());
        classNameCol.setCellValueFactory(cellData -> cellData.getValue().classNameProperty());
        roomCol.setCellValueFactory(cellData -> cellData.getValue().roomProperty());
        dayCol.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());


    }

    public void initialize() {
        UpdateTable();
    }

    public void setScheduleData(String schedule) {
        // Parse the schedule string and create a list of ClassInfo objects
        List<ClassInfo> scheduleData = parseSchedule(schedule);

        // Populate the TableView with the schedule data
        classTable.setItems(FXCollections.observableArrayList(scheduleData));
    }

    @FXML
    private void handleRefreshButton() {
        // call the method to fetch schedule data from the server via ClassManagementController
        classManagementController.handleDisplayButton();
    }

    private List<ClassInfo> parseSchedule(String schedule) {

        List<ClassInfo> scheduleData = new ArrayList<>();

        // Split the schedule string by newline character
        String[] lines = schedule.split("\n");

        // Iterate over each line in the schedule
        for (String line : lines) {
            // Split each line by a delimiter (e.g., comma) to extract class info
            String[] parts = line.split(", ");

            // Assuming the format is: "Class ID: 2, Class Name: MA4004, Room: DG016, Day: Tuesday, Time: 2PM"
            if (parts.length == 5) {
                // Extracting the necessary information from each part
                String classIdStr = parts[0].split(": ")[1];
                String className = parts[1].split(": ")[1];
                String room = parts[2].split(": ")[1];
                String day = parts[3].split(": ")[1];
                String time = parts[4].split(": ")[1];

                // Parsing classId to Integer
                int classId = Integer.parseInt(classIdStr);

                // Create a new ClassInfo object and add it to the list
                ClassInfo classInfo = new ClassInfo(classId, className, room, day, time);
                scheduleData.add(classInfo);
            }
        }

        return scheduleData;
    }
}