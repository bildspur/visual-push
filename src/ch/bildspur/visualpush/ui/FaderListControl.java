package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.NoteChangeHandler;
import ch.bildspur.visualpush.event.midi.MidiEvent;
import ch.bildspur.visualpush.event.midi.MidiEventCoordinator;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import processing.core.PApplet;

import java.util.List;

/**
 * Created by cansik on 30/08/16.
 */
public class FaderListControl extends ListControl implements MidiEventListener {
    private ControlChangeHandler ccHandler;
    private NoteChangeHandler nnHandler;

    private float slowFactor = 0.1f;
    private float slowValue = 0f;

    public FaderListControl(DataModel<Integer> model,
                            List<ListElement> items,
                            int ccChannel,
                            int ccNumber,
                            int nnChannel,
                            int nnNumber) {
        super(model, items);

        // set up ccHandler
        ccHandler = new ControlChangeHandler(ccChannel, ccNumber) {
            @Override
            public void controlChange(int channel, int number, int value) {
                FaderListControl.this.controlChange(channel, number, value);
            }
        };

        nnHandler = new NoteChangeHandler(nnChannel, nnNumber) {
            @Override
            public void noteOn(int channel, int number, int value) {
                isActive = true;
            }

            @Override
            public void noteOff(int channel, int number, int value) {
                isActive = false;
            }
        };
    }

    private void controlChange(int channel, int number, int value) {
        slowValue = slowValue > 1.0f ? 0.0f : slowValue + slowFactor;

        if(value == 127) {
            model.setValue(PApplet.max(0, model.getValue() - (int)slowValue));
        }
        else if(value == 1) {
            model.setValue(PApplet.min(model.getValue() + (int)slowValue, items.size() - 1));
        }
    }

    @Override
    public void registerMidiEvent(MidiEventCoordinator coordinator) {
        ccHandler.registerMidiEvent(coordinator);
        nnHandler.registerMidiEvent(coordinator);
    }

    @Override
    public void removeMidiEvent(MidiEventCoordinator coordinator) {
        ccHandler.removeMidiEvent(coordinator);
        nnHandler.removeMidiEvent(coordinator);
    }

    @Override
    public void midiEvent(MidiEvent event) {
        ccHandler.midiEvent(event);
        nnHandler.midiEvent(event);
    }
}
