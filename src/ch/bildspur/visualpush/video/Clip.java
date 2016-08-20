package ch.bildspur.visualpush.video;

import processing.core.PApplet;
import processing.video.Movie;

/**
 * Created by cansik on 18/08/16.
 */
public class Clip extends Movie {
    boolean isPlaying = false;

    public Clip(PApplet pApplet, String s) {
        super(pApplet, s);
    }

    public boolean isPlaying() {
        return isPlaying;
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
}
