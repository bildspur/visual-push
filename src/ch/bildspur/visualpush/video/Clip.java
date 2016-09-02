package ch.bildspur.visualpush.video;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.util.ImageUtil;
import ch.bildspur.visualpush.video.event.ClipStateListener;
import ch.bildspur.visualpush.video.playmode.LoopMode;
import ch.bildspur.visualpush.video.playmode.PlayMode;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Movie;

import java.util.HashSet;

/**
 * Created by cansik on 18/08/16.
 */
public class Clip extends Movie {

    final static int SPEED_UPDATE_TIME = 20;
    static volatile int clipNumberCounter = 0;

    public static synchronized int generateClipNumber()
    {
        return clipNumberCounter++;
    }

    PImage previewImage;

    // settings
    DataModel<Float> opacity;
    DataModel<Float> startTime;
    DataModel<Float> endTime;
    DataModel<Float> speed;
    DataModel<PlayMode> playMode;
    DataModel<Float> zoom;
    DataModel<BlendMode> blendMode;
    DataModel<Float> redTint;
    DataModel<Float> blueTint;
    DataModel<Float> greenTint;

    HashSet<ClipStateListener> stateListener = new HashSet<>();

    int clipNumber;

    public Clip(PApplet pApplet, String s) {
        super(pApplet, s);

        // set unique clip number
        clipNumber = generateClipNumber();

        // initialize models
        playMode = new DataModel<>(new LoopMode());
        opacity = new DataModel<>(255f);
        startTime = new DataModel<>(0f);
        speed = new DataModel<>(1f);
        zoom = new DataModel<>(1f);
        blendMode = new DataModel<>(BlendMode.BLEND);
        endTime = new DataModel<>(duration());
        redTint = new DataModel<>(255f);
        blueTint = new DataModel<>(255f);
        greenTint = new DataModel<>(255f);

        speed.addListener(value -> {
            if(pApplet.frameCount % SPEED_UPDATE_TIME == 0)
                this.speed(value);
        });

        generatePreviewImage();
    }

    public boolean isPlaying() {
        return playing;
    }

    public DataModel<PlayMode> getPlayMode() {
        return playMode;
    }

    public void addStateListener(ClipStateListener l)
    {
        stateListener.add(l);
    }

    public void removeStateListener(ClipStateListener l)
    {
        stateListener.remove(l);
    }

    public DataModel<Float> getOpacity() {
        return opacity;
    }

    public DataModel<Float> getStartTime() {
        return startTime;
    }

    public DataModel<Float> getEndTime() {
        return endTime;
    }

    public DataModel<Float> getSpeed() {
        return speed;
    }

    public DataModel<Float> getZoom() {
        return zoom;
    }

    public DataModel<BlendMode> getBlendMode() {
        return blendMode;
    }

    public DataModel<Float> getRedTint() {
        return redTint;
    }

    public DataModel<Float> getBlueTint() {
        return blueTint;
    }

    public DataModel<Float> getGreenTint() {
        return greenTint;
    }

    public int getClipNumber() {
        return this.clipNumber;
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

    public void paint(PGraphics g)
    {
        // set opacity
        g.tint(redTint.getValue(), greenTint.getValue(), blueTint.getValue(), opacity.getValue());

        // set blend mode
        g.blendMode(blendMode.getValue().getIntValue());

        // draw image
        ImageUtil.centerImage(g, this);
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
