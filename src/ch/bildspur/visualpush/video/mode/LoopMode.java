package ch.bildspur.visualpush.video.mode;

import ch.bildspur.visualpush.video.Clip;

/**
 * Created by cansik on 27/08/16.
 */
public class LoopMode implements PlayMode {
    @Override
    public void onTriggered(Clip clip) {
        if(clip.isPlaying())
            clip.stop();
        else
            clip.loop();
    }

    @Override
    public void offTriggered(Clip clip) {

    }
}