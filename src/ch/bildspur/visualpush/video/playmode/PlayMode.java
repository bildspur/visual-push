package ch.bildspur.visualpush.video.playmode;

import ch.bildspur.visualpush.sketch.controller.ClipController;
import ch.bildspur.visualpush.video.Clip;

/**
 * Created by cansik on 27/08/16.
 */
public interface PlayMode {
    void onTriggered(Clip clip, ClipController clipController);
    void offTriggered(Clip clip, ClipController clipController);
    void clipStopped(Clip clip, ClipController clipController);
}
