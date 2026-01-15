package resources;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    private Clip clip;

    public AudioPlayer(String path) {
        try {
            URL url = getClass().getClassLoader().getResource(path);

            if (url == null) {
                System.err.println("ERREUR : fichier audio introuvable : " + path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) return;

        clip.stop();
        clip.setFramePosition(0);  // remet au début
        clip.start();
    }

    public void loop() {
        if (clip == null) return;

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip == null) return;

        clip.stop();
        clip.setFramePosition(0);
    }
}
