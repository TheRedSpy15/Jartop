import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.SecureRandom;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

class UserAccountSecurity {

    private boolean verified = false;

    private Stage securityWindow;

    private byte failedAttempts = 0;

    UserAccountSecurity(){

        securityWindow = new Stage();

        securityWindow.initModality(Modality.APPLICATION_MODAL);
        securityWindow.setTitle("UAS");
        securityWindow.setAlwaysOnTop(true);
    }

    private void authenticate() throws IOException {

        Parent authenticatePane = FXMLLoader.load(Core.class.getResource("fxml/Authenticate.fxml"));

        securityWindow.setScene(new Scene(authenticatePane));
        securityWindow.showAndWait();
    }

    final synchronized void secureDelete(File file, boolean signOut) throws IOException {

        if (file.exists()) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws")) {

                long length = file.length();
                SecureRandom random = new SecureRandom();
                randomAccessFile.seek(0);
                randomAccessFile.getFilePointer();
                byte[] data = new byte[64];
                int position = 0;
                while (position < length) {

                    random.nextBytes(data);
                    randomAccessFile.write(data);
                    position += data.length;
                }
            } finally {

                if(file.delete()) {
                    Logger.getAnonymousLogger().warning("File bleached");

                    Notifications
                            .create()
                            .title("Warning")
                            .text("File bleached")
                            .darkStyle()
                            .showError();
                }
            }
        }

        if (signOut) SignOutController.signOut(false);
    }

    static void UASLoadFXML(String fxmlName) throws IOException {

        if (Core.getSecurity().verified || // load
                Core.getUserData().isGuest() ||
                Core.getUserData().getPassword().trim().isEmpty()){

            Core.loadAndTitle(fxmlName, true);
        } else {

            Core.getSecurity().authenticate();

            if (Core.getSecurity().verified){ // load

                Core.loadAndTitle(fxmlName, true);
            }
        }
    }

    private boolean isVerified() {
        return verified;
    }

    final void setVerified() {
        this.verified = true;
    }

    final Stage getSecurityWindow() {
        return securityWindow;
    }

    private void setSecurityWindow(Stage securityWindow) {
        this.securityWindow = securityWindow;
    }

    final byte getFailedAttempts() {
        return failedAttempts;
    }

    final void setFailedAttempts(byte failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}
