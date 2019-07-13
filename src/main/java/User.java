import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.controlsfx.control.Notifications;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.logging.Logger;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class User implements Serializable {

    static boolean internetConnection = false;
    static final String saveExtension = ".jtop";
    private static final long serialVersionUID = 42L; // NEVER change unless ABSOLUTELY necessary
    // changing will prevent all .ser files with a different value from being deserialized
    // MIGHT remove with a full release, and this class rarely changes

    // Browser
    private boolean javascriptAllow = BrowserSettings.defaultJavaScript;
    private boolean popUpAllow = BrowserSettings.defaultPopUp;
    private boolean autoDelete = BrowserSettings.defaultClearExit;
    private boolean httpsOnly = BrowserSettings.defaultHttps;
    private boolean cookiesAllowed = BrowserSettings.defaultCookies;

    private String homePageUrl = BrowserSettings.defaultHomePage;
    private String userAgent = BrowserSettings.defaultUserAgent;

    private int maxHistory = BrowserSettings.defaultHistory;

    // Check List
    private Deque<String> todos = new ArrayDeque<>(); // not yet used

    // Password Manager
    private Deque<Account> accountDeque = new ArrayDeque<>();

    // EmailClient
    private boolean sslConnection;

    private String emailPassword;
    private String emailAddress;
    private String hostName;

    private short smtpPort;

    // Contacts
    private HashMap<String, String> contacts = new HashMap<>(); // <name, address> // not yet used

    // Proxy
    private String proxyAddress;

    private short proxyPort;

    // System
    private boolean sentryReporting = true;
    private boolean torConnection = false;

    private String wallpaperPath = "images/wallpapericon.jpg";
    private String preferredColor = "#FFFFFF";

    private double volume = 0.3;

    private File userFile = new File("guest" + saveExtension);

    // Credentials
    private boolean guest = true;

    private String name = "Guest";
    private String password = Core.defaultPassword; // default

    // Encryption
    private boolean encryptWith256 = false;

    // Serialization
    final synchronized void saveData() throws
            IOException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            IllegalBlockSizeException {

        // Determining key size
        byte keySize;

        if (Core.getUserData().encryptWith256) keySize = 32; // 256
        else keySize = 16; // 128

        // Hashing
        final String hash =
                Hashing.sha256()
                        .hashString(password, Charsets.UTF_8)
                        .toString().substring(0, keySize);

        // Creating keys
        final byte[] key = hash.getBytes();
        final String transformation = "AES";
        final SecretKeySpec sks = new SecretKeySpec(key, transformation);

        // Creating cipher
        final Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, sks);

        final SealedObject sealedUser = new SealedObject(Core.getUserData(), cipher);

        // Streams
        final FileOutputStream fileOutputStream = new FileOutputStream(userFile.getAbsoluteFile());
        final CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(cipherOutputStream)) {

            // Writing
            objectOutputStream.writeObject(sealedUser);
        }
        fileOutputStream.close();
        cipherOutputStream.close();
    }

    final synchronized void login(String password, boolean loadWith256) throws
            IOException, InterruptedException {

        boolean loggedIn = true;

        final byte bruteForcePauseLength = 50;
        final byte maximumTries = 20;

        // Determining key size
        byte keySize;

        if (loadWith256) keySize = 32; // 256
        else keySize = 16; // 128

        try {
            try {
                // Hashing
                final String hash =
                        Hashing.sha256()
                                .hashString(password, Charsets.UTF_8)
                                .toString().substring(0, keySize);

                // Creating keys
                final byte[] key = hash.getBytes();
                final String transformation = "AES";
                final SecretKeySpec sks = new SecretKeySpec(key, transformation);

                // Creating cipher
                final Cipher cipher = Cipher.getInstance(transformation);
                cipher.init(Cipher.DECRYPT_MODE, sks);

                // Streams
                final FileInputStream fileInputStream = new FileInputStream(userFile.getAbsoluteFile());
                try (fileInputStream; CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher); ObjectInputStream objectInputStream = new ObjectInputStream(cipherInputStream)) {

                    // Reading
                    SealedObject sealedObject = (SealedObject) objectInputStream.readObject();
                    Core.setUserData((User) sealedObject.getObject(cipher));
                }

                // Closing streams
            } catch (NoSuchPaddingException |
                    NoSuchAlgorithmException |
                    ClassNotFoundException |
                    InvalidKeyException |
                    IllegalBlockSizeException |
                    BadPaddingException e) {

                e.printStackTrace();
            }
        } catch (Exception e) { // assuming wrong password

            loggedIn = false;

            Core.getUAS().setFailedAttempts((byte) (Core.getUAS().getFailedAttempts() + 1));

            // secure delete
            if (Core.getUAS().getFailedAttempts() >= maximumTries){

                Core.getUAS().secureDelete(Core.getUserData().userFile, false);
            }

            Core.getUserData().wait(bruteForcePauseLength); // to slow down brute force attacks

            Logger.getAnonymousLogger().warning("Log in failed - likely wrong password");

            Notifications
                    .create()
                    .title("Warning")
                    .text("Log in failed - likely wrong password")
                    .darkStyle()
                    .showWarning();
        }

        // Loading desktop
        if (loggedIn) {

            Parent desktopScene = FXMLLoader.load(Core.class.getResource("Desktop.fxml"));
            Core.getDesktop().setScene(new Scene(desktopScene));
            Core.getUserData().guest = false;

            Logger.getAnonymousLogger().info("Logged in successfully");

            Notifications
                    .create()
                    .title("Notice")
                    .text("Logged in as " + name)
                    .darkStyle()
                    .showInformation();
        }
    }

    // Getters & Setters
    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    protected final synchronized String getPassword() {
        return password;
    }

    protected final synchronized void setPassword(String password) {
        this.password = password;
    }

    final boolean isGuest() {
        return guest;
    }

    final void setGuest(boolean guest) {
        this.guest = guest;
    }

    final String getEmailPassword() {
        return emailPassword;
    }

    final void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    final boolean isJavascriptAllow() {
        return javascriptAllow;
    }

    final void setJavascriptAllow(boolean javascriptAllow) {
        this.javascriptAllow = javascriptAllow;
    }

    final boolean isPopUpAllow() {
        return popUpAllow;
    }

    final void setPopUpAllow(boolean popUpAllow) {
        this.popUpAllow = popUpAllow;
    }

    final boolean isAutoDelete() {
        return autoDelete;
    }

    final void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    final boolean isHttpsOnly() {
        return httpsOnly;
    }

    final void setHttpsOnly(boolean httpsOnly) {
        this.httpsOnly = httpsOnly;
    }

    final boolean isCookiesAllowed() {
        return cookiesAllowed;
    }

    final void setCookiesAllowed(boolean cookiesAllowed) {
        this.cookiesAllowed = cookiesAllowed;
    }

    final String getHomePageUrl() {
        return homePageUrl;
    }

    final void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
    }

    final int getMaxHistory() {
        return maxHistory;
    }

    final void setMaxHistory(int maxHistory) {
        this.maxHistory = maxHistory;
    }

    public final Deque<String> getTodos() {
        return todos;
    }

    public final void setTodos(Deque<String> todos) {
        this.todos = todos;
    }

    final Deque<Account> getPasswords() {
        return accountDeque;
    }

    public final void setPasswords(Deque<Account> accountDeque) {
        this.accountDeque = accountDeque;
    }

    final boolean isSslConnection() {
        return sslConnection;
    }

    final void setSslConnection(boolean sslConnection) {
        this.sslConnection = sslConnection;
    }

    final String getEmailAddress() {
        return emailAddress;
    }

    final void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    final String getHostName() {
        return hostName;
    }

    final void setHostName(String hostName) {
        this.hostName = hostName;
    }

    final short getSmtpPort() {
        return smtpPort;
    }

    final void setSmtpPort(short smtpPort) {
        this.smtpPort = smtpPort;
    }

    public final HashMap<String, String> getContacts() {
        return contacts;
    }

    public final void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
    }

    public final String getProxyAddress() {
        return proxyAddress;
    }

    final void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public final short getProxyPort() {
        return proxyPort;
    }

    final void setProxyPort(short proxyPort) {
        this.proxyPort = proxyPort;
    }

    final boolean isSentryReporting() {
        return sentryReporting;
    }

    final void setSentryReporting(boolean sentryReporting) {
        this.sentryReporting = sentryReporting;
    }

    final String getWallpaperPath() {
        return wallpaperPath;
    }

    final void setWallpaperPath(String wallpaperPath) {
        this.wallpaperPath = wallpaperPath;
    }

    public final String getPreferredColor() {
        return preferredColor;
    }

    final void setPreferredColor(String preferredColor) {
        this.preferredColor = preferredColor;
    }

    final double getVolume() {
        return volume;
    }

    public final void setVolume(double volume) {
        this.volume = volume;
    }

    final File getUserFile() {
        return userFile;
    }

    final void setUserFile(File userFile) {
        this.userFile = userFile;
    }

    final boolean isEncryptWith256() {
        return encryptWith256;
    }

    final void setEncryptWith256(boolean encryptWith256) {
        this.encryptWith256 = encryptWith256;
    }

    final String getUserAgent() {
        return userAgent;
    }

    final void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    final boolean isTorConnection() {
        return torConnection;
    }

    final void setTorConnection(boolean torConnection) {
        this.torConnection = torConnection;
    }
}
