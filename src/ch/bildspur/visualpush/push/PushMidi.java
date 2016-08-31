package ch.bildspur.visualpush.push;

import themidibus.MidiBus;

/**
 * Created by cansik on 31/08/16.
 */
public class PushMidi {
    public static void setupTouchStrip(MidiBus bus)
    {
        bus.sendMessage(
                new byte[] {
                        (byte)0xF0, (byte)0x00, (byte)0x21, (byte)0x1D, (byte)0x01, (byte)0x01, (byte)0x17, (byte)0x0C, (byte)0xF7
                }
        );
    }
}
