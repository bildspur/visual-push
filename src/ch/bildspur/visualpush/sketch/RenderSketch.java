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

        // test things
        // todo: clean this up

        // encoder test
        for(int i = 0; i < 8; i++) {
            EncoderControl c = new EncoderControl(0, i + 71, 0, i);
            c.setPosition(new PVector(60 + (120 * i), 70));
            c.registerMidiEvent(midi);

            // set style
            c.setFillColor(new Color(1, 176, 240));
            c.setStrokeColor(new Color(255, 255, 255));

            ui.activeScene.addControl(c);
        }

        // first event listener test
        List<MidiEventListener> listeners =  new ArrayList<>();
        for(int i = 36; i < 100; i++)
            listeners.add(new PadHandler(0, i));

        for(int i = 20; i < 28; i++)
            listeners.add(new ButtonHandler(0, i));

        for(MidiEventListener l : listeners)
            l.registerMidiEvent(midi);
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
}

