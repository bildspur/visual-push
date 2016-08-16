package ch.bildspur.visualpush.sketch.controller;

import processing.core.PApplet;

/**
 * Created by cansik on 16/08/16.
 */
public abstract class ProcessingController {
    protected PApplet sketch;

    public void setup(PApplet sketch){
        this.sketch = sketch;
    }
}
