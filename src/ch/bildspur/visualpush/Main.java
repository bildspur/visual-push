package ch.bildspur.visualpush;

import ch.bildspur.visualpush.push.DisplayDriver;
import ch.bildspur.visualpush.sketch.RenderSketch;
import processing.core.PApplet;

/**
 * Created by cansik on 16/08/16.
 */
public class Main {
    public static void main(String... args) {

        usbTest();

        RenderSketch sketch = new RenderSketch();
        PApplet.runSketch(new String[]{"RenderSketch "}, sketch);
    }

    static void usbTest()
    {
        try {
            DisplayDriver display = new DisplayDriver();
            display.test();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
