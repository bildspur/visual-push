package ch.bildspur.visualpush.sketch.state;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Created by cansik on 17/08/16.
 */
public abstract class PushState {
    PApplet sketch;
    PGraphics screen;
    boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    public void setup(PApplet sketch, PGraphics screen)
    {
        this.sketch = sketch;
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
