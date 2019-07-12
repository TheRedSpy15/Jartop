import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class Core extends Application {

    /*
        PLANS

    * BROWSER
    * - ad-blocker (Long term)
    * - history button - needs finished
    * - option to change search engine
    * - manually clear cookies and history button - separate
    *
    * MOVIE PLAYER
    * - time-line
    * - resize support
    *
    * TERMINAL
    * -
    *
    * PASSWORD MANAGER
    * - needs finished - first draft
    *
    * EMAIL CLIENT
    * - way to receive emails
    * - contact button to quickly select an address to send to
    * - make more responsive by removing freeze when sending
    * - better exception handling
    * - multiple recipients support
    * - default buttons
    *
    * LOGIN
    * - need a confirm dialog to overwrite original file when making a new one
    *   assuming one with the same name exists. dialog should also have a note
    *   with a suggestion to rename the file, and an option to bleach it before
    *   overwriting it
    * - optional remember last loaded user file
    *
    * CALCULATOR
    * - multiplication
    * - division
    * - subtraction
    *
    * NEW APPS
    * - chat app
    * - paint app
    * - word app
    * - how to use app
    * - music app
    * - calender app
    * - password manager app - needs finished
    * - contact app
    * - check list app
    * - photo viewer app
    * - photo editor app
    * - world map app - controlsfx
    * - printing app
    *
    * SETTINGS
    * - change file name option when changing username - needs finished
    * - replace proxies, with tor
    * - volume slider - needs finished
    * - wallpaper preview does not move with anchorpane
    * - ip info on networking tab
    *
    * DESKTOP
    * - desktop tabs in replace of multiple stages (Long term)
    * - auto resizing wallpaper and icons - basically a listener
    *
    * BUGS
    * - changing wallpaper changes preview, not wallpaper itself
    * - enabling clear on exit throws exception
    *
    * BACK END
    * - unit tests
    * - scaling support
    * - move code to a package
    * - log4j - needed for sentry
    * - custom app support (Long term)
    * - auto saving along with frequency slider
    * - aspectj or observers
    * - "everything else" section on trello page
    * - update to java 9
     */

    static boolean encryptionLimit = true;

    static final String defaultPassword = versionChecksum();

    private static final String version = "Pre-Alpha";

    private static User userData = new User();
    private static UserAccountSecurity UAS = new UserAccountSecurity();

    private static Stage desktop;

    public static void main(String[] args) throws NoSuchAlgorithmException {

        Logger.getAnonymousLogger().info("Booting up...");

        encryptionLimit = Cipher.getMaxAllowedKeyLength("RC5")<256;

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        setDesktop(primaryStage);

        Parent loginScene = FXMLLoader.load(getClass().getResource("login.fxml"));

        // window config
        getDesktop().setScene(new Scene(loginScene));
        getDesktop().setTitle("Jartop - " + version);
        getDesktop().setOnCloseRequest(e -> shutdown());

        getDesktop().initStyle(StageStyle.TRANSPARENT);
        getDesktop().show();
    }

    static void shutdown() {

        nonGuestSave();

        Logger.getAnonymousLogger().info("Shutting down...");

        System.exit(0);
    }

    static void nonGuestSave() {

        if (!Core.getUserData().isGuest()) {
            try {
                try {
                    Core.getUserData().saveData();
                } catch (NoSuchPaddingException |
                        NoSuchAlgorithmException |
                        IllegalBlockSizeException |
                        InvalidKeyException e1) {
                    e1.printStackTrace();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    static void loadAndTitle(String fxmlName) throws IOException {

        final String suffix = ".fxml";

        Parent pane = FXMLLoader.load(Core.class.getResource(fxmlName + suffix));

        Desktop.getAppWindow().setTitle(fxmlName);
        Desktop.getAppWindow().setScene(new Scene(pane));

        Desktop.getAppWindow().show();

        Logger.getAnonymousLogger().info("Loading app : " + fxmlName);
    }

    private static String versionChecksum() {

        return String.valueOf(Hashing.adler32().hashString(version, Charsets.UTF_8));
    }

    static void testConnection() {

        // if changed, make sure to use https
        final String addressToTest = "https://duckduckgo.com";

        try {
            URL testURL = new URL(addressToTest);
            testURL.openConnection();
            User.internetConnection = true;
            Logger.getAnonymousLogger().info("Internet test passed");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            User.internetConnection = false;
            Logger.getAnonymousLogger().warning("Internet test failed - likely offline");
        }
    }

    protected static Stage getDesktop() {
        return desktop;
    }

    protected static void setDesktop(Stage desktop) {
        Core.desktop = desktop;
    }

    protected static User getUserData() {
        return userData;
    }

    static void setUserData(User userData) {
        Core.userData = userData;
    }

    protected static UserAccountSecurity getUAS() {
        return UAS;
    }

    static void setUAS(UserAccountSecurity UAS) {
        Core.UAS = UAS;
    }
}
