import javafx.scene.image.Image;

public class JartopFile {
    private String name;
    private byte[] data;
    private boolean isImage;
    private boolean isPlainText;
    private boolean isBinary;
    private Image image;
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public boolean isPlainText() {
        return isPlainText;
    }

    public void setPlainText(boolean plainText) {
        isPlainText = plainText;
    }

    public boolean isBinary() {
        return isBinary;
    }

    public void setBinary(boolean binary) {
        isBinary = binary;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
