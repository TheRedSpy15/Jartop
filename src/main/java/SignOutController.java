import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class SignOutController {

    @FXML private AnchorPane background;

    @FXML private void back() throws IOException {

        Core.loadAndTitle("Settings", true);
    }

    @FXML private void signOut() throws IOException {

        signOut(true);
    }

    static void signOut(boolean saveOnOut) throws IOException {

        if (saveOnOut) Core.nonGuestSave();

        if (DesktopController.getAppWindow().isShowing()) DesktopController.getAppWindow().close();
        if (Core.getSecurity().getSecurityWindow().isShowing()) Core.getSecurity().getSecurityWindow().close();

        Core.loadAndTitle("Login", false);

        Core.setUserData(new User());
        Core.setSecurity(new UserAccountSecurity());

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
