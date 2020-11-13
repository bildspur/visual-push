package ch.bildspur.visualpush.video;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.util.ImageUtil;
import ch.bildspur.visualpush.video.event.ClipStateListener;
import ch.bildspur.visualpush.video.playmode.LoopMode;
import ch.bildspur.visualpush.video.playmode.PlayMode;
import gohai.glvideo.GLVideo;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Movie;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

/**
 * Created by cansik on 18/08/16.
 */
public class Clip extends GLVideo {

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
    DataModel<String> clipName;
    DataModel<Boolean> playing;
    DataModel<Path> fileName;

    HashSet<ClipStateListener> stateListener = new HashSet<>();

    int clipNumber;

    public Clip(PApplet pApplet, String s) {
        super(pApplet, s);

        // set unique clip number
        clipNumber = generateClipNumber();

        // initialize models
        playing = new DataModel<>(false);
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

        // get clip name
        Path p = Paths.get(s);
        fileName = new DataModel<>(p);
        String file = p.getFileName().toString().replace(".mov", "").replace("_", " ").trim();

        clipName = new DataModel<>(file);

        speed.addListener(value -> {
            if(pApplet.frameCount % SPEED_UPDATE_TIME == 0)
                this.speed(value);
        });

        generatePreviewImage();
    }

    public boolean isPlaying() {
        return playing.getValue();
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

    public DataModel<String> getClipName() {
        return clipName;
    }

    public int getClipNumber() {
        return this.clipNumber;
    }

    public DataModel<Path> getFileName() {
        return fileName;
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
        if(playing.getValue())
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

        // on end set eos event
        if(super.time() == 1.0)
            eosEvent();
    }

    @Override
    public void play()
     {
         super.play();
         playing.setValue(true);
     }

    @Override
    public void loop()
    {
        super.loop();
        playing.setValue(true);
    }

    public void stop()
    {
        super.pause();
        playing.setValue(false);
    }

    private void eosEvent() {
        notifiyListener();
    }
}
