import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class Settings {

    @FXML private Label nameLbl, guestLbl, statusLbl, hashLbl;
    @FXML private JFXToggleButton sentryToggle, aes256Toggle;
    @FXML private ImageView wallpaperPreview;
    @FXML private JFXSlider volumeSlider;
    @FXML private JFXColorPicker colorPicker;
    @FXML private JFXToggleButton torToggle;
    @FXML private TabPane background;
    @FXML private JFXButton createBtn, bleachBtn;

    @FXML private void changeWallpaper() {

        File file;

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select an image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "Image Files (.png, .jpg, .gif, .bmp)",
                        "*.png", "*.jpg", "*.gif", "bmp"),
                new FileChooser.ExtensionFilter(
                        "All Files",
                        "*.*")
        );

        file = fileChooser.showOpenDialog(Desktop.getAppWindow());
        if (file != null) {
            Core.getUserData().setWallpaperPath(file.getPath());
            wallpaperPreview.setImage(new Image(Core.getUserData().getWallpaperPath()));
            new Desktop().updateWallpaper();
        }
    }

    @FXML private void changeName() throws IOException {

        Core.getUAS().UASLoadFXML("ChangeName", true);
    }

    @FXML private void changePassword() throws IOException {

        Core.getUAS().UASLoadFXML("ChangePassword", true);
    }

    @FXML private void browserSettings() throws IOException {

        Core.getUAS().UASLoadFXML("BrowserSettingsMenu", true);
    }

    @FXML private void emailSettings() throws IOException {

        Core.getUAS().UASLoadFXML("EmailSettings", true);
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

        Core.loadAndTitle("NewUser", true);
    }

    @FXML private void bleach() throws IOException {

        Core.getUAS().UASLoadFXML("Bleach", true);
    }

    @FXML private void toggleTor() { // change to tor

        Core.getUserData().setTorConnection(torToggle.selectedProperty().getValue());

        if (Core.getUserData().isTorConnection()) {

            // need to connect to tor here
        } else {

            // need to disable tor here
        }
    }

    @FXML private void testConnection() {

        Core.testConnection();

        statusLbl.setText("Status: " + User.internetConnection);
    }

    @FXML private void initialize() throws IOException {

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

        // wallpaper
        wallpaperPreview.setImage(new Image(Core.getUserData().getWallpaperPath()));

        // volume
        // need a way to set it between 0 - 100 in GUI, but between 0.0 - 1.0 in code

        // sentry
        sentryToggle.selectedProperty().setValue(Core.getUserData().isSentryReporting());
        sentryToggle.setOnAction(
                e -> Core.getUserData().setSentryReporting(sentryToggle.selectedProperty().getValue())
        );

        // aes-256
        aes256Toggle.setDisable(Core.encryptionLimit);
        aes256Toggle.selectedProperty().setValue(Core.getUserData().isEncryptWith256());
        aes256Toggle.setOnAction(
                e -> Core.getUserData().setEncryptWith256(aes256Toggle.selectedProperty().getValue())
        );

        testConnection();
    }
}
