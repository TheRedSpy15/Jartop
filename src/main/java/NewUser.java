import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

class NewUser extends LoginController {

    static void create() throws IOException {

        File userFile = new File(System.getProperty("user.name") + User.saveExtension);

        Core.setUserData(new User());
        Core.getUserData().setGuest(false);
        Core.getUserData().setUserFile(userFile);
        Core.getUserData().setName(Core.getUserData().getUserFile().getName().replace(".jtop",""));

        if (Core.schoolMode) {
            Core.getUserData().setPasswordSchool(Core.config.getString("passwordSchool", ""));
            Core.getUserData().setPassword(Core.config.getString("passwordStudent", "student"));
            Core.getUserData().setName(Core.config.getString("nameStudent", "student"));
            Core.getUserData().setSentryReporting(Core.config.getBoolean("errorReporting", true));
            Core.getUserData().setWallpaperImageData(Files.readAllBytes(Paths.get(Core.config.getString("wallpaper"))));
        }

        Core.loadAndTitle("Desktop", false);

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
