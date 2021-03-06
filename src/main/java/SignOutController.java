import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

        if (!Core.getUserData().isGuest()) signOut(true);
        else signOut(false);
    }

    static void signOut(boolean saveOnOut) throws IOException {

        if (saveOnOut) Core.nonGuestSave();

        for (Stage stage : DesktopController.appWindows) if (stage.isShowing()) stage.close();
        if (Core.getSecurity().getSecurityWindow().isShowing()) Core.getSecurity().getSecurityWindow().close();

        Core.loadAndTitle("Login", false);

        Logger.getAnonymousLogger().info("Signed out");
    }

    @FXML private void initialize(){

        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());
    }
}
