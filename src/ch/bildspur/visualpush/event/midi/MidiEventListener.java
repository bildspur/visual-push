package ch.bildspur.visualpush.event.midi;

/**
 * Created by cansik on 18/08/16.
 */
public interface MidiEventListener {

    void registerMidiEvent(MidiEventCoordinator coordinator);

    void removeMidiEvent(MidiEventCoordinator coordinator);

    void midiEvent(MidiEvent event);
}
