package ch.bildspur.visualpush.sketch;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.push.Project;
import ch.bildspur.visualpush.sketch.controller.*;
import ch.bildspur.visualpush.sketch.state.ClipLaunchState;
import ch.bildspur.visualpush.sketch.state.PushState;
import ch.bildspur.visualpush.sketch.state.SplashScreenState;
import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PJOGL;

import javax.sound.midi.MidiMessage;
import java.io.File;

/**
 * Created by cansik on 16/08/16.
 */
public class RenderSketch extends PApplet {
    final static int PUSH_DISPLAY_REFRESH_STEP = 1;

    final static int OUTPUT_WIDTH = 640;
    final static int OUTPUT_HEIGHT = 480;

    final static int FRAME_RATE = 60;

    SyphonController syphon = new SyphonController();
    MidiController midi = new MidiController();
    PushController push = new PushController();
    DesignController design = new DesignController();
    UIController ui = new UIController();
    ClipController clips = new ClipController();
    ConfigurationController config = new ConfigurationController();

    PGraphics uiScreen;
    PGraphics outputScreen;

    PushState activeState;

    DataModel<Float> globalOpacity = new DataModel<>(255f);

    boolean showOutput = true;

    public void settings(){
        size(OUTPUT_WIDTH, OUTPUT_HEIGHT, P2D);
        PJOGL.profile = 1;
    }

    public void setup()
    {
        frameRate(FRAME_RATE);
        surface.setTitle("Visual Push");

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
        activeState = new SplashScreenState(config);
        activeState.setup(this, uiScreen);

        // load global config
        config.loadGlobalConfig();

        if(config.getProject().exists()) {
            // load config async
            println("loading config " + config.getProject().getConfigFile().getFileName().toString());
            // config.loadAsync(config.getProject().getConfigFile().toString());
            config.load(config.getProject().getConfigFile().toString());
            println("loaded!");
        }
        else
        {
            config.notifyListener();
        }

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
            if(c != null)
                c.paint(outputScreen);

        outputScreen.endDraw();

        // send syphon screen
        syphon.sendImageToSyphon(outputScreen);

        // draw output screen
        if(showOutput) {
            tint(255, globalOpacity.getValue());
            image(outputScreen, 0, 0);
        }

        //draw push uiScreen
        uiScreen.beginDraw();

        // render ui
        ui.renderUI(uiScreen);

        // show debug information
        /*
        uiScreen.textAlign(PApplet.LEFT, PApplet.BOTTOM);
        uiScreen.text("FPS: " + (frameRate / PUSH_DISPLAY_REFRESH_STEP), 5, 20);
        */
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

    public void keyPressed()
    {
        switch (key) {
            case 'h':
                showOutput = !showOutput;
                break;
            case ' ':
                activeState = new ClipLaunchState();
                activeState.setup(this, uiScreen);
                break;
            case 'n':
                config.setProject(new Project(""));
                resetClips();
                break;
            case 'l':
                selectInput("Select a config file:", "loadConfig");
                break;
            case 's':
                if (config.getProject().exists()) {
                    saveConfig(new File(config.getProject().getConfigFile().toString()));
                }
                else
                {
                    selectOutput("Select a file to write the config to:", "saveConfig");
                }
                break;
        }
    }

    public void resetClips()
    {
        // deactivate all running clips
        for(Clip c : clips.getActiveClips())
            if(c != null)
                clips.deactivateClip(c);

        clips.initClipGrid();
    }

    public void saveConfig(File selection) {
        if (selection == null) {
            println("Window was closed or the user hit cancel.");
        } else {
            config.save(selection.toString());
            System.out.println("config saved!");
        }
    }

    public void loadConfig(File selection)
    {
        if (selection == null) {
            println("Window was closed or the user hit cancel.");
        } else {
            resetClips();
            activeState = new SplashScreenState(config);
            config.loadAsync(selection.toString());
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

    public ConfigurationController getConfig() {
        return config;
    }

    public DataModel<Float> getGlobalOpacity() {
        return globalOpacity;
    }
}

