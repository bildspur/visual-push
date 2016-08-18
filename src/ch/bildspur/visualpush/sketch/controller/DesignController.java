package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.util.ContentUtil;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

/**
 * Created by cansik on 18/08/16.
 */
public class DesignController extends ProcessingController {
    PGraphics screen;

    public void setup(PApplet sketch, PGraphics screen) {
        super.setup(sketch);
        this.screen = screen;

        setupFont();
    }

    void setupFont()
    {
        PFont pushScreenFont = sketch.createFont(ContentUtil.getContent("swissl.ttf"), 12);
        PFont displayFont = sketch.createFont(ContentUtil.getContent("swissl.ttf"), 14);

        sketch.textFont(displayFont);

        screen.beginDraw();
        screen.textFont(pushScreenFont);
        screen.endDraw();
    }
}
