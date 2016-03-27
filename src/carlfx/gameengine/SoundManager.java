package carlfx.gameengine;

import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

/**
 * Responsible for loading sound media to be played using an id or key.
 * Contains all sounds for use later.
 * <p/>
 * User: cdea
 */
public class SoundManager {
    ExecutorService soundPool = Executors.newFixedThreadPool(2);
    Map<String, AudioClip> soundEffectsMap = new HashMap<>();

    /**
     * Constructor to create a simple thread pool.
     *
     * @param numberOfThreads - number of threads to use media players in the map.
     */
    public SoundManager(int numberOfThreads) {
        soundPool = Executors.newFixedThreadPool(numberOfThreads);
    }

    /**
     * Load a sound into a map to later be played based on the id.
     *
     * @param id  - The identifier for a sound.
     * @param url - The url location of the media or audio resource. Usually in src/main/resources directory.
     */
    public void loadSoundEffects(String id, URL url) {
        AudioClip sound = new AudioClip(url.toExternalForm());
        soundEffectsMap.put(id, sound);
    }

    /**
     * Lookup a name resource to play sound based on the id.
     *
     * @param id identifier for a sound to be played.
     */
    public void playSound(final String id) {
        playSound(id, 1);
    }

    public void playSound(String id, int count) {
        Runnable soundPlay = new Runnable() {
            @Override
            public void run() {
                AudioClip soundClip = soundEffectsMap.get(id);
                soundClip.setCycleCount(count);
                soundClip.play();
            }
        };
        soundPool.execute(soundPlay);
    }

    /**
     * Hacked in without much thought to play background music randomly
     * For sure this could be done in a more adaptable and reusable manner
     */
//    public void playRandomSoundInfinitely(String[] ids) {
//        Runnable soundPlay = new Runnable() {
//            // unseeded intentionally
//            Random rng = new Random();
//            @Override
//            public void run() {
//                int choice = rng.nextInt(ids.length);
//
//                //AudioClip soundClip = soundEffectsMap.get(ids[choice]);
//                //soundClip.setCycleCount(1);
//                //soundClip.play();
//            }
//        };
//        soundPool.execute(soundPlay);
//    }

    /**
     * Stop all threads and media players.
     */
    public void shutdown() {
        soundPool.shutdown();
    }

}
