package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.Constants;
import processing.core.PApplet;
import themidibus.MidiBus;

/**
 * Created by cansik on 16/08/16.
 */
public class MidiController extends ProcessingController {
    MidiBus bus;

    public void setup(PApplet sketch){
        super.setup(sketch);

        bus = new MidiBus(sketch, Constants.ABLETON_PUSH_MIDI_PORT, Constants.ABLETON_PUSH_MIDI_PORT);
    }

    public void noteOn(int channel, int pitch, int velocity) {
        // Receive a noteOn
        PApplet.println("NoteOn: C: " + channel + "\tP: " + pitch + "\tV: " + velocity);
    }

    public void noteOff(int channel, int pitch, int velocity) {
        // Receive a noteOff
        PApplet.println("NoteOff: C: " + channel + "\tP: " + pitch + "\tV: " + velocity);
    }

    public void controllerChange(int channel, int number, int value) {
        // Receive a controllerChange
        PApplet.println("CC: C: " + channel + "\tN: " + number + "\tV: " + value);
    }
}
