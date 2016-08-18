package ch.bildspur.visualpush.sketch;

import ch.bildspur.visualpush.sketch.controller.MidiController;
import ch.bildspur.visualpush.sketch.controller.PushController;
import ch.bildspur.visualpush.sketch.controller.SyphonController;
import ch.bildspur.visualpush.sketch.state.PushState;
import ch.bildspur.visualpush.sketch.state.SplashScreenState;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PJOGL;
import processing.video.Movie;

/**
 * Created by cansik on 16/08/16.
 */
public class RenderSketch extends PApplet {

    SyphonController syphon = new SyphonController();
    MidiController midi = new MidiController();
    PushController push = new PushController();

    PGraphics screen;

    PushState activeState = new SplashScreenState();

    public void settings(){
        size(640, 480, P2D);
        PJOGL.profile = 1;
    }

    public void setup()
    {
        frameRate(60);

        // controller setup
        syphon.setup(this);
        midi.setup(this);
        push.setup(this);

        screen = push.getScreen();

        // first state setup
        activeState.setup(this, screen);
    }

    public void draw(){
        background(0);
        ellipse(mouseX, mouseY, 20, 20);

        // draw screen
        screen.beginDraw();
        screen.background(0);

        if(activeState.isRunning())
            activeState.update();
        else
        {
            // change state
            activeState.stop();
            activeState = activeState.getNextState();
            activeState.setup(this, screen);
        }

        screen.text("FPS: " + (frameRate), 5, 20);
        screen.endDraw();

        syphon.sendScreenToSyphon();

        push.sendFrame();

        text("FPS: " + frameRate, 5, 20);
    }

    public void stop()
    {
        push.close();
    }

    // Video methods
    public void movieEvent(Movie m) {
        if(m.available())
            m.read();
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

