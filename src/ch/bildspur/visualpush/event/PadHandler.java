package ch.bildspur.visualpush.event;

import ch.bildspur.visualpush.sketch.controller.MidiController;

/**
 * Created by cansik on 20/08/16.
 */
public class PadHandler extends NoteChangeHandler {

    public PadHandler(int channel, int number) {
        super(channel, number);
    }

    @Override
    public void noteOn(int channel, int number, int value) {
    }

    @Override
    public void noteOff(int channel, int number, int value) {

    }
}
