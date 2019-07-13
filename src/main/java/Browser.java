import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.CookieManager;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class Browser {

    @FXML private WebView browserView = new WebView();
    @FXML private JFXTextField urlField;
    WebEngine engine;
    private String homePage = Core.getUserData().getHomePageUrl();

    @FXML protected void goToWebSite() {

        if (!(urlField.getText().trim().equals(""))) {
            if (urlField.getText().contains(".com") || urlField.getText().contains(".gov") || urlField.getText().contains(".edu"))
                engine.load(urlField.getText());
            else engine.load("https://duckduckgo.com/?q=" + urlField.getText());
        }
    }

    @FXML private void back() {

        if (Core.getUserData().getMaxHistory() > 0) {
            try {
                engine.getHistory().go(-1);
            }catch (IndexOutOfBoundsException ignored){
                Logger.getAnonymousLogger().info("Reached end of browserView history : Browser.back()");
            }
        }
    }

    @FXML private void settings() throws IOException {

        Core.getUAS().UASLoadFXML("BrowserSettingsMenu");
    }

    @FXML private void refresh() {

        engine.reload();
    }

    @FXML private void forward() {

        if (Core.getUserData().getMaxHistory() > 0) {
            try {
                engine.getHistory().go(1);
            }catch (IndexOutOfBoundsException ignored){
                Logger.getAnonymousLogger().info("Reached end of browserView history : Browser.forward()");
            }
        }
    }

    @FXML private void home() {

        engine.load(homePage);
    }

    @FXML private void history() throws IOException {

        Core.getUAS().UASLoadFXML("BrowsingHistory");
    }

    @FXML private void initialize() {

        engine = browserView.getEngine();

        engine.load(homePage);

        // browserView security settings

        // home page
        homePage = Core.getUserData().getHomePageUrl();

        // max history
        engine.getHistory().setMaxSize(Core.getUserData().getMaxHistory());

        // javascript
        engine.setJavaScriptEnabled(Core.getUserData().isJavascriptAllow());

        // user agent
        engine.setUserAgent(Core.getUserData().getUserAgent());

        // cookies
        CookieManager manager = new CookieManager();
        manager.setCookiePolicy((uri, cookie) -> Core.getUserData().isCookiesAllowed());
        CookieManager.setDefault(manager);

        // pop-up
        if (!Core.getUserData().isPopUpAllow()) engine.createPopupHandlerProperty().unbind();

        // clear on exit
        if (Core.getUserData().isAutoDelete() && Core.getUserData().getMaxHistory() > 0)
            engine.getUserDataDirectory().deleteOnExit();

        // https changing
        if (Core.getUserData().isHttpsOnly()){
            engine.locationProperty().addListener((observable, oldValue, newValue) -> {
                if (engine.getLocation().trim().contains("http://")){

                    engine.load(engine.getLocation().trim().replace("http://","https://"));
                }
            });
        }
    }
}
