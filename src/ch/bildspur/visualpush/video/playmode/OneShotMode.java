package ch.bildspur.visualpush.video.playmode;

import ch.bildspur.visualpush.sketch.controller.ClipController;
import ch.bildspur.visualpush.video.Clip;

/**
 * Created by cansik on 27/08/16.
 */
public class OneShotMode implements PlayMode {
    @Override
    public void onTriggered(Clip clip, ClipController clipController) {
        clip.stop();
        clip.play();
        clipController.activateClip(clip);
    }

    @Override
    public void offTriggered(Clip clip, ClipController clipController) {

    }

    @Override
    public void clipStopped(Clip clip, ClipController clipController) {
        clipController.deactivateClip(clip);
    }
}