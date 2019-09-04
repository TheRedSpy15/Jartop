import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class ChangePasswordController {

    @FXML private JFXTextField passwordField;
    @FXML private AnchorPane background;

    @FXML private void initialize(){

        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());
    }

    @FXML private void change() throws
            IOException, UnknownResponseCode, BadRequestException, NoSuchAlgorithmException, RateLimitException {

        final byte minRecommendedLength = 8;

        Core.getUserData().setPassword(passwordField.getText());

        Notifications
                .create()
                .title("Notice")
                .text("Password changed")
                .darkStyle()
                .showInformation();

        // blank
        if (passwordField.getText().trim().isEmpty()) {

            Logger.getAnonymousLogger().warning("New password is blank - zero security");

            Notifications
                    .create()
                    .title("Warning")
                    .text("New password is blank - zero security")
                    .darkStyle()
                    .showWarning();
        }

        // not blank
        else {
            // Pwned password
            Core.testConnection(); // needed, cause being offline will cause HIBP to break
            if (passwordField.getText().length() > 8) {
                if (PwnedPasswords.pwnByRange(passwordField.getText()) && User.internetConnection) {

                    Notifications
                            .create()
                            .title("Warning")
                            .text("New password found in Pwned database - please consider a different one")
                            .darkStyle()
                            .showWarning();
                }
            } else if (PwnedPasswords.pwnPassword(passwordField.getText())) {

                Notifications
                        .create()
                        .title("Warning")
                        .text("New password found in Pwned database - please consider a different one")
                        .darkStyle()
                        .showWarning();
            }

            // length
            if (passwordField.getText().length() < minRecommendedLength) {

                Logger.getAnonymousLogger().warning(
                        "New password shorter than " + minRecommendedLength + " characters - please consider a longer one"
                );

                Notifications
                        .create()
                        .title("Warning")
                        .text("New password shorter than 8 characters - please consider a longer one")
                        .darkStyle()
                        .showWarning();
            }
        }

        Parent settingsPane = FXMLLoader.load(Core.class.getResource("Settings.fxml"));
        DesktopController.getAppWindow().setScene(new Scene(settingsPane));
    }
}
