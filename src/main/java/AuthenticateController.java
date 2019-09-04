import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class AuthenticateController extends UserAccountSecurity{

    @FXML private JFXPasswordField passwordField;

    @FXML private void ok(){

        if (passwordField.getText().equals(Core.getUserData().getPassword())){

            Core.getSecurity().setVerified();
            Core.getSecurity().getSecurityWindow().close();
        }
    }
}
