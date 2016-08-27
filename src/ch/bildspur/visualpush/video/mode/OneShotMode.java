package ch.bildspur.visualpush.video.mode;

import ch.bildspur.visualpush.video.Clip;

/**
 * Created by cansik on 27/08/16.
 */
public class OneShotMode implements PlayMode {
    @Override
    public void onTriggered(Clip clip) {
        clip.play();
    }

    @Override
    public void offTriggered(Clip clip) {

    }
}