import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import javafx.scene.image.Image;
import org.controlsfx.control.Notifications;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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
    // changing will prevent all .jtop files with a different value from being deserialized
    // MIGHT remove with a full release, and this class rarely changes

    // Browser
    private boolean javascriptAllow = BrowserSettingsController.defaultJavaScript;
    private boolean popUpAllow = BrowserSettingsController.defaultPopUp;
    private boolean autoDelete = BrowserSettingsController.defaultClearExit;
    private boolean httpsOnly = BrowserSettingsController.defaultHttps;
    private boolean cookiesAllowed = BrowserSettingsController.defaultCookies;

    private String homePageUrl = BrowserSettingsController.defaultHomePage;
    private String userAgent = BrowserSettingsController.defaultUserAgent;

    private int maxHistory = BrowserSettingsController.defaultHistory;

    // Check List
    private Deque<String> todos = new ArrayDeque<>(100); // not yet used

    // Password Manager
    private Deque<Account> accountDeque = new ArrayDeque<>(100);

    // EmailClient
    private boolean sslConnection;

    private String emailPassword;
    private String emailAddress;
    private String hostName;

    private short smtpPort;

    // Contacts
    private HashMap<String, String> contacts = new HashMap<>(100); // <name, address> // not yet used

    // Proxy
    private String proxyAddress;

    private short proxyPort;

    // System
    private boolean sentryReporting = true;

    private Image wallpaperImage = new Image("images/wallpapericon.jpg");
    private String preferredColor = "#D2B48C";

    private double volume = 0.3;

    private File userFile = new File("guest" + saveExtension);

    private List<JartopFile> fileSystem = new ArrayList<>(500);

    // Credentials
    private boolean guest = true;

    private String name = "Guest";
    private String password = Core.defaultPassword; // default
    private String passwordSchool = "";

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
        byte keySize = (byte) (Core.getUserData().encryptWith256 ? 32 : 16); // 256 / 128

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
        byte keySize = (byte) (loadWith256 ? 32 : 16); // 256 / 128

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

            Core.getSecurity().setFailedAttempts((byte) (Core.getSecurity().getFailedAttempts() + 1));

            // secure delete
            if (Core.getSecurity().getFailedAttempts() >= maximumTries){

                Core.getSecurity().secureDelete(Core.getUserData().userFile, false);
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

            Core.loadAndTitle("Desktop", false);
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
        Logger.getAnonymousLogger().info("Set name to " + name);
    }

    protected final synchronized String getPassword() {
        return password;
    }

    protected final synchronized void setPassword(String password) {
        this.password = password;
        Logger.getAnonymousLogger().info("Updated password");
    }

    final boolean isGuest() {
        return guest;
    }

    final void setGuest(boolean guest) {
        this.guest = guest;
        Logger.getAnonymousLogger().info("Set guest to " + guest);
    }

    final String getEmailPassword() {
        return emailPassword;
    }

    final void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
        Logger.getAnonymousLogger().info("Updated email password");
    }

    final boolean isJavascriptAllow() {
        return javascriptAllow;
    }

    final void setJavascriptAllow(boolean javascriptAllow) {
        this.javascriptAllow = javascriptAllow;
        Logger.getAnonymousLogger().info("Set javascript to " + javascriptAllow);
    }

    final boolean isPopUpAllow() {
        return popUpAllow;
    }

    final void setPopUpAllow(boolean popUpAllow) {
        this.popUpAllow = popUpAllow;
        Logger.getAnonymousLogger().info("Set pop ups to " + popUpAllow);
    }

    final boolean isAutoDelete() {
        return autoDelete;
    }

    final void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
        Logger.getAnonymousLogger().info("Set auto delete to " + autoDelete);
    }

    final boolean isHttpsOnly() {
        return httpsOnly;
    }

    final void setHttpsOnly(boolean httpsOnly) {
        this.httpsOnly = httpsOnly;
        Logger.getAnonymousLogger().info("Set https only to " + httpsOnly);
    }

    final boolean isCookiesAllowed() {
        return cookiesAllowed;
    }

    final void setCookiesAllowed(boolean cookiesAllowed) {
        this.cookiesAllowed = cookiesAllowed;
        Logger.getAnonymousLogger().info("Set cookies to " + cookiesAllowed);
    }

    final String getHomePageUrl() {
        return homePageUrl;
    }

    final void setHomePageUrl(String homePageUrl) {
        this.homePageUrl = homePageUrl;
        Logger.getAnonymousLogger().info("Set home url to " + homePageUrl);
    }

    final int getMaxHistory() {
        return maxHistory;
    }

    final void setMaxHistory(int maxHistory) {
        this.maxHistory = maxHistory;
        Logger.getAnonymousLogger().info("Set max history to " + maxHistory);
    }

    public final Deque<String> getTodos() {
        return todos;
    }

    public final void setTodos(Deque<String> todos) {
        this.todos = todos;
        Logger.getAnonymousLogger().info("Updated todo");
    }

    final Deque<Account> getPasswords() {
        return accountDeque;
    }

    public final void setPasswords(Deque<Account> accountDeque) {
        this.accountDeque = accountDeque;
        Logger.getAnonymousLogger().info("Updated passwords");
    }

    final boolean isSslConnection() {
        return sslConnection;
    }

    final void setSslConnection(boolean sslConnection) {
        this.sslConnection = sslConnection;
        Logger.getAnonymousLogger().info("Set ssl to " + sslConnection);
    }

    final String getEmailAddress() {
        return emailAddress;
    }

    final void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        Logger.getAnonymousLogger().info("Set email address to " + emailAddress);
    }

    final String getHostName() {
        return hostName;
    }

    final void setHostName(String hostName) {
        this.hostName = hostName;
        Logger.getAnonymousLogger().info("Set hostname to " + hostName);
    }

    final short getSmtpPort() {
        return smtpPort;
    }

    final void setSmtpPort(short smtpPort) {
        this.smtpPort = smtpPort;
        Logger.getAnonymousLogger().info("Set smtp port to " + smtpPort);
    }

    public final HashMap<String, String> getContacts() {
        return contacts;
    }

    public final void setContacts(HashMap<String, String> contacts) {
        this.contacts = contacts;
        Logger.getAnonymousLogger().info("Updated contacts");
    }

    public final String getProxyAddress() {
        return proxyAddress;
    }

    final void setProxyAddress(String proxyAddress) {
        this.proxyAddress = proxyAddress;
        Logger.getAnonymousLogger().info("Set proxy address to " + proxyAddress);
    }

    public final short getProxyPort() {
        return proxyPort;
    }

    final void setProxyPort(short proxyPort) {
        this.proxyPort = proxyPort;
        Logger.getAnonymousLogger().info("Set proxy port to " + proxyPort);
    }

    final boolean isSentryReporting() {
        return sentryReporting;
    }

    final void setSentryReporting(boolean sentryReporting) {
        this.sentryReporting = sentryReporting;
        Logger.getAnonymousLogger().info("Set sentry reporting to " + sentryReporting);
    }

    final Image getWallpaperImage() {
        return wallpaperImage;
    }

    final void setWallpaperImage(Image wallpaperImage) {
        this.wallpaperImage = wallpaperImage;
        Logger.getAnonymousLogger().info("Set wallpaper path to " + wallpaperImage);
    }

    public final String getPreferredColor() {
        return preferredColor;
    }

    final void setPreferredColor(String preferredColor) {
        this.preferredColor = preferredColor;
        Logger.getAnonymousLogger().info("Set preferred color to " + preferredColor);
    }

    final double getVolume() {
        return volume;
    }

    public final void setVolume(double volume) {
        this.volume = volume;
        Logger.getAnonymousLogger().info("Set volume to " + volume);
    }

    final File getUserFile() {
        return userFile;
    }

    final void setUserFile(File userFile) {
        this.userFile = userFile;
        Logger.getAnonymousLogger().info("Set userfile to " + userFile);
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
        Logger.getAnonymousLogger().info("Set user agent to " + userAgent);
    }

    public String getPasswordSchool() {
        return passwordSchool;
    }

    public void setPasswordSchool(String passwordSchool) {
        this.passwordSchool = passwordSchool;
    }

    public List<JartopFile> getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(List<JartopFile> fileSystem) {
        this.fileSystem = fileSystem;
    }
}
