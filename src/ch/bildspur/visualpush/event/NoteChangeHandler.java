package ch.bildspur.visualpush.event;

import ch.bildspur.visualpush.event.midi.MidiCommand;
import ch.bildspur.visualpush.event.midi.MidiEvent;
import ch.bildspur.visualpush.event.midi.MidiEventCoordinator;
import ch.bildspur.visualpush.event.midi.MidiEventListener;

import javax.sound.midi.ShortMessage;

/**
 * Created by cansik on 20/08/16.
 */
public abstract class NoteChangeHandler extends BasicEventHandler {
    MidiEvent noteOnEvent;
    MidiEvent noteOffEvent;

    public NoteChangeHandler(int channel, int number)
    {
        super(channel, number);
        noteOnEvent = new MidiEvent(MidiCommand.NoteOn, channel, number);
        noteOffEvent = new MidiEvent(MidiCommand.NoteOff, channel, number);
    }

    @Override
    public void registerMidiEvent(MidiEventCoordinator coordinator) {
        coordinator.addMidiEventListener(this, noteOnEvent);
        coordinator.addMidiEventListener(this, noteOffEvent);
    }

    @Override
    public void removeMidiEvent(MidiEventCoordinator coordinator) {
        coordinator.removeMidiEventListener(this, noteOnEvent);
        coordinator.removeMidiEventListener(this, noteOffEvent);
    }

    @Override
    public void midiEvent(MidiEvent event) {
        if(event.equals(noteOnEvent)) {
            ShortMessage msg = event.getShort();
            noteOn(msg.getChannel(), msg.getData1(), msg.getData2());
        }

        if(event.equals(noteOffEvent)) {
            ShortMessage msg = event.getShort();
            noteOff(msg.getChannel(), msg.getData1(), msg.getData2());
        }
    }

    abstract void noteOn(int channel, int number, int value);

    abstract void noteOff(int channel, int number, int value);
}
