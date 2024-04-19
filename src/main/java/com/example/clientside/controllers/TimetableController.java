package com.example.clientside.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.example.clientside.models.ClassInfo;

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

public class TimetableController {

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

    public void populateTimetable(List<ClassInfo> classInfos) {
        timetableGrid.getChildren().clear();
        for (ClassInfo classInfo : classInfos) {
            int dayIndex = dayToIndex.getOrDefault(classInfo.getDay(), -1);
            int timeIndex = timeToIndex.getOrDefault(LocalTime.parse(classInfo.getTime(), DateTimeFormatter.ofPattern("HH:mm")), -1);
            if (dayIndex != -1 && timeIndex != -1) {
                Label classLabel = new Label(classInfo.getClassName() + "\n" + classInfo.getRoom());
                classLabel.setWrapText(true);
                classLabel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null)));
                classLabel.setPadding(new Insets(5));
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
    private void refreshTimetable() {
        // This should trigger fetching the schedule and updating the grid
        classManagementController.handleDisplayButton();

    }
}
