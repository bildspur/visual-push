package ch.bildspur.visualpush.video.mode;

import ch.bildspur.visualpush.sketch.controller.ClipController;
import ch.bildspur.visualpush.video.Clip;

/**
 * Created by cansik on 27/08/16.
 */
public class HoldMode implements PlayMode {
    @Override
    public void onTriggered(Clip clip, ClipController clipController) {
        clip.loop();
        clipController.activateClip(clip);
    }

    @Override
    public void offTriggered(Clip clip, ClipController clipController) {
        clip.stop();
        clipController.deactivateClip(clip);
    }

    @Override
    public void clipStopped(Clip clip, ClipController clipController) {

    }
}
