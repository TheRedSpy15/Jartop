import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

class FileSystem {

    static JartopFile findFile() {
        Stage stage = new Stage(StageStyle.UTILITY);
        AtomicReference<JartopFile> rFile = new AtomicReference<>(new JartopFile());
        VBox vbox = new VBox();
        for (JartopFile file : Core.getUserData().getFileSystem()) {
            JFXButton button = new JFXButton(file.getName());
            button.setOnAction(e -> {
                rFile.set(file);
                stage.close();
            });
            vbox.getChildren().add(button);
        }
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Files");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        stage.toFront();
        return rFile.get();
    }

    static void displayFiles() {

    }

    static void importFile() throws IOException {
        File file;
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "All Files",
                        "*.*")
        );

        file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            JartopFile jfile = new JartopFile();
            jfile.setName(file.getName());
            jfile.setData(Files.readAllBytes(Paths.get(file.toURI())));
            Core.getUserData().getFileSystem().add(jfile);
        }
    }

    static void exportFile(List<JartopFile> files) {

    }
}
