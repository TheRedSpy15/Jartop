import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class Login {

    @FXML private JFXPasswordField passwordField;
    @FXML private Label userLbl, defaultLbl;
    @FXML private JFXToggleButton load256Toggle;
    private File userFile;

    @FXML private void login() throws IOException {

        if (userFile != null && passwordField.getText() != null){

            Core.getUserData().setUserFile(this.userFile);

            try {
                Core.getUserData().login(passwordField.getText(), load256Toggle.selectedProperty().getValue());
            } catch (NullPointerException | InterruptedException e){
                e.printStackTrace();
            }
        }

        else select();
    }

    @FXML private void select() {

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select a user");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "User Files (." + User.saveExtension + ")",
                        "*" + User.saveExtension)
        );

        userFile = fileChooser.showOpenDialog(Desktop.getAppWindow());
        if (userFile != null) {

            userLbl.setText("Selected User: " + userFile.getName());

            Core.getUAS().setFailedAttempts((byte) 0);
        }
    }

    @FXML private void guest() throws IOException {

        Core.getUserData().setGuest(true);

        Core.loadAndTitle("Desktop");

        Logger.getAnonymousLogger().info("Logging in as guest");

        Notifications
                .create()
                .title("Notice")
                .text("Logged in as guest")
                .darkStyle()
                .showInformation();
    }

    @FXML private void newUser() throws IOException {

        new NewUser().create();
    }

    @FXML private void exit(){

        Logger.getAnonymousLogger().info("Shutting down...");

        System.exit(0);
    }

    @FXML private void copy(){

        StringSelection stringSelection = new StringSelection(Core.defaultPassword);
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipBoard.setContents(stringSelection, null);
    }

    @FXML private void initialize() {

        Logger.getAnonymousLogger().info("encryption size limited to 128 : " + Core.encryptionLimit);

        defaultLbl.setText("Default password: " + Core.defaultPassword);

        load256Toggle.setDisable(Core.encryptionLimit);
    }

    private File getUserFile() {
        return userFile;
    }

    private void setUserFile(File userFile) {
        this.userFile = userFile;
    }
}
