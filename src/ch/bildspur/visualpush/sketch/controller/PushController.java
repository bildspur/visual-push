package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.push.Wayang;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by cansik on 17/08/16.
 */
public class PushController extends ProcessingController implements Runnable {

    static final int PUBLISH_FRAME_RATE = 50;

    Thread publishThread;

    BufferedImage pushDisplay = null;
    PGraphics screen;

    int[] buffer = new int[Wayang.DISPLAY_WIDTH * Wayang.DISPLAY_HEIGHT];
    int[] drawBuffer = new int[Wayang.DISPLAY_WIDTH * Wayang.DISPLAY_HEIGHT];

    final Object bufferLock = new Object();

    volatile boolean publishImage;

    public void setup(PApplet sketch){
        super.setup(sketch);

        screen = sketch.createGraphics(Wayang.DISPLAY_WIDTH, Wayang.DISPLAY_HEIGHT, PConstants.P2D);
    }

    public void open()
    {
        // connect to push
        pushDisplay = Wayang.open();

        publishImage = true;

        // run publishing thread
        publishThread = new Thread(this);
        publishThread.start();
    }

    public void close()
    {
        publishImage = false;

        try {
            publishThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Wayang.close();
    }

    public void sendFrame() {
        publishFrameDisplay(screen);
    }

    public PGraphics getScreen() {
        return screen;
    }

    public void run() {
        while (publishImage)
        {
            renderOnDisplay();

            try
            {
                Thread.sleep(PUBLISH_FRAME_RATE);
            }
            catch(Exception ex)
            {
            }
        }
    }

    private void renderOnDisplay()
    {
        synchronized(bufferLock) {
            System.arraycopy(buffer, 0, drawBuffer, 0, buffer.length );
        }


        for (int u = 0; u < Wayang.DISPLAY_HEIGHT; u++)
        {
            for (int v = 0; v < Wayang.DISPLAY_WIDTH; v++)
            {
                pushDisplay.setRGB(v, u, drawBuffer[(u * Wayang.DISPLAY_WIDTH) + v]);
            }
        }

        Wayang.sendFrameAsync();
    }

    private void publishFrameDisplay(PGraphics img)
    {
        Texture tex = ((PGraphicsOpenGL)sketch.g).getTexture(img);

        if (tex.available())
        {
            synchronized(bufferLock) {
                tex.get(buffer);
            }
        }
    }
}
