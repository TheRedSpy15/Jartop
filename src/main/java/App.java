import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.Serializable;

public class App implements Serializable {
    private byte[] imageData;
    private String url;

    App(String url, byte[] imageData) {
        this.url = url;
        this.imageData = imageData;
    }

    void open() {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);

        StackPane pane = new StackPane();
        Scene scene = new Scene(pane);
        stage.setScene(scene);

        WebView webView = new WebView();
        pane.getChildren().add(webView);

        stage.show();
        stage.toFront();
        webView.getEngine().load(url);
    }

    byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
