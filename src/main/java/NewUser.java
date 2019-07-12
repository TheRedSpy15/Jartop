import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

class NewUser extends Login {

    final void create() throws IOException {

        File userFile = new File(System.getProperty("user.name") + User.saveExtension);

        Core.getUserData().setGuest(false);
        Core.getUserData().setUserFile(userFile);
        Core.getUserData().setName(Core.getUserData().getUserFile().getName().replace(".ser",""));

        Parent desktopScene = FXMLLoader.load(Core.class.getResource("Desktop.fxml"));
        Core.getDesktop().setScene(new Scene(desktopScene));

        Core.nonGuestSave();

        Logger.getAnonymousLogger().info("New user created");

        Notifications
                .create()
                .title("Notice")
                .text("New user created")
                .darkStyle()
                .showInformation();
    }
}
