package com.example.clientside.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.example.clientside.models.ClassInfo;
import javafx.scene.layout.Pane;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.geometry.Insets;

public class TimetableModuleController {
    @FXML
    private GridPane timetableGrid;

    private ClassManagementController classManagementController;

    private final Map<String, Integer> dayToIndex = new HashMap<>();

    private final Map<LocalTime, Integer> timeToIndex = new HashMap<>();

    public void setClassManagementController(ClassManagementController controller) {
        this.classManagementController = controller;
    }
    @FXML
    public void initialize() {
        // Initialization logic here...
        timetableGrid.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        timetableGrid.setGridLinesVisible(true); // Make sure grid lines are visible
        fillGridWithEmptyPanes();

        dayToIndex.put("Monday", 0);
        dayToIndex.put("Tuesday", 1);
        dayToIndex.put("Wednesday", 2);
        dayToIndex.put("Thursday", 3);
        dayToIndex.put("Friday", 4);

        LocalTime startTime = LocalTime.of(9, 0); // assuming start time is 9 AM
        for (int i = 0; i < 9; i++) {
            timeToIndex.put(startTime.plusHours(i), i);
        }
    }
    private void fillGridWithEmptyPanes() {
        for (int row = 0; row < 9; row++) { // For each hour slot
            for (int col = 0; col < 5; col++) { // For each day of the week
                Pane emptyPane = new Pane();
                emptyPane.getStyleClass().add("grid-cell");
                timetableGrid.add(emptyPane, col, row + 1); // +1 if headers are in the first row
            }
        }
    }
    public void populateTimetable(List<ClassInfo> classInfos) {
        //timetableGrid.getChildren().clear();
        timetableGrid.getChildren().removeIf(node -> node instanceof Label && "class-label".equals(node.getStyleClass().get(0)));

        for (ClassInfo classInfo : classInfos) {
            int dayIndex = dayToIndex.getOrDefault(classInfo.getDay(), -1);
            int timeIndex = timeToIndex.getOrDefault(LocalTime.parse(classInfo.getTime(), DateTimeFormatter.ofPattern("HH:mm")), -1);

            if (dayIndex != -1 && timeIndex != -1) {
                Label classLabel = new Label(classInfo.getClassName() + "\n" + classInfo.getRoom() + "\n" + classInfo.getTime());
                classLabel.setWrapText(true);
                classLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                classLabel.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 5; -fx-background-color: lightgrey;");
                timetableGrid.add(classLabel, dayIndex, timeIndex);
            }
        }
    }
    public List<ClassInfo> parseSchedule(String schedule) {
        List<ClassInfo> scheduleData = new ArrayList<>();
        String[] lines = schedule.split("\n");
        for (String line : lines) {
            String[] parts = line.split(", ");
            if (parts.length == 5) {
                int classId = Integer.parseInt(parts[0].split(": ")[1]);
                String className = parts[1].split(": ")[1];
                String room = parts[2].split(": ")[1];
                String day = parts[3].split(": ")[1];
                String time = parts[4].split(": ")[1];
                scheduleData.add(new ClassInfo(classId, className, room, day, time));
            }
        }
        return scheduleData;
    }


    @FXML
    private void refreshModuleTimetable() {
        /*if (classManagementController != null) {
            // Fetch the latest schedule from the ClassManagementController
            String schedule = classManagementController.fetchLatestSchedule(); // Ensure there's a method fetchLatestSchedule() in ClassManagementController
            List<ClassInfo> classInfos = parseSchedule(schedule);
            populateTimetable(classInfos);
        }
    }*/
        classManagementController.handleDisplayModuleSchedule();
    }
}
