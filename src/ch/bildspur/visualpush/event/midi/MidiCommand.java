package ch.bildspur.visualpush.event.midi;

/**
 * Created by cansik on 19/08/16.
 */
public enum MidiCommand {
    NoteOn(144),
    NoteOff(127);

    private int value;

    MidiCommand(int value){
        this.value = value;
    }

    public static MidiCommand fromInteger(int x) {
        switch(x) {
            case 144:
                return NoteOn;
            case 127:
                return NoteOff;
        }
        return null;
    }
}