import com.google.common.annotations.Beta;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

@Beta
public class PasswordManagerController {

    // need service column
    @FXML private TableColumn accounts, passwords;
    @FXML private TableView table;

    @FXML private void create() throws IOException {

        Core.loadAndTitle("NewPassword", true);
    }

    @FXML private void initialize(){

        if (Core.getUserData().getPasswords() != null){

            for (Account i : Core.getUserData().getPasswords()){

                // need to add data to table
            }
        }
    }
}
