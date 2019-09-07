import io.sentry.Sentry;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class DesktopController {

    private static Stage appWindow;
    @FXML private Label timeLbl;
    @FXML private ImageView wallpaper;

    @FXML private void calculator() throws IOException {

        final String nameOfApp = "Calculator";
        final boolean UASApp = false;

        loadApp(nameOfApp, UASApp);
    }

    @FXML private void settings() throws IOException {

        final String nameOfApp = "Settings";
        final boolean UASApp = false;

        loadApp(nameOfApp, UASApp);
    }

    @FXML private void terminal() throws IOException {

        final String nameOfApp = "Terminal";
        final boolean UASApp = true;

        if (!Core.getUserData().isGuest()) loadApp(nameOfApp, UASApp);
        else
            Notifications
                    .create()
                    .title("Warning")
                    .text("Terminal app disabled when guest")
                    .darkStyle()
                    .showWarning();
    }

    @FXML final void browser() throws IOException {

        final String nameOfApp = "Browser";
        final boolean UASApp = false;

        loadApp(nameOfApp, UASApp);
    }

    @FXML private void moviePlayer() throws IOException {

        final String nameOfApp = "MoviePlayer";
        final boolean UASApp = false;

        loadApp(nameOfApp, UASApp);
    }

    @FXML private void mail() throws IOException {

        final String nameOfApp = "EmailClient";
        final boolean UASApp = false;

        loadApp(nameOfApp, UASApp);
    }

    @FXML private void shutdown() throws IOException {

        final String nameOfApp = "Shutdown";
        final boolean UASApp = false;

        loadApp(nameOfApp, UASApp);
    }

    @FXML private void initialize() {

        if (Core.getUserData().isSentryReporting())
            Sentry.init("https://6db11d4c3f864632aa5b1932f6c80c82@sentry.io/220483");


        // resize wallpaper listener
        wallpaper.fitHeightProperty().bind(Core.getDesktop().heightProperty());
        wallpaper.fitWidthProperty().bind(Core.getDesktop().widthProperty());
        updateWallpaper();

        appWindow = new Stage();

        // clock thread
        Thread timeThread = new Thread(this::updateTime);
        timeThread.setPriority(Thread.currentThread().getPriority() - 1);
        timeThread.start();

        Core.testConnection();

        appWindow.setOnCloseRequest(e -> Logger.getAnonymousLogger().info("App window closed"));
    }

    private static void loadApp(String nameOfApp, boolean UASApp) throws IOException {

        if (UASApp) {

            UserAccountSecurity.UASLoadFXML(nameOfApp, true);
        } else {

            Core.loadAndTitle(nameOfApp, true);
        }
    }

    private void updateTime() {

        final short pollRate = 1_000;

        Logger.getAnonymousLogger().info("Starting clock thread");

        while (true) {

            DateFormat df = new SimpleDateFormat("HH:mm");
            Date date = new Date();

            Platform.runLater(() -> timeLbl.setText(df.format(date)));

            try {
                Thread.sleep(pollRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    final void updateWallpaper() {

        wallpaper.setImage(new Image(Core.getUserData().getWallpaperPath()));

        Logger.getAnonymousLogger().info("Wallpaper updated");
    }

    public static Stage getAppWindow() {
        return appWindow;
    }

    private static void setAppWindow(Stage appWindow) {
        DesktopController.appWindow = appWindow;
    }
}
