package ch.bildspur.visualpush.sketch;

import ch.bildspur.visualpush.Constants;
import ch.bildspur.visualpush.sketch.controller.MidiController;
import ch.bildspur.visualpush.sketch.controller.SyphonController;
import processing.core.PApplet;
import processing.opengl.PJOGL;
import themidibus.MidiBus;

/**
 * Created by cansik on 16/08/16.
 */
public class RenderSketch extends PApplet {

    SyphonController syphon = new SyphonController();
    MidiController midi = new MidiController();

    public void settings(){
        size(400, 400, P3D);
        PJOGL.profile = 1;
    }

    public void setup()
    {
        // controller setup
        syphon.setup(this);
        midi.setup(this);
    }

    public void draw(){
        background(0);
        ellipse(mouseX, mouseY, 20, 20);

        syphon.sendScreenToSyphon();
    }


    // Midi methods
    public void noteOn(int channel, int pitch, int velocity) {
        midi.noteOn(channel, pitch, velocity);
    }

    public void noteOff(int channel, int pitch, int velocity) {
        midi.noteOff(channel, pitch, velocity);
    }

    public void controllerChange(int channel, int number, int value) {
        midi.controllerChange(channel, number, value);
    }
}

