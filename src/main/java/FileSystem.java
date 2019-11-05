import com.jfoenix.controls.JFXButton;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    static JartopFile findFileGUI() {
        Stage stage = new Stage(StageStyle.UTILITY);
        AtomicReference<JartopFile> rFile = new AtomicReference<>(new JartopFile());
        VBox vbox = new VBox();
        short fileCount = 0;
        for (JartopFile file : Core.getUserData().getFileSystem()) {
            JFXButton button = new JFXButton(file.getName());
            button.setOnAction(e -> {
                rFile.set(file);
                stage.close();
            });
            vbox.getChildren().add(button);
            fileCount+=1;
        }
        if(fileCount == 0) {
            vbox.getChildren().add(new Label("No files found"));
        }
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Files");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        return rFile.get();
    }

    static JartopFile findFile(String name) {
        for (JartopFile file : Core.getUserData().getFileSystem()) {
            if (file.getName().equals(name)) return file;
        }
        return null;
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
        JartopFile jfile;
        if (file != null) {
            jfile = new JartopFile(file.getName(), Files.readAllBytes(Paths.get(file.toURI())));
            Core.getUserData().getFileSystem().add(jfile);
        }
    }

    static void exportFile(List<JartopFile> files) {

    }
}
