import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class Core extends Application {

    static boolean encryptionLimit = true;
    static boolean schoolMode = false;
    static Configuration config;
    static String defaultPassword = versionChecksum();
    private static final Configurations configs = new Configurations();
    private static final String version = "Pre-Alpha";
    private static User userData;
    private static UserAccountSecurity security = new UserAccountSecurity();
    private static Stage desktop;

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

        Logger.getAnonymousLogger().info("Booting up...");

        encryptionLimit = Cipher.getMaxAllowedKeyLength("RC5")<256;

        if (new File("school.properties").exists()) {
            schoolMode = true;
            try {
                config = configs.properties(new File("school.properties"));
            } catch (ConfigurationException e) {
                e.printStackTrace();
            }
            defaultPassword = config.getString("passwordStudent", versionChecksum());
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        desktop = primaryStage;

        Parent loginScene = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));

        // window config
        desktop.setScene(new Scene(loginScene));
        desktop.setTitle("Jartop - " + version);
        desktop.setOnCloseRequest(e -> shutdown());
        desktop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        desktop.setFullScreen(true);
        desktop.show();
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

    static void loadAndTitle(String fxmlName, boolean isApp) throws IOException {

        final String suffix = ".fxml";
        final String folder = "fxml/";

        Parent pane = FXMLLoader.load(Core.class.getResource(folder + fxmlName + suffix));

        // app window vs desktop window
        if (isApp) {
            int id = DesktopController.newWindow();
            DesktopController.appWindows.get(id).setTitle(fxmlName);
            DesktopController.appWindows.get(id).setScene(new Scene(pane));
            DesktopController.appWindows.get(id).initStyle(StageStyle.UTILITY);
            DesktopController.appWindows.get(id).show();
            DesktopController.appWindows.get(id).toFront();
            Logger.getAnonymousLogger().info("Loading app : " + fxmlName);
        } else {
            desktop.getScene().setRoot(pane);
            Logger.getAnonymousLogger().info("Loading scene : " + fxmlName);
        }
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

    static Stage getDesktop() {
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

    protected static UserAccountSecurity getSecurity() {
        return security;
    }

    static void setSecurity(UserAccountSecurity security) {
        Core.security = security;
    }
}
