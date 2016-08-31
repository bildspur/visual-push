package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.Constants;
import ch.bildspur.visualpush.event.midi.MidiCommand;
import ch.bildspur.visualpush.event.midi.MidiEventCoordinator;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.event.midi.MidiEvent;
import processing.core.PApplet;
import themidibus.MidiBus;

import javax.sound.midi.MidiMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cansik on 16/08/16.
 */
public class MidiController extends ProcessingController implements MidiEventCoordinator {
    MidiBus bus;

    Map<MidiEvent, List<MidiEventListener>> midiListeners;

    public void setup(PApplet sketch){
        super.setup(sketch);

        midiListeners = new HashMap<>();
        bus = new MidiBus(sketch, Constants.ABLETON_PUSH_MIDI_PORT, Constants.ABLETON_PUSH_MIDI_PORT);
        bus.sendTimestamps(false);
    }

    public void addMidiEventListener(MidiEventListener listener, MidiEvent event)
    {
        if(midiListeners.containsKey(event))
            midiListeners.get(event).add(listener);
        else
            midiListeners.put(event, new ArrayList<MidiEventListener>() {{ add(listener); }});
    }

    public void removeMidiEventListener(MidiEventListener listener, MidiEvent event)
    {
        if(midiListeners.containsKey(event))
            midiListeners.get(event).remove(listener);
    }

    public void midiMessage(MidiMessage message) {
        MidiEvent event = new MidiEvent(message);

        // don't do anything if message is not registered
        if(!midiListeners.containsKey(event))
            return;

        // notify observers
        for(MidiEventListener listener : midiListeners.get(event))
                listener.midiEvent(event);
    }

    public void emulateNoteOn(int channel, int number, int velocity)
    {
        midiMessage(new MidiEvent(MidiCommand.NoteOn, channel, number, velocity).getMessage());
    }

    public void emulateNoteOff(int channel, int number, int velocity)
    {
        midiMessage(new MidiEvent(MidiCommand.NoteOff, channel, number, velocity).getMessage());
    }

    public void emulateControllerChange(int channel, int number, int velocity)
    {
        midiMessage(new MidiEvent(MidiCommand.ControlChange, channel, number, velocity).getMessage());
    }

    public void sendNoteOn(int channel, int number, int velocity) {
        bus.sendNoteOn(channel, number, velocity);
    }

    public void sendNoteOff(int channel, int number, int velocity) {
        bus.sendNoteOff(channel, number, velocity);
    }

    public void sendControllerChange(int channel, int number, int velocity) {
        bus.sendControllerChange(channel, number, velocity);
    }

    public void clearLEDs()
    {
        // clear cc
        for(int i = 3; i <= 119; i++)
            bus.sendControllerChange(0, i, 0);

        // clear nn
        for(int i = 36; i <= 99; i++)
            bus.sendNoteOff(0, i, 0);
    }

    public void sendMessage(byte[] bytes) {
        bus.sendMessage(bytes);
    }

    public MidiBus getBus() {
        return bus;
    }
}
