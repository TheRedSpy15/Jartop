import com.google.common.base.Charsets;
import com.google.common.io.Files;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class NotepadController {

    @FXML HTMLEditor editor;
    @FXML TextField nameField;

    @FXML private void save() throws IOException {

        String fileName = !nameField.getText().trim().isEmpty() ? nameField.getText().trim() : "note.txt";

        File file = new File(fileName);
        Files.asCharSink(file, Charsets.UTF_8).write(editor.getHtmlText());
    }

    @FXML private void load() throws IOException {

        File file;
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "text files (*.txt)",
                        "*.txt"),
                new FileChooser.ExtensionFilter(
                        "All Files",
                        "*.*")
        );

        file = fileChooser.showOpenDialog(editor.getScene().getWindow());
        if (file != null) for (String line : Files.asCharSource(file, Charsets.UTF_8).readLines())
            editor.setHtmlText(editor.getHtmlText() + "\n" + line);
    }

    @FXML private void initialize() {

    }
}
