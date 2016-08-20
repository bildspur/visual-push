package ch.bildspur.visualpush.sketch;

import ch.bildspur.visualpush.event.ButtonHandler;
import ch.bildspur.visualpush.event.PadHandler;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.sketch.controller.*;
import ch.bildspur.visualpush.sketch.state.PushState;
import ch.bildspur.visualpush.sketch.state.SplashScreenState;
import ch.bildspur.visualpush.ui.EncoderControl;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PJOGL;
import processing.video.Movie;

import javax.sound.midi.MidiMessage;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cansik on 16/08/16.
 */
public class RenderSketch extends PApplet {

    final static int PUSH_DISPLAY_REFRESH_STEP = 2;

    SyphonController syphon = new SyphonController();
    MidiController midi = new MidiController();
    PushController push = new PushController();
    DesignController design = new DesignController();
    UIController ui = new UIController();

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
        ui.setup(this);

        // get screen from push lib
        screen = push.getScreen();

        // setup screen font and design
        design.setup(this, screen);

        // first state setup
        activeState.setup(this, screen);
    }

    public void draw(){
        background(0);

        // draw screen
        screen.beginDraw();
        screen.background(0);

        // state machine
        if(activeState.isRunning())
            activeState.update();
        else
        {
            // change state
            activeState.stop();
            activeState = activeState.getNextState();
            activeState.setup(this, screen);
        }

        // render ui
        ui.renderUI(screen);

        // show debug information
        screen.textAlign(PApplet.LEFT, PApplet.BOTTOM);
        screen.text("FPS: " + (frameRate / PUSH_DISPLAY_REFRESH_STEP), 5, 20);

        textAlign(PApplet.LEFT, PApplet.BOTTOM);
        text("FPS: " + frameRate, 5, 20);

        screen.endDraw();

        // send screens
        syphon.sendScreenToSyphon();

        if(frameCount % PUSH_DISPLAY_REFRESH_STEP == 0)
            push.sendFrame();
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
    public void midiMessage(MidiMessage message) { midi.midiMessage(message);}

    public SyphonController getSyphon() {
        return syphon;
    }

    public MidiController getMidi() {
        return midi;
    }

    public PushController getPush() {
        return push;
    }

    public DesignController getDesign() {
        return design;
    }

    public UIController getUi() {
        return ui;
    }
}

