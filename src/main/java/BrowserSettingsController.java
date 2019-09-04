import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class BrowserSettingsController extends BrowserController {

    // Defaults
    static final byte defaultHistory = 100;

    static final String defaultHomePage =
            "https://duckduckgo.com/?kae=d&k1=1&kak=-1&kax=-1&kaq=-1&kap=-1&kao=-1&kg=p";
    static final String defaultUserAgent =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/604.1 (KHTML, like Gecko) JavaFX/8.0 Safari/604.1";

    static final boolean defaultJavaScript = true;
    static final boolean defaultPopUp = false;
    static final boolean defaultHttps = false;
    static final boolean defaultClearExit = false;
    static final boolean defaultCookies = true;
    private static final boolean defaultTor = false;

    // JavaFX
    @FXML private JFXToggleButton
            javaScriptToggle,
            popUpToggle,
            httpsToggle,
            clearExitToggle,
            cookieToggle,
            torToggle;
    @FXML private JFXSlider historySlider;
    @FXML private JFXTextField homePageField, agentField;
    @FXML private AnchorPane background;

    @FXML private void back() throws IOException {

        new DesktopController().browser();
    }

    @FXML private void apply() throws IOException {

        if (!homePageField.getText().trim().isEmpty())
            Core.getUserData().setHomePageUrl(homePageField.getText());

        Core.getUserData().setMaxHistory((int) historySlider.getValue());
        Core.getUserData().setPopUpAllow(popUpToggle.selectedProperty().getValue());
        Core.getUserData().setJavascriptAllow(javaScriptToggle.selectedProperty().getValue());
        Core.getUserData().setUserAgent(agentField.getText());
        Core.getUserData().setHttpsOnly(httpsToggle.selectedProperty().getValue());
        Core.getUserData().setAutoDelete(clearExitToggle.selectedProperty().getValue());
        Core.getUserData().setCookiesAllowed(cookieToggle.selectedProperty().getValue());
        Core.getUserData().setTorConnection(torToggle.selectedProperty().getValue());

        back();
    }

    @FXML private void reset(){

        // max history & homepage url & user agent
        homePageField.setText(defaultHomePage);
        agentField.setText(defaultUserAgent);
        historySlider.setValue(defaultHistory);

        // tor connection
        torToggle.selectedProperty().setValue(defaultTor);

        // javascript
        javaScriptToggle.selectedProperty().setValue(defaultJavaScript);

        // pop-up
        popUpToggle.selectedProperty().setValue(defaultPopUp);

        // https only
        httpsToggle.selectedProperty().setValue(defaultHttps);

        // clear on exit
        clearExitToggle.selectedProperty().setValue(defaultClearExit);

        // cookies
        cookieToggle.selectedProperty().setValue(defaultCookies);
    }

    @FXML private void initialize(){

        // background
        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());

        // max history & homepage url & user agent
        homePageField.setText(Core.getUserData().getHomePageUrl());
        agentField.setText(Core.getUserData().getUserAgent());
        historySlider.setValue(Core.getUserData().getMaxHistory());

        // tor connection
        torToggle.selectedProperty().setValue(Core.getUserData().isTorConnection());

        // javascript
        javaScriptToggle.selectedProperty().setValue(Core.getUserData().isJavascriptAllow());

        // pop-up
        popUpToggle.selectedProperty().setValue(Core.getUserData().isPopUpAllow());

        // https only
        httpsToggle.selectedProperty().setValue(Core.getUserData().isHttpsOnly());

        // clear on exit
        clearExitToggle.selectedProperty().setValue(Core.getUserData().isAutoDelete());

        // cookies
        cookieToggle.selectedProperty().setValue(Core.getUserData().isCookiesAllowed());
    }
}
