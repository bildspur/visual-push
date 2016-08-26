package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.push.Wayang;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by cansik on 17/08/16.
 */
public class PushController extends ProcessingController {

    BufferedImage pushDisplay = null;
    PGraphics screen;
    Graphics2D graphics;

    public void setup(PApplet sketch){
        super.setup(sketch);

        // connect to push
        pushDisplay = Wayang.open();
        screen = sketch.createGraphics(Wayang.DISPLAY_WIDTH, Wayang.DISPLAY_HEIGHT, PConstants.P2D);
        graphics = pushDisplay.createGraphics();
    }

    public void close()
    {
        Wayang.close();
    }

    public void sendFrame() {
        renderOnDisplay(screen);
        Wayang.sendFrameAsync();
    }

    public PGraphics getScreen() {
        return screen;
    }
    private void renderOnDisplay(PGraphics img)
    {
        graphics.drawImage((Image)img.get().getNative(), 0, 0, null);
    }
}
