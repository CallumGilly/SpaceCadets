package com.example.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientController {
    String currentRoom = "default";
    @FXML private TextField msgBox;
    @FXML private TextField addressInput;
    @FXML private TextField usernameInput;
    @FXML protected TextArea chatBox;
    @FXML protected Button sentBtn;
    private Client client = null;

    @FXML
    protected void send() {
        //Send msgBox.getText() to the sever here
        client.send(usernameInput.getText() + ": " + msgBox.getText());
        msgBox.clear();
    }
    @FXML
    protected void toggleConnect() {
        //Should connect to the sever here with the address from addressInput.getText() and username from usernameInput.getText()
        if (client == null || !client.isConnected()) {
            client = new Client(addressInput.getText(), chatBox);
            client.start();
            sentBtn.setDisable(false);
            msgBox.setDisable(false);
        } else {
            client.disconnect();
            sentBtn.setDisable(true);
            msgBox.setDisable(true);
        }
    }
    @FXML
    protected void setCurrentRoom() {
        //This is not yet implemented, defaults to default
    }



}