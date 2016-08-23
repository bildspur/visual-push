package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.event.NoteChangeHandler;
import ch.bildspur.visualpush.event.midi.MidiEvent;
import ch.bildspur.visualpush.event.midi.MidiEventCoordinator;
import ch.bildspur.visualpush.event.midi.MidiEventListener;
import ch.bildspur.visualpush.ui.style.NumberDisplayStyle;

/**
 * Created by cansik on 23/08/16.
 */
public abstract class NumberDisplayControl extends UIControl implements MidiEventListener {
    private ControlChangeHandler ccHandler;
    private NoteChangeHandler nnHandler;

    DataModel<Integer> model;

    float minimumValue = 0;
    float maximumValue = 255;

    NumberDisplayStyle displayStyle = NumberDisplayStyle.FILL;

    boolean isActive = false;
    boolean showLabel = true;

    public NumberDisplayControl(DataModel<Integer> model, int ccChannel, int ccNumber, int nnChannel, int nnNumber)
    {
        super();

        this.model = model;
        width = 80;
        height = 80;

        // set up ccHandler
        ccHandler = new ControlChangeHandler(ccChannel, ccNumber) {
            @Override
            public void controlChange(int channel, int number, int value) {
                NumberDisplayControl.this.controlChange(channel, number, value);
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

    public void controlChange(int channel, int number, int value) {
        if(value == 127)
            model.setValue(model.getValue() - 1);
        else if(value == 1)
            model.setValue(model.getValue() + 1);

        if(model.getValue() > 255)
            model.setValue(255);

        if(model.getValue() < 0)
            model.setValue(0);
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

    public float getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(float minimumValue) {
        this.minimumValue = minimumValue;
    }

    public float getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(float maximumValue) {
        this.maximumValue = maximumValue;
    }

    public NumberDisplayStyle getDisplayStyle() {
        return displayStyle;
    }

    public void setDisplayStyle(NumberDisplayStyle displayStyle) {
        this.displayStyle = displayStyle;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isShowLabel() {
        return showLabel;
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
    }

    public DataModel<Integer> getModel() {
        return model;
    }

    public void setModel(DataModel<Integer> model) {
        this.model = model;
    }
}
