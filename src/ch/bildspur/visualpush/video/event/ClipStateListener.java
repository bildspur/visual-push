package ch.bildspur.visualpush.video.event;

import ch.bildspur.visualpush.video.Clip;

/**
 * Created by cansik on 27/08/16.
 */
public interface ClipStateListener {
    void clipEnded(Clip clip);
}
