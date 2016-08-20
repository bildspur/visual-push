package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.sketch.RenderSketch;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Created by cansik on 17/08/16.
 */
public abstract class PushState {
    RenderSketch sketch;
    PGraphics screen;
    boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    public void setup(PApplet sketch, PGraphics screen)
    {
        this.sketch = (RenderSketch)sketch;
        this.screen = screen;
        isRunning = true;
    }

    public void update() {}

    public void stop() {}

    public PushState getNextState()
    {
        return new NullState();
    }
}
