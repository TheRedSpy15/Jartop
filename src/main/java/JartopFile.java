import java.io.Serializable;

public class JartopFile implements Serializable {
    private static final long serialVersionUID = -6677488858235437744L;
    private String name = "file";
    private byte[] data;

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
