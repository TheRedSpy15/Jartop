import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class Shutdown {

    @FXML private AnchorPane background;

    @FXML private void initialize(){

        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());
    }

    @FXML private void back(){

        Desktop.getAppWindow().close();
    }

    @FXML private void shutdown(){

        Core.shutdown();
    }
}
