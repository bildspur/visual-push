package ch.bildspur.visualpush.event.midi;

/**
 * Created by cansik on 20/08/16.
 */
public interface MidiEventCoordinator {

    void addMidiEventListener(MidiEventListener listener, MidiEvent event);

    void removeMidiEventListener(MidiEventListener listener, MidiEvent event);
}
