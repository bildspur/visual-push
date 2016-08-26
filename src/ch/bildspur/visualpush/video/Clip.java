package ch.bildspur.visualpush.video;

import ch.bildspur.visualpush.util.ImageUtil;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.video.Movie;

/**
 * Created by cansik on 18/08/16.
 */
public class Clip extends Movie {
    PlayMode playMode = PlayMode.Normal;
    float opacity = 255;
    boolean isPlaying = false;

    public Clip(PApplet pApplet, String s) {
        super(pApplet, s);
    }

    public boolean isPlaying() {
        return isPlaying;
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

    @Override
    public void play()
     {
         super.play();
         isPlaying = true;
     }

    @Override
    public void loop()
    {
        super.loop();
        isPlaying = true;
    }

    @Override
    public void stop()
    {
        super.stop();
        isPlaying = false;
    }

    @Override
    protected void eosEvent() {
        super.eosEvent();
        System.out.println("END OF SIGNAL");
    }
}
