package ch.bildspur.visualpush.event;

import ch.bildspur.visualpush.sketch.controller.MidiController;

/**
 * Created by cansik on 20/08/16.
 */
public class ButtonHandler extends ControlChangeHandler {

    public ButtonHandler(int channel, int number) {
        super(channel, number);
    }

    @Override
    public void controlChange(int channel, int number, int value) {
        System.out.println("Control Changed: " + number + " V:" + value);
        MidiController.bus.sendControllerChange(1, this.number, 25);
    }
}
