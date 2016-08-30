package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Stack;

/**
 * Created by cansik on 26/08/16.
 */
public class ClipController extends ProcessingController {
    public static final int GRID_SIZE = 8;

    Stack<Clip> activeClips = new Stack<>();
    Clip[][] clipGrid;

    public void setup(PApplet sketch) {
        super.setup(sketch);

        initClipGrid();
    }

    public Stack<Clip> getActiveClips() {
        return activeClips;
    }

    public Clip[] getImmutableClips()
    {
        Clip[] clips = new Clip[activeClips.size()];
        activeClips.copyInto(clips);
        return clips;
    }

    public Clip[][] getClipGrid() {
        return clipGrid;
    }

    public void initClipGrid()
    {
        clipGrid = new Clip[GRID_SIZE][];
        for(int i = 0; i < clipGrid.length; i++)
            clipGrid[i] = new Clip[GRID_SIZE];
    }

    public void activateClip(Clip c)
    {
        if(activeClips.contains(c))
            deactivateClip(c);

        activeClips.push(c);
    }

    public void deactivateClip(Clip c)
    {
        activeClips.removeElement(c);
    }
}
