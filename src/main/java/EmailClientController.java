import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class EmailClientController {

    private MultiPartEmail mail = new MultiPartEmail();
    private Deque<EmailAttachment> attachments = new ArrayDeque<>(5);

    @FXML private JFXTextField addressField, subjectField, bccField, ccField;
    @FXML private Label attachLbl;
    @FXML private TextArea messageArea;
    @FXML private AnchorPane background;

    @FXML private void initialize(){

        background.setStyle("-fx-background-color: " + Core.getUserData().getPreferredColor());
    }

    @FXML private void send() throws EmailException {

        // Credentials
        final String addressSender = Core.getUserData().getEmailAddress();
        final String password = Core.getUserData().getEmailPassword();
        final String host = Core.getUserData().getHostName();

        final short port = Core.getUserData().getSmtpPort();

        final boolean useSSL = Core.getUserData().isSslConnection();

        // Email details
        final String addressReceiver = addressField.getText();
        final String subject = subjectField.getText();
        final String message = messageArea.getText();

        // Sending
        mail.setHostName(host);
        mail.setSmtpPort(port);
        mail.setAuthenticator(new DefaultAuthenticator(addressSender, password));
        mail.setSSLOnConnect(useSSL);
        mail.setFrom(addressSender);
        mail.setSubject(subject);
        mail.setMsg(message);
        mail.addTo(addressReceiver);

        // bcc & cc
        if (!bccField.getText().trim().isEmpty()) mail.addBcc(bccField.getText());
        if (!ccField.getText().trim().isEmpty()) mail.addCc(ccField.getText());

        if (attachments != null) {

            for (EmailAttachment i : attachments) {

                mail.attach(i);
            }
        }

        mail.send();

        background.getScene().getWindow().hide();
    }

    @FXML private void attach(){

        EmailAttachment attachment = new EmailAttachment();

        File file;

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select a file");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "All Files",
                        "*.*")
        );

        file = fileChooser.showOpenDialog(background.getScene().getWindow());
        if (file != null) {

            attachment.setPath(file.getPath());

            attachments.push(attachment);

            // attachment label updating
            if (!attachLbl.getText().equals("Attachments: none"))
                attachLbl.setText(String.join(", ", attachLbl.getText(), file.getName()));
            else attachLbl.setText("Attachments: " + file.getName());
        }
    }

    @FXML private void remove(){

        attachments.clear();

        attachLbl.setText("Attachments: none");
    }

    @FXML private void settings() throws IOException {

        Core.loadAndTitle("EmailSettings", true);
    }
}
