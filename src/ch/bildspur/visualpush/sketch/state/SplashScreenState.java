package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.util.ContentUtil;
import ch.bildspur.visualpush.util.ImageUtil;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.video.Movie;

import java.net.URISyntaxException;

/**
 * Created by cansik on 17/08/16.
 */
public class SplashScreenState extends PushState {

    Movie bildspurLogo;

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);

        String splashPath = ContentUtil.getContent("splash_screen_short.mov");
        bildspurLogo = new Movie(sketch, splashPath);
        bildspurLogo.play();
    }

    public void update()
    {
        ImageUtil.centerImageAdjusted(sketch.g, bildspurLogo);

        screen.beginDraw();
        ImageUtil.centerImageAdjusted(screen, bildspurLogo);
        screen.endDraw();

        if(bildspurLogo.time() >= bildspurLogo.duration())
            isRunning = false;
    }

    @Override
    public PushState getNextState() {
        return new ClipLaunchState();
    }
}
