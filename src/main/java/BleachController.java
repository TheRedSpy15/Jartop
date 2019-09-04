import javafx.fxml.FXML;

import java.io.IOException;

public class BleachController {

    @FXML private void bleach() throws IOException {

        Core.getSecurity().secureDelete(Core.getUserData().getUserFile(), true);
    }
}
