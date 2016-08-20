package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.event.ButtonHandler;
import ch.bildspur.visualpush.event.PadHandler;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.ui.EncoderControl;
import ch.bildspur.visualpush.util.ContentUtil;
import ch.bildspur.visualpush.util.ImageUtil;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.video.Movie;

import java.awt.*;
import java.util.*;

/**
 * Created by cansik on 20/08/16.
 */
public class ExampleState extends PushState {
    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);
        setupUI();
    }

    void setupUI()
    {
        // test things
        // todo: clean this up

        // encoder test
        for(int i = 0; i < 8; i++) {
            EncoderControl c = new EncoderControl(0, i + 71, 0, i);
            c.setPosition(new PVector(60 + (120 * i), 70));
            c.registerMidiEvent(sketch.getMidi());

            // set style
            c.setFillColor(new Color(1, 176, 240));
            //c.setFillColor(Color.getHSBColor(i * (1.0f / 8), 1f, 0.94f));
            c.setStrokeColor(new Color(255, 255, 255));

            sketch.getUi().activeScene.addControl(c);
        }

        // first event listener test
        java.util.List<MidiEventListener> listeners =  new ArrayList<>();
        for(int i = 36; i < 100; i++)
            listeners.add(new PadHandler(0, i));

        for(int i = 20; i < 28; i++)
            listeners.add(new ButtonHandler(0, i));

        for(MidiEventListener l : listeners)
            l.registerMidiEvent(sketch.getMidi());
    }

    public void update()
    {
    }
}
