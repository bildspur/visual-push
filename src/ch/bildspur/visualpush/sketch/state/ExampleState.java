package ch.bildspur.visualpush.sketch.state;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.event.ButtonHandler;
import ch.bildspur.visualpush.event.PadHandler;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.sketch.controller.MidiController;
import ch.bildspur.visualpush.ui.ClipViewerControl;
import ch.bildspur.visualpush.ui.EncoderControl;
import ch.bildspur.visualpush.util.ContentUtil;
import ch.bildspur.visualpush.util.ImageUtil;
import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.video.Movie;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by cansik on 20/08/16.
 */
public class ExampleState extends PushState {
    List<Clip> clipList = new ArrayList<>();

    Clip tunnel;
    Clip circle;
    Clip beeple;

    public void setup(PApplet sketch, PGraphics screen)
    {
        super.setup(sketch, screen);
        setupUI();
    }

    void setupUI()
    {
        // first event listener test
        java.util.List<MidiEventListener> listeners =  new ArrayList<>();

        // test things
        // todo: clean this up
        tunnel = new Clip(sketch, ContentUtil.getContent("visuals/tunnel_enc.mov"));
        circle = new Clip(sketch, ContentUtil.getContent("visuals/circle_enc.mov"));
        beeple = new Clip(sketch, ContentUtil.getContent("visuals/starfall_enc.mov"));

        clipList.add(tunnel);
        clipList.add(circle);
        clipList.add(beeple);

        // add ui
        sketch.getUi().activeScene.addControl(new ClipViewerControl(tunnel, 20, 90, 80, 60));
        sketch.getUi().activeScene.addControl(new ClipViewerControl(circle, 120, 90, 80, 60));
        sketch.getUi().activeScene.addControl(new ClipViewerControl(beeple, 220, 90, 80, 60));

        // add controllers
        listeners.add(new PadHandler(0, 92) {
            @Override
            public void noteOn(int channel, int number, int value) {
                super.noteOn(channel, number, value);
                tunnel.loop();
                System.out.println("play tunnel!");
            }

            @Override
            public void noteOff(int channel, int number, int value) {
                super.noteOff(channel, number, value);
                tunnel.stop();
                MidiController.bus.sendNoteOn(9, 92, 127);
                System.out.println("stop tunnel!");
            }
        });

        listeners.add(new PadHandler(0, 93) {
            @Override
            public void noteOn(int channel, int number, int value) {
                super.noteOn(channel, number, value);
                circle.loop();
                System.out.println("play circle!");
            }

            @Override
            public void noteOff(int channel, int number, int value) {
                super.noteOff(channel, number, value);
                circle.stop();
                MidiController.bus.sendNoteOn(9, 93, 127);
                System.out.println("stop circle!");
            }
        });

        listeners.add(new PadHandler(0, 94) {
            @Override
            public void noteOn(int channel, int number, int value) {
                super.noteOn(channel, number, value);
                beeple.loop();
                System.out.println("play beeple!");
            }

            @Override
            public void noteOff(int channel, int number, int value) {
                super.noteOff(channel, number, value);
                beeple.stop();
                MidiController.bus.sendNoteOn(9, 94, 127);
                System.out.println("stop beeple!");
            }
        });

                /*
        for(int i = 36; i < 100; i++)
            listeners.add(new PadHandler(0, i));
            */

        for(int i = 20; i < 28; i++)
            listeners.add(new ButtonHandler(0, i));

        for(MidiEventListener l : listeners)
            l.registerMidiEvent(sketch.getMidi());

        // encoder test
        DataModel<Integer> model = new DataModel<>(0);
        for(int i = 0; i < 8; i++) {
            EncoderControl c = new EncoderControl(model, 0, i + 71, 0, i);
            c.setPosition(new PVector(20 + (120 * i), 40));
            c.registerMidiEvent(sketch.getMidi());

            // set style
            c.setFillColor(new Color(1, 176, 240));
            //c.setFillColor(Color.getHSBColor(i * (1.0f / 8), 1f, 0.94f));
            c.setStrokeColor(new Color(255, 255, 255));

            sketch.getUi().activeScene.addControl(c);
        }
    }

    public void update()
    {
        if(tunnel.isPlaying())
            sketch.image(tunnel, 0, 0);
            //sketch.blend(tunnel, 0, 0, tunnel.width, tunnel.height, 0, 0, tunnel.width, tunnel.height, PApplet.SCREEN);

        if(beeple.isPlaying())
            sketch.image(beeple, 0, 0);

        if(circle.isPlaying())
            sketch.image(circle, 0, 0);
            //sketch.blend(circle, 0, 0, circle.width, circle.height, 0, 0, circle.width, circle.height, PApplet.SCREEN);
    }
}
