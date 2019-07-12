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

class UserAccountSecurity extends User {

    private boolean verified = false;

    private Stage securityWindow;

    private byte failedAttempts = 0;

    UserAccountSecurity(){

        setSecurityWindow(new Stage());

        getSecurityWindow().initModality(Modality.APPLICATION_MODAL);
        getSecurityWindow().setTitle("UAS");
    }

    private void authenticate() throws IOException {

        Parent authenticatePane = FXMLLoader.load(Core.class.getResource("Authenticate.fxml"));

        getSecurityWindow().setScene(new Scene(authenticatePane));
        getSecurityWindow().showAndWait();
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

                file.delete();

                Logger.getAnonymousLogger().warning("File bleached");

                Notifications
                        .create()
                        .title("Warning")
                        .text("File bleached")
                        .darkStyle()
                        .showError();
            }
        }

        if (signOut) new SignOut().signOut(false);
    }

    final void UASLoadFXML(String fxmlName) throws IOException {

        if (Core.getUAS().isVerified() || // load
                Core.getUserData().isGuest() ||
                Core.getUserData().getPassword().trim().equals("")){

            Core.loadAndTitle(fxmlName);
        } else {

            Core.getUAS().authenticate();

            if (Core.getUAS().isVerified()){ // load

                Core.loadAndTitle(fxmlName);
            }
        }
    }

    private boolean isVerified() {
        return verified;
    }

    protected final void setVerified() {
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
