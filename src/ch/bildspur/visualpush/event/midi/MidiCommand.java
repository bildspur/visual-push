package ch.bildspur.visualpush.event.midi;

/**
 * Created by cansik on 19/08/16.
 */
public enum MidiCommand {
    NoteOn(0x90),
    NoteOff(0x80),
    ControlChange(0xB0),
    PitchBend(0xE0),
    ChannelPressure(0xD0),
    PolyKeyPressure(0xA0);

    private int value;

    MidiCommand(int value){
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static MidiCommand fromInteger(int x) {
        switch(x) {
            case 0x90:
                return NoteOn;
            case 0x80:
                return NoteOff;
            case 0xB0:
                return ControlChange;
            case 0xE0:
                return PitchBend;
            case 0xD0:
                return ChannelPressure;
            case 0xA0:
                return PolyKeyPressure;
        }
        return null;
    }
}