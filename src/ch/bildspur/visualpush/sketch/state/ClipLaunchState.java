package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.sketch.controller.ClipController;
import ch.bildspur.visualpush.sketch.controller.MidiController;
import ch.bildspur.visualpush.ui.Scene;
import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Stack;

/**
 * Created by cansik on 26/08/16.
 */
public class ClipLaunchState extends PushState {
    public static final int START_COLUMN_MIDI = 20;
    public static final int START_ROW_MIDI = 36;

    ClipController clips;
    Scene launchScene;

    int activeRow = 7;
    int activeColumn = 0;

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);
        clips = this.sketch.getClips();

        initMidi();
        initScene();
    }

    void initScene()
    {
        launchScene = new Scene();
        sketch.getUi().setActiveScene(launchScene);

        MidiController.clearLEDs();

        switchColumn(activeColumn);
        switchRow(activeRow);
    }

    void initMidi()
    {
        // column selectors
        for(int i = 0; i < ClipController.GRID_SIZE; i++)
            new ControlChangeHandler(0, i + START_COLUMN_MIDI)
            {
                @Override
                public void controlChange(int channel, int number, int value) {
                    if(value == 127)
                        switchColumn(number - START_COLUMN_MIDI);
                }
            }.registerMidiEvent(sketch.getMidi());

        // row selectors
        for(int i = 0; i < ClipController.GRID_SIZE; i++)
            new ControlChangeHandler(0, i + START_ROW_MIDI)
            {
                @Override
                public void controlChange(int channel, int number, int value) {
                    if(value == 127)
                        switchRow(number - START_ROW_MIDI);
                }
            }.registerMidiEvent(sketch.getMidi());
    }

    void switchRow(int newNumber)
    {
        MidiController.bus.sendControllerChange(1, activeRow + START_ROW_MIDI, 0);
        activeRow = newNumber;
        MidiController.bus.sendControllerChange(1, activeRow + START_ROW_MIDI, 125);
    }

    void switchColumn(int newNumber)
    {
        MidiController.bus.sendControllerChange(1, activeColumn + START_COLUMN_MIDI, 0);
        activeColumn = newNumber;
        MidiController.bus.sendControllerChange(1, activeColumn + START_COLUMN_MIDI, 125);
    }

    public void update()
    {
    }
}
