package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.push.color.RGBColor;
import ch.bildspur.visualpush.sketch.controller.ConfigurationController;
import ch.bildspur.visualpush.util.ContentUtil;
import ch.bildspur.visualpush.util.ImageUtil;
import gohai.glvideo.GLMovie;
import gohai.glvideo.GLVideo;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Created by cansik on 17/08/16.
 */
public class SplashScreenState extends PushState {
    final int LED_START =  36;

    GLMovie bildspurLogo;
    int lightingLED = 63;
    int duration;
    float startFrame = 0;

    volatile boolean configurationLoaded = false;

    public SplashScreenState(ConfigurationController configurationController)
    {
        configurationController.addConfigLoadedListener(cc -> {
            configurationLoaded = true;
        });
    }

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);

        String splashPath = ContentUtil.getContent("splash_screen_short.mov");
        bildspurLogo = new GLMovie(sketch, splashPath);
        bildspurLogo.loop();

        this.sketch.getMidi().clearLEDs();
    }

    public void update()
    {
        if (bildspurLogo.available()) {
            bildspurLogo.read();

            if(duration == 0)
                duration = (int)(bildspurLogo.duration() * 45);
        }

        this.sketch.getMidi().sendNoteOff(0, lightingLED + LED_START, 0);
        lightingLED = (int)(PApplet.map(sketch.frameCount, startFrame, duration, 0, 63));
        this.sketch.getMidi().sendNoteOn(0, lightingLED + LED_START, RGBColor.WHITE().getColor());

        sketch.getOutputScreen().beginDraw();
        ImageUtil.centerImageAdjusted(sketch.getOutputScreen(), bildspurLogo);
        sketch.getOutputScreen().endDraw();

        screen.beginDraw();
        ImageUtil.centerImageAdjusted(screen, bildspurLogo);
        screen.endDraw();

        if(configurationLoaded)
            running = false;
    }

    @Override
    public PushState getNextState() {
        return new ClipLaunchState();
    }
}
