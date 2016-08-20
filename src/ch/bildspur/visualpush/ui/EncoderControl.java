package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.NoteChangeHandler;
import ch.bildspur.visualpush.event.midi.MidiEvent;
import ch.bildspur.visualpush.event.midi.MidiEventCoordinator;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Created by cansik on 20/08/16.
 */
public class EncoderControl extends UIControl implements MidiEventListener {
    private ControlChangeHandler ccHandler;
    private NoteChangeHandler nnHandler;

    int value = 0;

    boolean isActive = false;

    public EncoderControl(int ccChannel, int ccNumber, int nnChannel, int nnNumber)
    {
        super();

        width = 50;
        height = 50;

        // set up ccHandler
        ccHandler = new ControlChangeHandler(ccChannel, ccNumber) {
            @Override
            public void controlChange(int channel, int number, int value) {
                EncoderControl.this.controlChange(channel, number, value);
            }
        };

        nnHandler = new NoteChangeHandler(nnChannel, nnNumber) {
            @Override
            public void noteOn(int channel, int number, int value) {
                isActive = true;
            }

            @Override
            public void noteOff(int channel, int number, int value) {
                isActive = false;
            }
        };
    }

    public void controlChange(int channel, int number, int value) {
        if(value == 127)
            this.value -= 1;
        else if(value == 1)
            this.value += 1;

        if(this.value > 255)
            this.value = 255;

        if(this.value < 0)
            this.value = 0;
    }

    public void paint(PGraphics g)
    {
        PVector pos = getAbsolutePosition();

        float diameter = 80;
        float start = -240;
        float end = 60;
        float angle = PApplet.map(value, 0, 255, 0, 300);

        int c = isActive ? this.strokeColor.getRGB() : this.strokeColor.darker().getRGB();

        if(isActive)
            g.fill(this.fillColor.getRGB());
        else
            g.fill(this.fillColor.darker().getRGB());

        // draw value
        g.noStroke();
        g.strokeWeight(1.5f);
        g.arc(pos.x, pos.y, diameter, diameter, PApplet.radians(start), PApplet.radians(start + angle), PApplet.PIE);

        // draw border
        g.stroke(c);
        g.noFill();
        g.arc(pos.x, pos.y, diameter, diameter, PApplet.radians(start), PApplet.radians(end), PApplet.PIE);
        g.fill(0);
        g.arc(pos.x, pos.y, diameter/2, diameter/2, PApplet.radians(start), PApplet.radians(end), PApplet.PIE);
        g.noStroke();
        g.ellipse(pos.x, pos.y, diameter/2-1, diameter/2-1);

        g.fill(c);
        g.textAlign(PApplet.CENTER, PApplet.CENTER);
        g.text(value, pos.x, pos.y);

        super.paint(g);
    }

    @Override
    public void registerMidiEvent(MidiEventCoordinator coordinator) {
        ccHandler.registerMidiEvent(coordinator);
        nnHandler.registerMidiEvent(coordinator);
    }

    @Override
    public void removeMidiEvent(MidiEventCoordinator coordinator) {
        ccHandler.removeMidiEvent(coordinator);
        nnHandler.removeMidiEvent(coordinator);
    }

    @Override
    public void midiEvent(MidiEvent event) {
        ccHandler.midiEvent(event);
        nnHandler.midiEvent(event);
    }
}
