package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.push.color.RGBColor;
import ch.bildspur.visualpush.sketch.controller.ClipController;
import ch.bildspur.visualpush.ui.*;
import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by cansik on 31/08/16.
 */
public class AddClipState extends PushState {
    public static final int START_PAD_MIDI = 36;

    PushState parentState;
    Scene addClipScene;

    List<MidiEventListener> midiListener;
    ArrayList<ListElement> clipFiles;

    FaderListControl clipSelector;

    public static final int CLIP_BUTTON = 113;

    ButtonControl upButton;
    ButtonControl enterButton;

    ButtonControl addButton;
    ButtonControl addAllButton;
    ButtonControl removeButton;

    LabelControl currentDirControl;

    DataModel<String> currentDirText = new DataModel<>("Current Directory");

    Path currentDir;

    boolean returnToParentState = false;

    int padNumber;

    public AddClipState(PushState parentState, int padNumber)
    {
        this.parentState = parentState;
        this.padNumber = padNumber;
    }

    public void setup(PApplet sketch, PGraphics screen) {
        super.setup(sketch, screen);

        midiListener = new ArrayList<>();
        clipFiles = new ArrayList<>();

        this.returnToParentState = false;

        this.sketch.getMidi().clearLEDs();
        this.sketch.getMidi().sendControllerChange(9, CLIP_BUTTON, RGBColor.WHITE().getColor());

        // set current clip blinking
        this.sketch.getMidi().sendNoteOn(9, padNumber, RGBColor.PINK().getColor());

        addClipScene = new Scene();

        // setup ui
        currentDirControl = new LabelControl(currentDirText);
        currentDirControl.setPosition(new PVector(1, 1));
        addClipScene.addControl(currentDirControl);

        upButton = new ButtonControl("UP", 0, false, this.sketch.getMidi(), (s -> {
            if(currentDir.getParent() != null)
                loadNewDirectory(currentDir.getParent());
            else
                PApplet.println("Can't go up!");
        }));
        addClipScene.addControl(upButton);
        midiListener.add(upButton.getHandler());

        enterButton = new ButtonControl("ENTER", 1, false, this.sketch.getMidi(), (s -> {
            Path clipPath = (Path) clipFiles.get(clipSelector.getModel().getValue()).getValue();
            File file = new File(clipPath.toString());

            if(file.isDirectory())
                loadNewDirectory(clipPath);
            else
                PApplet.println("Not a directory!");
        }));
        addClipScene.addControl(enterButton);
        midiListener.add(enterButton.getHandler());

        addButton = new ButtonControl("ADD", 3, false, this.sketch.getMidi(), (s -> {
            addSelectedClip(true);
        }));
        addClipScene.addControl(addButton);
        midiListener.add(addButton.getHandler());

        addAllButton = new ButtonControl("ADD ALL", 4, false, this.sketch.getMidi(), (s -> {
            addAllClips();
        }));
        addClipScene.addControl(addAllButton);
        midiListener.add(addAllButton.getHandler());

        removeButton = new ButtonControl("REMOVE", 5, false, this.sketch.getMidi(), (s -> {
            removeCurrentClip();
        }));
        addClipScene.addControl(removeButton);
        midiListener.add(removeButton.getHandler());


        midiListener.add(new ControlChangeHandler(0, CLIP_BUTTON) {
            @Override
            public void controlChange(int channel, int number, int value) {
                if (value == 127) {
                    returnToParentState = true;
                }
            }
        });

        // setup controls
        clipSelector = new FaderListControl(new DataModel<>(0), clipFiles, 0, 71, 0, 0);
        clipSelector.setAlwaysActive(true);
        clipSelector.setPosition(new PVector(5, 20));
        clipSelector.setWidth(950);
        clipSelector.setHeight(110);
        clipSelector.setFillColor(Color.CYAN);
        midiListener.add(clipSelector);

        addClipScene.addControl(clipSelector);

        // set scene
        this.sketch.getUi().setActiveScene(addClipScene);

        registerMidiListeners();

        if(currentDir == null)
            currentDir = this.sketch.getConfig().getProject().getVisualPath();

        loadNewDirectory(currentDir);
    }

    void removeCurrentClip()
    {
        int row = (padNumber - START_PAD_MIDI) / ClipController.GRID_SIZE;
        int column = (padNumber - START_PAD_MIDI) % ClipController.GRID_SIZE;

        Clip clip = AddClipState.this.sketch.getClips().getClipGrid()[row][column];
        if (clip != null) {
            clip.stop();
            AddClipState.this.sketch.getClips().deactivateClip(clip);
            AddClipState.this.sketch.getClips().getClipGrid()[row][column] = null;
        }

        returnToParentState = true;
    }

    void addSelectedClip(boolean doReturn)
    {
        Path clipPath = (Path) clipFiles.get(clipSelector.getModel().getValue()).getValue();
        File file = new File(clipPath.toString());

        if(!file.isFile()) {
            PApplet.println("Not a file!");
            return;
        }

        int row = (padNumber - START_PAD_MIDI) / ClipController.GRID_SIZE;
        int column = (padNumber - START_PAD_MIDI) % ClipController.GRID_SIZE;

        // delete old clip if exists
        Clip clip = AddClipState.this.sketch.getClips().getClipGrid()[row][column];
        if (clip != null) {
            clip.stop();
            AddClipState.this.sketch.getClips().deactivateClip(clip);
            AddClipState.this.sketch.getClips().getClipGrid()[row][column] = null;
        }

        AddClipState.this.sketch.getClips().getClipGrid()[row][column] = new Clip(sketch, clipPath.toString());

        if(doReturn)
            returnToParentState = true;
    }

    void addAllClips()
    {
        int addCounter = 0;
        for(ListElement element : clipSelector.getItems())
        {
            Path path = (Path) element.getValue();
            File file = new File(path.toString());

            if(file.isDirectory())
                continue;

            // add file to pad position
            clipSelector.getModel().setValue(addCounter);
            addSelectedClip(false);

            padNumber++;
            if((padNumber - START_PAD_MIDI) % 8 == 0)
                padNumber -= 16;

            addCounter++;
        }

        returnToParentState = true;
    }

    void loadNewDirectory(Path dir)
    {
        currentDir = dir.toAbsolutePath();

        // load clips from folder
        clipFiles.clear();

        List<Path> files = fileList(currentDir.toString());
        Collections.sort(files, (o1, o2) -> o1.toString().compareTo(o2.toString()));

        for(Path path : files)
        {
            File file = new File(path.toString());
            if(file.isDirectory())
            {
                clipFiles.add(new ListElement(path,  path.getFileName().toString() + "/"));
            }
        }

        // add files
        int fileCount = 0;
        for (Path path : files) {
            File file = new File(path.toString());

            if(file.isFile() && file.toString().toLowerCase().endsWith(".mov")) {
                fileCount++;
                clipFiles.add(new ListElement(path, path.getFileName().toString()));
            }
        }

        clipSelector.getModel().setValue(0);
        currentDirText.setValue("Current (" + fileCount + " Files): " + currentDir.toString());
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

    public Path getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(Path currentDir) {
        this.currentDir = currentDir;
    }

    public int getPadNumber() {
        return padNumber;
    }

    public void setPadNumber(int padNumber) {
        this.padNumber = padNumber;
    }
}
