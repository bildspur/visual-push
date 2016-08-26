package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.sketch.controller.MidiController;
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
    final int LED_START =  36;

    Movie bildspurLogo;
    int lightingLED = 63;
    int duration;
    float startFrame = 0;

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);

        String splashPath = ContentUtil.getContent("splash_screen_short.mov");
        bildspurLogo = new Movie(sketch, splashPath);
        bildspurLogo.play();

        duration = (int)(bildspurLogo.duration() * 45);
    }

    public void update()
    {
        MidiController.bus.sendNoteOff(0, lightingLED + LED_START, 0);
        lightingLED = (int)(PApplet.map(sketch.frameCount, startFrame, duration, 0, 63));
        MidiController.bus.sendNoteOn(0, lightingLED + LED_START, 126);

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
