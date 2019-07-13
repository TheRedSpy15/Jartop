import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class SignOut {

    @FXML private AnchorPane background;

    @FXML private void back() throws IOException {

        String nameOfApp = "Settings";

        Parent settingsPane = FXMLLoader.load(Core.class.getResource(nameOfApp + ".fxml"));

        Desktop.getAppWindow().setScene(new Scene(settingsPane));
        Desktop.getAppWindow().show();
        Desktop.getAppWindow().setTitle(nameOfApp);
    }

    @FXML private void signOut() throws IOException {

        signOut(true);
    }

    final void signOut(boolean saveOnOut) throws IOException {

        if (saveOnOut) Core.nonGuestSave();

        Parent loginScene = FXMLLoader.load(Core.class.getResource("login.fxml"));

        if (Desktop.getAppWindow().isShowing()) Desktop.getAppWindow().close();
        if (Core.getUAS().getSecurityWindow().isShowing()) Core.getUAS().getSecurityWindow().close();

        Core.getDesktop().setScene(new Scene(loginScene));

        Core.setUserData(new User());
        Core.setUAS(new UserAccountSecurity());

        Logger.getAnonymousLogger().info("Signed out");

        Notifications
                .create()
                .title("Info")
                .text("Signed out")
                .darkStyle()
                .showInformation();
    }

    @FXML private void initialize(){

        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());
    }
}
