package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.push.color.RGBColor;
import ch.bildspur.visualpush.ui.FaderControl;
import ch.bildspur.visualpush.ui.FaderListControl;
import ch.bildspur.visualpush.ui.ListElement;
import ch.bildspur.visualpush.ui.Scene;
import ch.bildspur.visualpush.video.Clip;
import ch.bildspur.visualpush.video.playmode.PlayMode;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cansik on 31/08/16.
 */
public class AddClipState extends PushState {
    PushState parentState;
    Scene addClipScene;

    List<MidiEventListener> midiListener = new ArrayList<>();
    ArrayList<ListElement> clipFiles = new ArrayList<>();

    FaderListControl clipSelector;

    public static final int ADD_CLIP_BUTTON = 53;
    public static final int UNDO_BUTTON = 119;

    boolean returnToParentState = false;

    public AddClipState(PushState parentState)
    {
        this.parentState = parentState;
    }

    public void setup(PApplet sketch, PGraphics screen) {
        super.setup(sketch, screen);

        this.sketch.getMidi().clearLEDs();
        this.sketch.getMidi().sendControllerChange(9, ADD_CLIP_BUTTON, RGBColor.WHITE().getColor());
        this.sketch.getMidi().sendControllerChange(0, UNDO_BUTTON, RGBColor.WHITE().getColor());

        addClipScene = new Scene();

        midiListener.add(new ControlChangeHandler(0, ADD_CLIP_BUTTON)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127)
                {
                    System.out.println("add clip: " + clipFiles.get(clipSelector.getModel().getValue()).getValue());
                    returnToParentState = true;
                }
            }
        });

        midiListener.add(new ControlChangeHandler(0, UNDO_BUTTON)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127)
                {
                    returnToParentState = true;
                }
            }
        });

        // load clips from folder
        clipFiles.add(new ListElement("test", "test.mov"));
        clipFiles.add(new ListElement("test2", "test2.mov"));
        clipFiles.add(new ListElement("test3", "test3.mov"));

        // setup controls
        clipSelector = new FaderListControl(new DataModel<>(0), clipFiles, 0, 71, 0, 0);
        clipSelector.setPosition(new PVector(5, 5));
        clipSelector.setWidth(900);
        clipSelector.setHeight(150);
        clipSelector.setFillColor(Color.CYAN);
        midiListener.add(clipSelector);

        addClipScene.addControl(clipSelector);

        // set scene
        this.sketch.getUi().setActiveScene(addClipScene);

        registerMidiListeners();
    }

    void returnToParent()
    {
        removeMidiListeners();
        running = false;
    }

    void registerMidiListeners()
    {
        for(MidiEventListener listener : midiListener)
            listener.registerMidiEvent(sketch.getMidi());
    }

    void removeMidiListeners()
    {
        for(MidiEventListener listener : midiListener)
            listener.removeMidiEvent(sketch.getMidi());
    }

    @Override
    public void update()
    {
        if(returnToParentState)
            returnToParent();
    }

    @Override
    public PushState getNextState()
    {
        return parentState;
    }
}
