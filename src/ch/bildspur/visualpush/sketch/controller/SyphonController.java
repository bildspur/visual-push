package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.Constants;
import codeanticode.syphon.SyphonServer;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Created by cansik on 16/08/16.
 */
public class SyphonController extends ProcessingController {
    SyphonServer server;

    public void setup(PApplet sketch){
        super.setup(sketch);

        server = new SyphonServer(sketch, Constants.APP_NAME);
    }

    public void sendScreenToSyphon()
    {
        server.sendScreen();
    }

    public void sendImageToSyphon(PGraphics p)
    {
        // Flip image back (very bad approach)
        PImage canvas = new PImage(p.width, p.height);
        for (int x = 0; x < p.width; x++) {
            for (int y = 0; y < p.height; y++) {
                canvas.pixels[((p.height-y-1)*p.width+x)] = p.pixels[y*p.width+x];
            }
        }

        server.sendImage(canvas);
    }
}
