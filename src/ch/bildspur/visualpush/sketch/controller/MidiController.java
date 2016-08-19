package ch.bildspur.visualpush.sketch.controller;

import ch.bildspur.visualpush.Constants;
import ch.bildspur.visualpush.event.MidiEventListener;
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
public class MidiController extends ProcessingController {
    MidiBus bus;

    Map<MidiEvent, List<MidiEventListener>> midiListeners;

    public void setup(PApplet sketch){
        super.setup(sketch);

        midiListeners = new HashMap<>();
        bus = new MidiBus(sketch, Constants.ABLETON_PUSH_MIDI_PORT, Constants.ABLETON_PUSH_MIDI_PORT);
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

        // debug print
        PApplet.print("Status Byte/MIDI Command:"+message.getStatus());
        for (int i = 1;i < message.getMessage().length;i++) {
            PApplet.print("Param "+(i+1)+": "+(int)(message.getMessage()[i] & 0xFF));
        }
    }
}
