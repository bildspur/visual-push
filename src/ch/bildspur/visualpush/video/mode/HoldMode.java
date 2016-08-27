package ch.bildspur.visualpush.video.mode;

import ch.bildspur.visualpush.video.Clip;

/**
 * Created by cansik on 27/08/16.
 */
public class HoldMode implements PlayMode {
    @Override
    public void onTriggered(Clip clip) {
        clip.loop();
    }

    @Override
    public void offTriggered(Clip clip) {
        clip.stop();
    }
}
