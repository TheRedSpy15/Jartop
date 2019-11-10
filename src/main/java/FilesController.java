import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

public class FilesController {

    @FXML JFXListView<Label> listView;

    @FXML private void exportFile() {

    }

    @FXML private void importFile() throws IOException {
        FileSystem.importFile();
        populateView();
    }

    private void createLabel(JartopFile file) {
        Label label = new Label(file.getName());

        if (label.getText().contains(".png") || label.getText().contains(".bmp") || label.getText().contains(".gif") || label.getText().contains(".jpeg") || label.getText().contains(".jpg")) {
            label.setOnMouseClicked(e -> {
                if(e.getButton().equals(MouseButton.PRIMARY)){
                    if(e.getClickCount() == 2){
                        FileSystem.findFile(label.getText());
                        Image image = new Image(new ByteArrayInputStream(Objects.requireNonNull(FileSystem.findFile(label.getText())).getData()));
                        ImageView view = new ImageView(image);
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UTILITY);
                        StackPane pane = new StackPane();
                        pane.getChildren().add(view);
                        Scene scene = new Scene(pane);
                        stage.setScene(scene);
                        stage.show();
                        stage.toFront();
                    }
                }
            });
        }

        listView.getItems().add(label);
    }

    @FXML private void initialize() {
        populateView();
    }

    private void populateView() {
        listView.getItems().clear();
        short fileCount = 0;
        for(JartopFile file : Core.getUserData().getFileSystem()) {
            createLabel(file);
            fileCount+=1;
        }
        if (fileCount == 0) {
            listView.getItems().add(new Label("No files found"));
        }
    }
}
