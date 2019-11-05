import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class ChangeNameController {

    @FXML private JFXTextField nameField;
    @FXML private AnchorPane background;

    @FXML private void change() {

        Core.getUserData().setName(nameField.getText());

        Logger.getAnonymousLogger().info("Named changed");

        background.getScene().getWindow().hide();
    }

    @FXML private void initialize(){

        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());
    }
}
