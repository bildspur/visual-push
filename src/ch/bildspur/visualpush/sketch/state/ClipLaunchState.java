package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.NoteChangeHandler;
import ch.bildspur.visualpush.push.color.PushColor;
import ch.bildspur.visualpush.push.color.RGBColor;
import ch.bildspur.visualpush.sketch.controller.ClipController;
import ch.bildspur.visualpush.sketch.controller.MidiController;
import ch.bildspur.visualpush.ui.ClipViewerControl;
import ch.bildspur.visualpush.ui.Scene;
import ch.bildspur.visualpush.util.ContentUtil;
import ch.bildspur.visualpush.video.Clip;
import ch.bildspur.visualpush.video.event.ClipStateListener;
import ch.bildspur.visualpush.video.mode.HoldMode;
import ch.bildspur.visualpush.video.mode.LoopMode;
import ch.bildspur.visualpush.video.mode.OneShotMode;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Stack;

/**
 * Created by cansik on 26/08/16.
 */
public class ClipLaunchState extends PushState implements ClipStateListener {
    public static final int START_COLUMN_MIDI = 20;
    public static final int START_ROW_MIDI = 36;
    public static final int START_PAD_MIDI = 36;

    public static final int HOLD_MODE_COLOR = 125;
    public static final int LOOP_MODE_COLOR = 126;
    public static final int ONE_SHOT_MODE_COLOR = 127;

    public static final int COLUMN_ROW_SELECTOR_COLOR = 122;

    public static final int DEFAULT_PULSING = 1;
    public static final int PLAY_PULSING = 9;

    public static final int SOLO_BUTTON = 61;

    ClipController clipController;
    MidiController midiController;
    Scene launchScene;
    Clip[][] grid;

    int activeRow = 7;
    int activeColumn = 0;

    boolean soloMode = true;

    ClipViewerControl[] clipViewer;

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);

        // get controllers
        clipController = this.sketch.getClips();
        midiController = this.sketch.getMidi();
        grid = clipController.getClipGrid();

        grid[7][0] = new Clip(sketch, ContentUtil.getContent("visuals/tunnel_enc.mov"));
        grid[7][0].setPlayMode(new HoldMode());
        grid[7][1] = new Clip(sketch, ContentUtil.getContent("visuals/circle_enc.mov"));
        grid[7][1].setPlayMode(new OneShotMode());
        grid[7][2] = new Clip(sketch, ContentUtil.getContent("visuals/starfall_enc.mov"));
        grid[7][3] = new Clip(sketch, ContentUtil.getContent("visuals/tunnel_enc.mov"));

        midiController.clearLEDs();

        initMidi();
        initScene();
        initListener();
    }

    void initScene()
    {
        launchScene = new Scene();
        sketch.getUi().setActiveScene(launchScene);

        // init clip preview
        clipViewer = new ClipViewerControl[ClipController.GRID_SIZE];
        for(int i = 0; i < clipViewer.length; i++)
        {
            clipViewer[i] = new ClipViewerControl(20 + (100 * i), 90, 80, 60);
            launchScene.addControl(clipViewer[i]);
        }
        updateClipViewer();

        switchColumn(activeColumn);
        switchRow(activeRow);
        switchSoloMode();
    }

    void initListener()
    {
        for(int i = 36; i <= 99; i++) {
            Clip c = getClipByNumber(i);
            if(c != null)
                c.addStateListener(this);
        }
    }

    void updateClipViewer()
    {
        for(int i = 0; i < clipViewer.length; i++)
        {
            clipViewer[i].setClip(grid[activeRow][i]);
        }
    }

    void initMidi()
    {
        // solo mode button
        new ControlChangeHandler(0, 61)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127)
                    switchSoloMode();
            }
        }.registerMidiEvent(sketch.getMidi());

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
        for(int i = 36; i <= 99; i++) {
            new NoteChangeHandler(0, i) {
                @Override
                public void noteOn(int channel, int number, int value) {
                    Clip c = getClipByNumber(number);

                    if (c == null)
                        return;

                    // solo mode
                    if(soloMode && c.getPlayMode() instanceof LoopMode)
                        applySoloMode(c);

                    c.getPlayMode().onTriggered(c, clipController);

                    // set button color
                    setPadColor(number, getPadColor(c));
                }

                @Override
                public void noteOff(int channel, int number, int value) {
                    Clip c = getClipByNumber(number);

                    if (c == null)
                        return;

                    c.getPlayMode().offTriggered(c, clipController);

                    // set button color
                    setPadColor(number, getPadColor(c));
                }
            }.registerMidiEvent(sketch.getMidi());

            // set initial color
            Clip c = getClipByNumber(i);

            if(c != null) {
                setPadColor(i, getPadColor(c));
            }
        }
    }

    PushColor getPadColor(Clip c)
    {
        int pulsing = c.isPlaying() ? PLAY_PULSING : DEFAULT_PULSING;
        int color = 0;

        if(c.getPlayMode() instanceof HoldMode)
            color = HOLD_MODE_COLOR;

        if(c.getPlayMode() instanceof LoopMode)
            color = LOOP_MODE_COLOR;

        if(c.getPlayMode() instanceof OneShotMode)
            color = ONE_SHOT_MODE_COLOR;

        return new PushColor(color, pulsing);
    }

    Clip getClipByNumber(int number)
    {
        int column = (number - START_PAD_MIDI) / ClipController.GRID_SIZE;
        int row = (number - START_PAD_MIDI) % ClipController.GRID_SIZE;

        return grid[column][row];
    }

    /**
     * Warning: This is a very slow search!
     * @param c Clip to search for!
     * @return Pad number of the clip.
     */
    int getNumberByClip(Clip c)
    {
        for(int u = 0; u < grid.length; u++)
            for(int v = 0; v < grid[u].length; v++)
                if(grid[u][v] == c)
                    return ((u * grid.length) + v) + START_PAD_MIDI;

        return -1;
    }

    void applySoloMode(Clip clip)
    {
        for(int u = 0; u < grid.length; u++) {
            for (int v = 0; v < grid[u].length; v++) {
                Clip c = grid[u][v];
                if(c != null && c != clip && c.getPlayMode() instanceof LoopMode && c.isPlaying())
                {
                    int index = ((u * grid.length) + v) + START_PAD_MIDI;
                    midiController.emulateNoteOn(0, index, 127);
                }
            }
        }
    }

    void switchRow(int newNumber)
    {
        midiController.sendControllerChange(0, activeRow + START_ROW_MIDI, 0);
        activeRow = newNumber;
        midiController.sendControllerChange(1, activeRow + START_ROW_MIDI, COLUMN_ROW_SELECTOR_COLOR);

        updateClipViewer();
    }

    void switchColumn(int newNumber)
    {
        midiController.sendControllerChange(0, activeColumn + START_COLUMN_MIDI, 0);
        activeColumn = newNumber;
        midiController.sendControllerChange(1, activeColumn + START_COLUMN_MIDI, COLUMN_ROW_SELECTOR_COLOR);
    }

    void switchSoloMode()
    {
        midiController.sendControllerChange(0, SOLO_BUTTON, 0);
        soloMode = !soloMode;

        if(soloMode)
            midiController.sendControllerChange(9, SOLO_BUTTON, RGBColor.GREEN().getColor());
        else
            midiController.sendControllerChange(1, SOLO_BUTTON, RGBColor.WHITE().getColor());
    }

    void setPadColor(int number, PushColor color)
    {
        midiController.sendNoteOn(0, number, 0);
        midiController.sendNoteOn(color.getPulsing(), number, color.getColor());
    }

    public void update()
    {
    }

    @Override
    public void clipEnded(Clip clip) {
        if(clip.getPlayMode() instanceof OneShotMode)
        {
            clipController.deactivateClip(clip);

            int number = getNumberByClip(clip);
            if(number != -1)
                setPadColor(number, getPadColor(clip));
        }
    }
}
