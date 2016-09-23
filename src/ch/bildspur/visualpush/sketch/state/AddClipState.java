package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.push.color.RGBColor;
import ch.bildspur.visualpush.sketch.RenderSketch;
import ch.bildspur.visualpush.sketch.controller.ClipController;
import ch.bildspur.visualpush.ui.FaderListControl;
import ch.bildspur.visualpush.ui.ListElement;
import ch.bildspur.visualpush.ui.Scene;
import ch.bildspur.visualpush.util.ContentUtil;
import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cansik on 31/08/16.
 */
public class AddClipState extends PushState {
    public static final int START_PAD_MIDI = 36;

    PushState parentState;
    Scene addClipScene;

    List<MidiEventListener> midiListener = new ArrayList<>();
    ArrayList<ListElement> clipFiles = new ArrayList<>();

    FaderListControl clipSelector;

    public static final int ADD_CLIP_BUTTON = 53;
    public static final int CLIP_BUTTON = 113;
    public static final int DELETE_CLIP_BUTTON = 118;


    boolean returnToParentState = false;

    int padNumber;

    public AddClipState(PushState parentState, int padNumber)
    {
        this.parentState = parentState;
        this.padNumber = padNumber;
    }

    public void setup(PApplet sketch, PGraphics screen) {
        super.setup(sketch, screen);

        this.sketch.getMidi().clearLEDs();

        this.sketch.getMidi().sendControllerChange(9, CLIP_BUTTON, RGBColor.WHITE().getColor());
        this.sketch.getMidi().sendControllerChange(0, ADD_CLIP_BUTTON, RGBColor.GREEN().getColor());
        this.sketch.getMidi().sendControllerChange(0, DELETE_CLIP_BUTTON, RGBColor.RED().getColor());


        // set current clip blinking
        this.sketch.getMidi().sendNoteOn(9, padNumber, RGBColor.PINK().getColor());

        addClipScene = new Scene();

        midiListener.add(new ControlChangeHandler(0, ADD_CLIP_BUTTON)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127)
                {
                    Path clipPath = (Path)clipFiles.get(clipSelector.getModel().getValue()).getValue();
                    int row = (padNumber - START_PAD_MIDI) / ClipController.GRID_SIZE;
                    int column = (padNumber - START_PAD_MIDI) % ClipController.GRID_SIZE;

                    // delete old clip if exists
                    Clip clip = AddClipState.this.sketch.getClips().getClipGrid()[row][column];
                    if(clip != null) {
                        clip.stop();
                        AddClipState.this.sketch.getClips().deactivateClip(clip);
                        AddClipState.this.sketch.getClips().getClipGrid()[row][column] = null;
                    }

                    AddClipState.this.sketch.getClips().getClipGrid()[row][column] = new Clip(sketch, clipPath.toString());
                    returnToParentState = true;
                }
            }
        });

        midiListener.add(new ControlChangeHandler(0, CLIP_BUTTON)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127)
                {
                    returnToParentState = true;
                }
            }
        });

        midiListener.add(new ControlChangeHandler(0, DELETE_CLIP_BUTTON)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127)
                {
                    int row = (padNumber - START_PAD_MIDI) / ClipController.GRID_SIZE;
                    int column = (padNumber - START_PAD_MIDI) % ClipController.GRID_SIZE;

                    Clip clip = AddClipState.this.sketch.getClips().getClipGrid()[row][column];
                    if(clip != null) {
                        clip.stop();
                        AddClipState.this.sketch.getClips().deactivateClip(clip);
                        AddClipState.this.sketch.getClips().getClipGrid()[row][column] = null;
                    }

                    returnToParentState = true;
                }
            }
        });


        // load clips from folder
        for(Path path : fileList(((RenderSketch)sketch).getConfig().getProject().getVisualPath().toString()))
        {
            clipFiles.add(new ListElement(path, path.getFileName().toString()));
        }

        // setup controls
        clipSelector = new FaderListControl(new DataModel<>(0), clipFiles, 0, 71, 0, 0);
        clipSelector.setPosition(new PVector(5, 5));
        clipSelector.setAlwaysActive(true);
        clipSelector.setWidth(950);
        clipSelector.setHeight(150);
        clipSelector.setFillColor(Color.CYAN);
        midiListener.add(clipSelector);

        addClipScene.addControl(clipSelector);

        // set scene
        this.sketch.getUi().setActiveScene(addClipScene);

        registerMidiListeners();
    }

     List<Path> fileList(String directory) {
        List<Path> paths = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : directoryStream) {
                paths.add(path);
            }
        } catch (IOException ex) {}
        return paths;
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
