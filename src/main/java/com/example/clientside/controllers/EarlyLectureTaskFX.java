package com.example.clientside.controllers;

import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EarlyLectureTaskFX extends Task<Void> {
    private String day;
    private static final String SERVER_ADDRESS = "127.0.0.1"; // Replace with your server IP address
    private static final int SERVER_PORT = 1249; // Replace with your server port

    public EarlyLectureTaskFX(String day) {
        this.day = day;
    }

    @Override
    protected Void call() throws Exception {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            // Send early lecture request to the server
            writer.println("EARLY_LECTURE_REQUEST " + day);

            // Receive response from the server
            String response = reader.readLine();

            // Update GUI based on server response
            updateMessage(response);
        } catch (IOException e) {
            e.printStackTrace();
            // Update GUI with error message
            updateMessage("Failed to process early lecture request for day " + day);
        }

        return null;
    }
}
