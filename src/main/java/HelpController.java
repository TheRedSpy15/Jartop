import javafx.fxml.FXML;

public class HelpController {

    @FXML private void ok() {

        DesktopController.getAppWindow().close();
    }
}
