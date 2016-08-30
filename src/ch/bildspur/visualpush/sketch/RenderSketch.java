package ch.bildspur.visualpush.sketch;

import ch.bildspur.visualpush.sketch.controller.*;
import ch.bildspur.visualpush.sketch.state.ClipLaunchState;
import ch.bildspur.visualpush.sketch.state.PushState;
import ch.bildspur.visualpush.sketch.state.SplashScreenState;
import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PJOGL;
import processing.video.Movie;

import javax.sound.midi.MidiMessage;

/**
 * Created by cansik on 16/08/16.
 */
public class RenderSketch extends PApplet {

    final static int PUSH_DISPLAY_REFRESH_STEP = 1;

    final static int OUTPUT_WIDTH = 640;
    final static int OUTPUT_HEIGHT = 480;

    final static int FRAME_RATE = 30;

    SyphonController syphon = new SyphonController();
    MidiController midi = new MidiController();
    PushController push = new PushController();
    DesignController design = new DesignController();
    UIController ui = new UIController();
    ClipController clips = new ClipController();
    ConfigurationController config = new ConfigurationController();

    PGraphics uiScreen;
    PGraphics outputScreen;

    PushState activeState = new SplashScreenState();

    public void settings(){
        size(640, 480, P2D);
        PJOGL.profile = 1;
    }

    public void setup()
    {
        frameRate(FRAME_RATE);

        // create output screen
        outputScreen = createGraphics(OUTPUT_WIDTH, OUTPUT_HEIGHT, P2D);

        // controller setup
        syphon.setup(this);

        midi.setup(this);
        push.setup(this);
        ui.setup(this);
        clips.setup(this);
        config.setup(this);

        // get uiScreen from push lib
        uiScreen = push.getScreen();

        // setup uiScreen font and design
        design.setup(this, uiScreen);

        // first state setup
        activeState.setup(this, uiScreen);

        // load config async
        config.load("test.json");

        // start push screen
        push.open();
    }

    public void draw(){
        background(0);

        // clear push uiScreen
        uiScreen.beginDraw();
        uiScreen.background(0);
        uiScreen.endDraw();

        // clear output screen
        outputScreen.beginDraw();
        outputScreen.background(0);
        outputScreen.endDraw();

        // state machine
        if(activeState.isRunning())
            activeState.update();
        else
        {
            // change state
            activeState.stop();
            activeState = activeState.getNextState();
            activeState.setup(this, uiScreen);
        }

        outputScreen.beginDraw();

        // draw active clips
        for(Clip c : clips.getImmutableClips())
                c.paint(outputScreen);

        outputScreen.endDraw();

        // send syphon screen
        syphon.sendImageToSyphon(outputScreen);

        // draw output screen
        image(outputScreen, 0, 0);

        //draw push uiScreen
        uiScreen.beginDraw();

        // render ui
        ui.renderUI(uiScreen);

        // show debug information
        uiScreen.textAlign(PApplet.LEFT, PApplet.BOTTOM);
        uiScreen.text("FPS: " + (frameRate / PUSH_DISPLAY_REFRESH_STEP), 5, 20);
        uiScreen.endDraw();

        textAlign(PApplet.LEFT, PApplet.BOTTOM);
        text("FPS: " + frameRate, 5, 20);

        // publish push frame
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

    public void keyPressed()
    {
        switch (key) {
            case ' ':
                activeState = new ClipLaunchState();
                activeState.setup(this, uiScreen);
                break;
            case 's':
                System.out.println("config saved!");
                config.save("test.json");
                break;
        }
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

    public ClipController getClips() {
        return clips;
    }

    public PGraphics getUiScreen() {
        return uiScreen;
    }

    public PGraphics getOutputScreen() {
        return outputScreen;
    }
}

