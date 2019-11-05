import java.io.Serializable;

public class JartopFile implements Serializable {
    private static final long serialVersionUID = -6677488858235437744L;
    private String name;
    private byte[] data;

    JartopFile() {}

    public JartopFile(String name) {
        this.name = name;
    }

    JartopFile(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    byte[] getData() {
        return data;
    }

    void setData(byte[] data) {
        this.data = data;
    }
}
