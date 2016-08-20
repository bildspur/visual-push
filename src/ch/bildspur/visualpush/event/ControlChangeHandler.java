package ch.bildspur.visualpush.event;

import ch.bildspur.visualpush.event.midi.MidiCommand;
import ch.bildspur.visualpush.event.midi.MidiEvent;
import ch.bildspur.visualpush.event.midi.MidiEventCoordinator;
import ch.bildspur.visualpush.event.midi.MidiEventListener;

import javax.sound.midi.ShortMessage;

/**
 * Created by cansik on 20/08/16.
 */
public abstract class ControlChangeHandler extends BasicEventHandler{
    MidiEvent controlEvent;

    public ControlChangeHandler(int channel, int number)
    {
        super(channel, number);
        controlEvent = new MidiEvent(MidiCommand.ControlChange, channel, number);
    }

    @Override
    public void registerMidiEvent(MidiEventCoordinator coordinator) {
        coordinator.addMidiEventListener(this, controlEvent);
    }

    @Override
    public void removeMidiEvent(MidiEventCoordinator coordinator) {
        coordinator.removeMidiEventListener(this, controlEvent);
    }

    @Override
    public void midiEvent(MidiEvent event) {
        if(event.equals(controlEvent)) {
            ShortMessage msg = event.getShort();
            controlChange(msg.getChannel(), msg.getData1(), msg.getData2());
        }
    }

    public abstract void controlChange(int channel, int number, int value);
}
