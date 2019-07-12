import javafx.fxml.FXML;

import java.io.IOException;

public class Bleach {

    @FXML private void bleach() throws IOException {

        Core.getUAS().secureDelete(Core.getUserData().getUserFile(), true);
    }
}
