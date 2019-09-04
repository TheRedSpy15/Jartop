import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.fxml.FXML;
import org.controlsfx.control.Notifications;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class TerminalController {

    @FXML private JFXTextArea textArea;

    private void executeCommand() {

        String command = textArea.getText();

        StringBuilder output = new StringBuilder();

        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            Logger.getAnonymousLogger().info("Finished command");

        } catch (Exception e) {
            e.printStackTrace();

            Notifications
                    .create()
                    .title("Error")
                    .text("Command failed")
                    .darkStyle()
                    .showError();
        }

        Platform.runLater(() -> textArea.appendText(output.toString()));
    }

    @FXML private synchronized void execute(){

        Thread commandThread = new Thread(this::executeCommand);
        commandThread.setPriority(10);
        commandThread.start();

        Logger.getAnonymousLogger().info("Running command : " + textArea.getText());
    }

    @FXML private synchronized void clear(){

        textArea.clear();
    }
}
