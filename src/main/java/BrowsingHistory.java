import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;

import java.io.IOException;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class BrowsingHistory extends Browser{

    @FXML private JFXTextArea historyArea;

    @FXML private void back() throws IOException {

        new Desktop().browser();
    }

    @FXML private void clear(){

        engine.getHistory().getEntries().clear();

        updateHistoryArea();
    }

    @FXML private void initialize(){

        updateHistoryArea();
    }

    private void updateHistoryArea(){

        if (Core.getUserData().getMaxHistory() > 0){

            historyArea.appendText(engine.getHistory().getEntries().toString());
        }else {

            historyArea.appendText("History is disabled");
        }
    }
}
