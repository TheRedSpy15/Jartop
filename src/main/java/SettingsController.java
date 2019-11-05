import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.Notifications;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class SettingsController {

    @FXML private Label nameLbl, guestLbl, hashLbl;
    @FXML private JFXToggleButton sentryToggle, aes256Toggle;
    @FXML private ImageView wallpaperPreview;
    @FXML private JFXSlider volumeSlider;
    @FXML private JFXColorPicker colorPicker;
    @FXML private TabPane background;
    @FXML private JFXButton createBtn, bleachBtn, changeNameBtn, changePasswordBtn;

    @FXML private void changeWallpaper() throws IOException {

        JartopFile file = FileSystem.findFileGUI();

        if (file != null) {
            Core.getUserData().setWallpaperImageData(file.getData());
            wallpaperPreview.setImage(new Image(new ByteArrayInputStream(Core.getUserData().getWallpaperImageData())));
            Core.loadAndTitle("Desktop", false);
        }
    }

    @FXML private void changeName() throws IOException {

        UserAccountSecurity.UASLoadFXML("ChangeName");
    }

    @FXML private void changePassword() throws IOException {

        UserAccountSecurity.UASLoadFXML("ChangePassword");
    }

    @FXML private void browserSettings() throws IOException {

        UserAccountSecurity.UASLoadFXML("BrowserSettingsMenu");
    }

    @FXML private void emailSettings() throws IOException {

        UserAccountSecurity.UASLoadFXML("EmailSettings");
    }

    @FXML private void changeColor() {

        Core.getUserData().setPreferredColor(colorPicker.getValue().toString().replace("0x","#"));
        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());

        Logger.getAnonymousLogger().info("Background color changed");
    }

    @FXML private void signOut() throws IOException {

        Core.loadAndTitle("SignOut", true);
    }

    @FXML private void createAccount() throws IOException {

        Core.getUserData().setGuest(false);
        Core.loadAndTitle("ChangeName", true);

        background.getScene().getWindow().hide();
    }

    @FXML private void bleach() throws IOException {

        UserAccountSecurity.UASLoadFXML("Bleach");
    }

    @FXML private void initialize() throws IOException {

        // guest checks
        if (Core.getUserData().isGuest()) {
            changeNameBtn.setDisable(true);
            bleachBtn.setDisable(true);
            aes256Toggle.setDisable(true);
        }

        // school checks
        if (Core.schoolMode) {
            changePasswordBtn.setDisable(!Core.config.getBoolean("allowChangePassword", false));
            bleachBtn.setDisable(!Core.config.getBoolean("allowBleach", false));
            changeNameBtn.setDisable(!Core.config.getBoolean("allowChangeName", false));
        }

        // background
        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());

        // labels
        nameLbl.setText("Name: " + Core.getUserData().getName());
        guestLbl.setText("Guest: " + Core.getUserData().isGuest());

        // account hashing
        if (!Core.getUserData().isGuest())
            hashLbl.setText("Account Hash: " +
                    Files.asByteSource(
                            Core.getUserData().getUserFile()).hash(Hashing.adler32())
            );

        // buttons
        createBtn.setDisable(!Core.getUserData().isGuest());
        bleachBtn.setDisable(Core.getUserData().isGuest());
        changePasswordBtn.setDisable(Core.getUserData().isGuest());

        // wallpaper
        wallpaperPreview.setImage(new Image(new ByteArrayInputStream(Core.getUserData().getWallpaperImageData())));

        // volume
        // need a way to set it between 0 - 100 in GUI, but between 0.0 - 1.0 in code

        // sentry
        sentryToggle.selectedProperty().setValue(Core.getUserData().isSentryReporting());
        sentryToggle.setOnAction(
                e -> {
                    Core.getUserData().setSentryReporting(sentryToggle.selectedProperty().getValue());
                    Notifications
                            .create()
                            .title("Notice")
                            .text("Will take effect on restart")
                            .darkStyle()
                            .showInformation();
                }
        );

        // aes-256
        if (!Core.getUserData().isGuest()) aes256Toggle.setDisable(Core.encryptionLimit);
        aes256Toggle.selectedProperty().setValue(Core.getUserData().isEncryptWith256());
        aes256Toggle.setOnAction(
                e -> Core.getUserData().setEncryptWith256(aes256Toggle.selectedProperty().getValue())
        );
    }
}
