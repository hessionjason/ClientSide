package com.example.clientside.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ClassInfo {
    private final SimpleIntegerProperty classId;
    private final SimpleStringProperty className;
    private final SimpleStringProperty room;
    private final SimpleStringProperty day;
    private final SimpleStringProperty time;

    public ClassInfo(int classId, String className, String room, String day, String time) {
        this.classId = new SimpleIntegerProperty(classId);
        this.className = new SimpleStringProperty(className);
        this.room = new SimpleStringProperty(room);
        this.day = new SimpleStringProperty(day);
        this.time = new SimpleStringProperty(time);
    }

    public int getClassId() {
        return classId.get();
    }

    public void setClassId(int classId) {
        this.classId.set(classId);
    }

    public SimpleIntegerProperty classIdProperty() {
        return classId;
    }

    public String getClassName() {
        return className.get();
    }

    public void setClassName(String className) {
        this.className.set(className);
    }

    public SimpleStringProperty classNameProperty() {
        return className;
    }

    public String getRoom() {
        return room.get();
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public SimpleStringProperty roomProperty() {
        return room;
    }

    public String getDay() {
        return day.get();
    }

    public void setDay(String day) {
        this.day.set(day);
    }

    public SimpleStringProperty dayProperty() {
        return day;
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }
}


