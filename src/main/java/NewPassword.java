import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

import java.io.IOException;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class NewPassword {

    @FXML private JFXTextField accountField, passwordField;

    @FXML private void newPassword() {

        Account account = new Account();
        account.accountPassword = passwordField.getText();
        account.accountUsername = accountField.getText();

        Core.getUserData().getPasswords().add(account);
    }

    @FXML private void back() throws IOException {

        Core.loadAndTitle("PasswordManager", true);
    }
}
