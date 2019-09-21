import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class HelpController {

    @FXML AnchorPane anchor;

    @FXML private void ok() {

        anchor.getScene().getWindow().hide();
    }
}
