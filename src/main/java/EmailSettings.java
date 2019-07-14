import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class EmailSettings extends EmailClient {

    @FXML private JFXTextField addressField, passwordField, providerField, portField;
    @FXML private JFXToggleButton sslToggle;
    @FXML private AnchorPane background;

    @FXML private void apply() throws IOException {

        Core.getUserData().setEmailAddress(addressField.getText());
        Core.getUserData().setEmailPassword(passwordField.getText());
        Core.getUserData().setHostName(providerField.getText());
        Core.getUserData().setSslConnection(sslToggle.selectedProperty().getValue());
        Core.getUserData().setSmtpPort(Short.parseShort(portField.getText().trim()));

        back();
    }

    @FXML private void back() throws IOException {

        Core.loadAndTitle("Settings", true);
    }

    @FXML private void initialize(){

        // background
        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());

        // fields
        addressField.setText(Core.getUserData().getEmailAddress());
        passwordField.setText(Core.getUserData().getEmailPassword());
        providerField.setText(Core.getUserData().getHostName());
        sslToggle.selectedProperty().setValue(Core.getUserData().isSslConnection());
        portField.setText(String.valueOf(Core.getUserData().getSmtpPort()));
    }
}
