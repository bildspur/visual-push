package ch.bildspur.visualpush.video.mode;

import ch.bildspur.visualpush.video.Clip;

/**
 * Created by cansik on 27/08/16.
 */
public interface PlayMode {
    void onTriggered(Clip clip);
    void offTriggered(Clip clip);
}
