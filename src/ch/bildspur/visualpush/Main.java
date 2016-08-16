package ch.bildspur.visualpush;

import ch.bildspur.visualpush.sketch.RenderSketch;
import processing.core.PApplet;

/**
 * Created by cansik on 16/08/16.
 */
public class Main {
    public static void main(String... args) {
        RenderSketch sketch = new RenderSketch();
        PApplet.runSketch(new String[]{"RenderSketch "}, sketch);
    }
}
