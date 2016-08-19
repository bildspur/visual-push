package ch.bildspur.visualpush.event;

import ch.bildspur.visualpush.event.midi.MidiEvent;

import javax.sound.midi.MidiMessage;

/**
 * Created by cansik on 18/08/16.
 */
public interface MidiEventListener {
    void midiEvent(MidiEvent event);
}
