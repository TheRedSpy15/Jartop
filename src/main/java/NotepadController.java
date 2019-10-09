import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class NotepadController {

    @FXML HTMLEditor editor;
    @FXML TextField nameField;

    @FXML private void save() {

        String fileName = !nameField.getText().trim().isEmpty() ? nameField.getText().trim() : "note.jtxt";

        JartopFile file = new JartopFile();
        file.setName(fileName);
        file.setData(editor.getHtmlText().getBytes());
        Core.getUserData().getFileSystem().add(file);
    }

    @FXML private void load() {

        editor.setHtmlText("");
        for (String line : new String(FileSystem.findFile().getData()).split("\n"))
            editor.setHtmlText(editor.getHtmlText() + "\n" + line);
    }

    @FXML private void initialize() {

    }
}
