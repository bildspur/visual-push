package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.util.ImageUtil;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Movie;

/**
 * Created by cansik on 17/08/16.
 */
public class SplashScreenState extends PushState {

    Movie bildspurLogo;

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);
        bildspurLogo = new Movie(sketch, "/Users/cansik/git/bildspur/visual-push/src/images/splash_screen.mov");
        bildspurLogo.play();
        //SplashScreenState.class.getResource("images/bildspur_trace.mov").toString());
    }

    public void update()
    {
        //screen.image(bildspurLogo, 0, 0, 100, 100);
        //sketch.image(bildspurLogo, 0, 0, 100, 100);

        ImageUtil.centerImageAdjusted(screen, bildspurLogo);

        if(bildspurLogo.time() >= bildspurLogo.duration())
            isRunning = false;
    }
}
