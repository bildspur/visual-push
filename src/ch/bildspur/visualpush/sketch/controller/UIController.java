package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.ui.Panel;
import ch.bildspur.visualpush.ui.Scene;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

/**
 * Created by cansik on 18/08/16.
 */
public class UIController extends ProcessingController {
    public Scene activeScene;

    public void setup(PApplet sketch) {
        super.setup(sketch);

        activeScene = new Scene();
    }

    public void renderUI(PGraphics g)
    {
        activeScene.paint(g);
    }
}
