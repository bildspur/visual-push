package ch.bildspur.visualpush.event;

import ch.bildspur.visualpush.event.midi.MidiEventListener;

/**
 * Created by cansik on 20/08/16.
 */
public abstract class BasicEventHandler implements MidiEventListener {
    int channel;
    int number;

    public BasicEventHandler(int channel, int number)
    {
        this.channel = channel;
        this.number = number;
    }
}
