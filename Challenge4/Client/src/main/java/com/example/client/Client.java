package com.example.client;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.zip.Adler32;

public class Client extends Thread{
    protected Socket connection = null;
    protected PrintWriter output = null;
    protected BufferedReader input;
    private TextArea messageArea;
    public Client(String address, TextArea messageArea) {
        this.messageArea = messageArea;
        try {
            String[] addressArr = address.split(":");
            connection = new Socket(addressArr[0], Integer.parseInt(addressArr[1]));
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        while (true) {
            try {
                if (input.ready()) {
                    messageArea.setText(messageArea.getText() + "\n" + input.readLine());
                }

            } catch (Exception ex) {
                System.out.println(ex);

            }
        }
    }

    public void send(String message) {
    try {
        output.println(message);
        output.flush();
        System.out.println("Message Sent");
    } catch (Exception ex) {
        System.out.println(ex);
    }
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
