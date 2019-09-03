//import org.jasypt.util.text.AES256TextEncryptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;


class FileSystemUtil {

    //private static AES256TextEncryptor encryptor = new AES256TextEncryptor();

    // Serialization
    static synchronized void saveFile(File file, String password) throws IOException {

        //encryptor.setPassword(password);
        //String encryptedText = encryptor.encrypt(String.valueOf(Files.readAllLines(file.toPath())));

        try (FileWriter fw = new FileWriter(file.getAbsoluteFile(), false)) {
            //fw.write(encryptedText);
        }
    }

    //static synchronized String decryptFile(File file, String password) throws IOException {

        //encryptor.setPassword(password);

        //return encryptor.decrypt(new String(Files.readAllBytes(file.toPath())));
    //}
}
