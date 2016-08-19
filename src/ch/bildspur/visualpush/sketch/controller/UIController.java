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
    Scene activeScene;

    Panel p2;
    Panel p;

    public void setup(PApplet sketch) {
        super.setup(sketch);

        p2 = new Panel(0, 20, 10, 10);
        p2.setFillColor(Color.CYAN);

        p = new Panel(100, 50, 50, 50);
        p.addControl(p2);

        activeScene = new Scene();
        activeScene.addControl(p);
    }

    public void renderUI(PGraphics g)
    {
        p2.getPosition().add(1, 0);

        if(p2.getPosition().x > p.getWidth() - p2.getWidth())
            p2.setPosition(new PVector(0, 20));

        activeScene.paint(g);
    }
}
