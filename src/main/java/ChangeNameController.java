import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class ChangeNameController {

    @FXML private JFXTextField nameField;
    @FXML private JFXToggleButton fileNameToggle;
    @FXML private AnchorPane background;

    @FXML private void change() throws IOException {

        Core.getUserData().setName(nameField.getText());

        Logger.getAnonymousLogger().info("Named changed");

        if(fileNameToggle.selectedProperty().getValue()) changeFileName();

        Core.loadAndTitle("Settings", true);
    }

    private void changeFileName(){

        // Creates a new file with new name, but does not replace old one
        Core.getUserData().getUserFile().renameTo(new File(nameField.getText() + User.saveExtension));
    }

    @FXML private void initialize(){

        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());
    }
}
