import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.io.IOException;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class ShutdownController {

    @FXML private HBox background;

    @FXML private void initialize(){

    }

    @FXML private void cancel(){

        background.getScene().getWindow().hide();
    }

    @FXML private void shutdown(){

        Core.shutdown();
    }

    @FXML private void signOut() throws IOException {
        if (!Core.getUserData().isGuest()) SignOutController.signOut(true);
        else SignOutController.signOut(false);
    }
}
