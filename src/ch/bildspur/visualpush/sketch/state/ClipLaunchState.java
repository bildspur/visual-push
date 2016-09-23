package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.NoteChangeHandler;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.push.PushMidi;
import ch.bildspur.visualpush.push.color.PushColor;
import ch.bildspur.visualpush.push.color.RGBColor;
import ch.bildspur.visualpush.sketch.controller.ClipController;
import ch.bildspur.visualpush.sketch.controller.MidiController;
import ch.bildspur.visualpush.ui.*;
import ch.bildspur.visualpush.video.BlendMode;
import ch.bildspur.visualpush.video.Clip;
import ch.bildspur.visualpush.video.event.ClipStateListener;
import ch.bildspur.visualpush.video.playmode.HoldMode;
import ch.bildspur.visualpush.video.playmode.LoopMode;
import ch.bildspur.visualpush.video.playmode.OneShotMode;
import ch.bildspur.visualpush.video.playmode.PlayMode;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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
    public static final int SHIFT_BUTTON = 49;
    public static final int CLIP_BUTTON = 113;

    public static final float CONTROL_HEIGHT = 12;

    ClipController clipController;
    MidiController midiController;
    Scene launchScene;
    Clip[][] grid;

    int activeRow = 7;
    int activeColumn = 0;

    boolean soloMode = false;
    boolean previewMode = false;
    boolean showAddClip = false;

    // controls
    ClipViewerControl[] clipViewer;

    LabelControl[] clipNameLabels;

    FaderListControl playModeList;
    FaderListControl blendModeList;

    FaderControl opacitiyControl;
    FaderControl zoomControl;
    FaderControl speedControl;
    FaderControl redTintControl;
    FaderControl greenTintControl;
    FaderControl blueTintControl;

    List<MidiEventListener> midiListener = new ArrayList<>();

    boolean isInitialised = false;

    PushState nextState = new NullState();

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);

        // get controllers
        clipController = this.sketch.getClips();
        midiController = this.sketch.getMidi();
        grid = clipController.getClipGrid();

        midiController.clearLEDs();

        // only do init the first time
        if(!isInitialised) {
            initMidi();
            initScene();

            isInitialised = true;
            running = true;
        }
        else {
            initListener();
            updatePadColors();
        }

        // set initial values
        switchColumn(activeColumn);
        switchRow(activeRow);

        lightUpLEDs();

        // set scene
        this.sketch.getUi().setActiveScene(launchScene);

        // init midi listeners
        registerMidiListeners();
    }

    void registerMidiListeners()
    {
        for(MidiEventListener listener : midiListener)
            listener.registerMidiEvent(midiController);
    }

    void removeMidiListeners()
    {
        for(MidiEventListener listener : midiListener)
            listener.removeMidiEvent(midiController);
    }

    void initScene()
    {
        launchScene = new Scene();

        // init clip preview
        clipViewer = new ClipViewerControl[ClipController.GRID_SIZE];
        for(int i = 0; i < clipViewer.length; i++)
        {
            clipViewer[i] = new ClipViewerControl(5 + (120 * i), 79, 105, 80);
            clipViewer[i].setFillColor(Color.CYAN);
            launchScene.addControl(clipViewer[i]);
        }

        // init clip names
        clipNameLabels = new LabelControl[ClipController.GRID_SIZE];
        for(int i = 0; i < clipNameLabels.length; i++)
        {
            clipNameLabels[i] = new LabelControl(new DataModel<>(""));
            clipNameLabels[i].setPosition(new PVector(5 + (120 * i), 60));
            clipNameLabels[i].setTextSize(10);
            launchScene.addControl(clipNameLabels[i]);
        }

        updateClipViewer();

        createClipControls();
        updateClipControls();
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
            clipViewer[i].setHighlighted(i == activeColumn);
        }

        for(int i = 0; i < clipViewer.length; i++)
        {
            Clip clip = grid[activeRow][i];

            if(clip != null)
                clipNameLabels[i].setModel(clip.getClipName());
            else
                clipNameLabels[i].setModel(new DataModel<>(""));
        }
    }

    void updateClipControls()
    {
        Clip clip = grid[activeRow][activeColumn];
        if(clip != null)
        {
            playModeList.selectItemByValue(clip.getPlayMode().getValue().getIntValue());
            blendModeList.selectItemByValue(clip.getBlendMode().getValue().getIntValue());

            opacitiyControl.setModel(clip.getOpacity());
            zoomControl.setModel(clip.getZoom());
            speedControl.setModel(clip.getSpeed());

            redTintControl.setModel(clip.getRedTint());
            greenTintControl.setModel(clip.getGreenTint());
            blueTintControl.setModel(clip.getBlueTint());
        }
    }

    void createClipControls()
    {
        // setup play modes
        ArrayList<ListElement> playModeItems = new ArrayList<>();
        playModeItems.add(new ListElement(0, "LOOP"));
        playModeItems.add(new ListElement(1, "HOLD"));
        playModeItems.add(new ListElement(2, "ONE SHOT"));

        // setup blend modes
        ArrayList<ListElement> blendModeItems = new ArrayList<>();
        for(BlendMode m : BlendMode.class.getEnumConstants())
            blendModeItems.add(new ListElement(m.getIntValue(), m.name()));

        // setup controls
        playModeList = new FaderListControl(new DataModel<>(0), playModeItems, 0, 71, 0, 0);
        playModeList.setPosition(new PVector(5, CONTROL_HEIGHT));
        playModeList.setFillColor(Color.CYAN);
        playModeList.getModel().addListener(value -> {
            Clip clip = grid[activeRow][activeColumn];
            if(clip != null) {
                if(clip.getPlayMode().getValue().getIntValue() == value)
                    return;

                clip.getPlayMode().setValue(PlayMode.fromInteger(value));
                setPadColor(START_PAD_MIDI + (activeRow * grid.length) + activeColumn, getPadColor(clip));
            }
        });
        midiListener.add(playModeList);

        blendModeList = new FaderListControl(new DataModel<>(0), blendModeItems, 0, 72, 0, 1);
        blendModeList.setPosition(new PVector(125, CONTROL_HEIGHT));
        blendModeList.setFillColor(Color.CYAN);
        blendModeList.getModel().addListener(value -> {
            Clip clip = grid[activeRow][activeColumn];
            if(clip != null) {
                clip.getBlendMode().setValue(BlendMode.fromInteger((int)blendModeItems.get(value).getValue()));
            }
        });
        midiListener.add(blendModeList);

        opacitiyControl = new FaderControl(new DataModel<>(0f), 0, 73, 0, 2);
        opacitiyControl.setPosition(new PVector(245, CONTROL_HEIGHT));
        opacitiyControl.setFillColor(Color.decode("#56FF64"));
        midiListener.add(opacitiyControl);


        zoomControl = new FaderControl(new DataModel<>(0f), 0, 74, 0, 3);
        zoomControl.setPosition(new PVector(365, CONTROL_HEIGHT));
        zoomControl.setStepValue(0.1f);
        zoomControl.setMinimumValue(-5f);
        zoomControl.setMaximumValue(5f);
        midiListener.add(zoomControl);

        speedControl = new FaderControl(new DataModel<>(0f), 0, 75, 0, 4);
        speedControl.setPosition(new PVector(485, CONTROL_HEIGHT));
        speedControl.setStepValue(0.1f);
        speedControl.setMinimumValue(0f);
        speedControl.setMaximumValue(5f);
        speedControl.setFillColor(Color.decode("#269AE0"));
        midiListener.add(speedControl);

        // tint controls
        redTintControl = new FaderControl(new DataModel<>(0f), 0, 76, 0, 5);
        redTintControl.setPosition(new PVector(605, CONTROL_HEIGHT));
        redTintControl.setFillColor(Color.RED);
        midiListener.add(redTintControl);

        greenTintControl = new FaderControl(new DataModel<>(0f), 0, 77, 0, 6);
        greenTintControl.setPosition(new PVector(725, CONTROL_HEIGHT));
        greenTintControl.setFillColor(Color.GREEN);
        midiListener.add(greenTintControl);

        blueTintControl = new FaderControl(new DataModel<>(0f), 0, 78, 0, 7);
        blueTintControl.setPosition(new PVector(845, CONTROL_HEIGHT));
        blueTintControl.setFillColor(Color.BLUE);
        midiListener.add(blueTintControl);


        // add global opacity fader
        /*
        FaderControl globalOpacity = new FaderControl(sketch.getGlobalOpacity(), 0, 78, 0, 7);
        globalOpacity.setPosition(new PVector(845, CONTROL_HEIGHT));
        globalOpacity.setFillColor(Color.decode("#23D5A7"));
        midiListener.add(globalOpacity);
        */

        // add labels
        ArrayList<LabelControl> labels = new ArrayList<LabelControl>(){{
            add(new LabelControl(new DataModel<>("PLAY MODE")));
            add(new LabelControl(new DataModel<>("BLEND MODE")));
            add(new LabelControl(new DataModel<>("OPACITY")));
            add(new LabelControl(new DataModel<>("ZOOM")));
            add(new LabelControl(new DataModel<>("SPEED")));
            add(new LabelControl(new DataModel<>("RED")));
            add(new LabelControl(new DataModel<>("GREEN")));
            add(new LabelControl(new DataModel<>("BLUE")));
        }};

        for(int i = 0; i < labels.size(); i++)
        {
            LabelControl l = labels.get(i);
            l.setTextSize(8);
            l.setPosition(new PVector(5 + (i * 120), 0));
            launchScene.addControl(l);
        }

        // add controls to launch scene
        launchScene.addControl(playModeList);
        launchScene.addControl(blendModeList);
        launchScene.addControl(opacitiyControl);
        launchScene.addControl(zoomControl);
        launchScene.addControl(speedControl);
        launchScene.addControl(redTintControl);
        launchScene.addControl(greenTintControl);
        launchScene.addControl(blueTintControl);
    }

    void initMidi()
    {
        // preview mode button
        midiListener.add(new ControlChangeHandler(0, SHIFT_BUTTON)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                previewMode = value == 127;
            }
        });

        // solo mode button
        midiListener.add(new ControlChangeHandler(0, SOLO_BUTTON)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127)
                    switchSoloMode();
            }
        });

        // add clip button
        midiListener.add(new ControlChangeHandler(0, CLIP_BUTTON)
        {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127)
                    showAddClip = true;
            }
        });

        // column selectors
        for(int i = 0; i < ClipController.GRID_SIZE; i++)
            midiListener.add(new ControlChangeHandler(0, i + START_COLUMN_MIDI)
            {
                @Override
                public void controlChange(int channel, int number, int value) {
                    if(value == 127)
                        switchColumn(number - START_COLUMN_MIDI);
                }
            });

        // row selectors
        for(int i = 0; i < ClipController.GRID_SIZE; i++)
            midiListener.add(new ControlChangeHandler(0, i + START_ROW_MIDI)
            {
                @Override
                public void controlChange(int channel, int number, int value) {
                    if(value == 127)
                        switchRow(number - START_ROW_MIDI);
                }
            });

        // add pad handler
        for(int i = 36; i <= 99; i++) {
            midiListener.add(new NoteChangeHandler(0, i) {
                @Override
                public void noteOn(int channel, int number, int value) {
                    Clip c = getClipByNumber(number);

                    if (c == null)
                        return;

                    if(previewMode) {
                        c.play();
                        return;
                    }

                    // solo mode
                    if(soloMode && c.getPlayMode().getValue() instanceof LoopMode && !c.isPlaying())
                        applySoloMode(c);

                    c.getPlayMode().getValue().onTriggered(c, clipController);

                    // set button color
                    setPadColor(number, getPadColor(c));
                }

                @Override
                public void noteOff(int channel, int number, int value) {
                    Clip c = getClipByNumber(number);

                    if (c == null)
                        return;

                    if (previewMode) {
                        c.stop();
                        return;
                    }

                    c.getPlayMode().getValue().offTriggered(c, clipController);

                    // set button color
                    setPadColor(number, getPadColor(c));
                }
            });
        }

        // updatePadColors
        updatePadColors();

        // add
        PushMidi.setupTouchStrip(midiController.getBus());
    }

    void updatePadColors()
    {
        for(int i = 36; i <= 99; i++) {
            // set initial color
            Clip c = getClipByNumber(i);

            if (c != null) {
                setPadColor(i, getPadColor(c));
            }
        }
    }

    void showDialogState(PushState pushState)
    {
        showAddClip = false;
        nextState = pushState;
        removeMidiListeners();
        running = false;
    }

    @Override
    public PushState getNextState()
    {
        return nextState;
    }

    PushColor getPadColor(Clip c)
    {
        int pulsing = c.isPlaying() ? PLAY_PULSING : DEFAULT_PULSING;
        int color = 0;

        if(c.getPlayMode().getValue() instanceof HoldMode)
            color = HOLD_MODE_COLOR;

        if(c.getPlayMode().getValue() instanceof LoopMode)
            color = LOOP_MODE_COLOR;

        if(c.getPlayMode().getValue() instanceof OneShotMode)
            color = ONE_SHOT_MODE_COLOR;

        return new PushColor(color, pulsing);
    }

    Clip getClipByNumber(int number)
    {
        int row = (number - START_PAD_MIDI) / ClipController.GRID_SIZE;
        int column = (number - START_PAD_MIDI) % ClipController.GRID_SIZE;

        return grid[row][column];
    }

    /**
     * Warning: This is a very slow search!
     * @param c Clip to search for!
     * @return Pad number of the clip.
     */
    int getNumberByClip(Clip c)
    {
        for(int u = 0; u < grid.length; u++) {
            for (int v = 0; v < grid[u].length; v++) {
                if (grid[u][v] != null && grid[u][v].getClipNumber() == c.getClipNumber()) {
                    return ((u * grid.length) + v) + START_PAD_MIDI;
                }
            }
        }

        return -1;
    }

    void applySoloMode(Clip clip)
    {
        for(int u = 0; u < grid.length; u++) {
            for (int v = 0; v < grid[u].length; v++) {
                Clip c = grid[u][v];
                if(c != null && c != clip && c.getPlayMode().getValue() instanceof LoopMode && c.isPlaying())
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
        updateClipControls();
    }

    void switchColumn(int newNumber)
    {
        midiController.sendControllerChange(0, activeColumn + START_COLUMN_MIDI, 0);
        activeColumn = newNumber;
        midiController.sendControllerChange(1, activeColumn + START_COLUMN_MIDI, COLUMN_ROW_SELECTOR_COLOR);

        updateClipViewer();
        updateClipControls();
    }

    void lightUpLEDs()
    {
        midiController.sendControllerChange(1, CLIP_BUTTON, RGBColor.WHITE().getColor());
        midiController.sendControllerChange(1, SHIFT_BUTTON, RGBColor.WHITE().getColor());

        if(soloMode)
            midiController.sendControllerChange(9, SOLO_BUTTON, RGBColor.GREEN().getColor());
        else
            midiController.sendControllerChange(1, SOLO_BUTTON, RGBColor.WHITE().getColor());
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
        if(showAddClip)
            showDialogState(new AddClipState(ClipLaunchState.this, START_PAD_MIDI + (activeRow * grid.length) + activeColumn));
    }

    @Override
    public void clipEnded(Clip clip) {
        if(clip.getPlayMode().getValue() instanceof OneShotMode)
        {
            clipController.deactivateClip(clip);
            clip.stop();

            int number = getNumberByClip(clip);
            if(number != -1)
                setPadColor(number, getPadColor(clip));
        }
    }
}
