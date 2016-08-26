package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.Constants;
import codeanticode.syphon.SyphonServer;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.Texture;

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
        server.sendImage(p);
    }
}
