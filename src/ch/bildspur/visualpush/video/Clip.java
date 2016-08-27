package ch.bildspur.visualpush.video;

import ch.bildspur.visualpush.data.DataModelChangeListener;
import ch.bildspur.visualpush.util.ImageUtil;
import ch.bildspur.visualpush.video.event.ClipStateListener;
import ch.bildspur.visualpush.video.mode.LoopMode;
import ch.bildspur.visualpush.video.mode.PlayMode;
import com.jogamp.common.util.ArrayHashSet;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Movie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by cansik on 18/08/16.
 */
public class Clip extends Movie {
    PlayMode playMode = new LoopMode();
    float opacity = 255;
    PImage previewImage;

    HashSet<ClipStateListener> stateListener = new HashSet<>();

    public Clip(PApplet pApplet, String s) {
        super(pApplet, s);

        generatePreviewImage();
    }

    public boolean isPlaying() {
        return playing;
    }

    public PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(PlayMode playMode) {
        this.playMode = playMode;
    }

    public void paint(PGraphics g)
    {
        ImageUtil.centerImage(g, this);
    }

    public void addStateListener(ClipStateListener l)
    {
        stateListener.add(l);
    }

    public void removeStateListener(ClipStateListener l)
    {
        stateListener.remove(l);
    }

    public void generatePreviewImage()
    {
        // generate preview image
        play();
        jump(duration() / 2);
        previewImage = this.copy();
        stop();
    }

    void notifiyListener()
    {
        for(ClipStateListener l : stateListener)
            l.clipEnded(this);
    }

    public PImage getPreview()
    {
        if(playing)
            return this;
        else
            return previewImage;
    }

    @Override
    public void play()
     {
         super.play();
     }

    @Override
    public void loop()
    {
        super.loop();
    }

    @Override
    public void stop()
    {
        super.stop();
    }

    @Override
    protected void eosEvent() {
        super.eosEvent();
        notifiyListener();
    }
}
