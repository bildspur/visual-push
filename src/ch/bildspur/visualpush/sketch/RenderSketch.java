package ch.bildspur.visualpush.sketch;

import ch.bildspur.visualpush.sketch.controller.SyphonController;
import processing.core.PApplet;
import processing.opengl.PJOGL;

/**
 * Created by cansik on 16/08/16.
 */
public class RenderSketch extends PApplet {

    SyphonController syphon = new SyphonController();;

    public void settings(){
        size(400, 400, P3D);
        PJOGL.profile = 1;
    }

    public void setup()
    {
        // controller setup
        syphon.setup(this);

    }

    public void draw(){
        background(0);
        ellipse(mouseX, mouseY, 20, 20);

        syphon.sendScreenToSyphon();
    }
}
