import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class TerminalController {

    @FXML private TextArea textArea;
    @FXML private JFXTextField commandField;

    @FXML private void execute() throws IOException, InterruptedException {

        Logger.getAnonymousLogger().info("Running command : " + textArea.getText());

        // build my command as a list of strings
        List<String> command = Arrays.asList(commandField.getText().trim().split(" "));

        // execute my command
        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(command);
        int result = commandExecutor.executeCommand();

        // get the output from the command
        StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();

        // print the output from the command
        textArea.setText(stdout + "\n" + stdout);
    }
}
