import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FilesController {

    @FXML JFXListView<Label> listView;

    @FXML private void initialize() {

        for(JartopFile file : Core.getUserData().getFileSystem()) {
            Label label = new Label(file.getName());
            listView.getItems().add(label);
        }
    }
}
