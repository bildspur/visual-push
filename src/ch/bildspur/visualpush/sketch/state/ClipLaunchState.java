package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.NoteChangeHandler;
import ch.bildspur.visualpush.event.PadHandler;
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
    public static final int START_PAD_MIDI = 36;

    ClipController clips;
    Scene launchScene;
    Clip[][] grid;

    int activeRow = 7;
    int activeColumn = 0;

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);
        clips = this.sketch.getClips();
        grid = clips.getClipGrid();

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

        // add pad handler
        for(int i = 36; i <= 99; i++)
            new NoteChangeHandler(0, i) {
                @Override
                public void noteOn(int channel, int number, int value) {
                    Clip c = getClipByNumber(number);

                    if(c == null)
                        return;

                    c.getPlayMode().onTriggered(c);
                }

                @Override
                public void noteOff(int channel, int number, int value) {
                    Clip c = getClipByNumber(number);

                    if(c == null)
                        return;

                    c.getPlayMode().offTriggered(c);
                }
            };
    }

    Clip getClipByNumber(int number)
    {
        int column = (number - START_PAD_MIDI) / ClipController.GRID_SIZE;
        int row = (number - START_PAD_MIDI) % ClipController.GRID_SIZE;

        return grid[column][row];
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
