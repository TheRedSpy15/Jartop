import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import java.io.File;

/*
 * Jartop - Virtual Desktop Emulator. The MIT License (MIT).
 * Copyright (c) TheRedSpy15 (hjadar15@protonmail.com).
 * See LICENSE for details.
 */

public class MoviePlayerController {

    @FXML private MediaView player;

    @FXML private void play(){

        player.getMediaPlayer().play();
    }

    @FXML private void pause(){

        player.getMediaPlayer().pause();
    }

    @FXML private void select(){

        File file;
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select a video");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "video Files (.mp4)",
                        "*.mp4"),
                new FileChooser.ExtensionFilter(
                        "All Files",
                        "*.*")
        );

        file = fileChooser.showOpenDialog(DesktopController.getAppWindow());
        if (file != null) {
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            player.setMediaPlayer(mediaPlayer);
            player.getMediaPlayer().setVolume(Core.getUserData().getVolume()); // volume
        }
    }
}
