package ch.bildspur.visualpush.event.midi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

/**
 * Created by cansik on 19/08/16.
 */
public class MidiEvent {
    MidiMessage message;

    public MidiEvent(MidiMessage message)
    {
        this.message = message;
    }

    /**
     * Add new midi event to listen to by setting command type and channel id.
     * @param command Midi command to listen to.
     * @param channel Midi channel id to listen to.
     * @param number Midi notenumber.
     */
    public MidiEvent(MidiCommand command, int channel, int number)
    {
        this(command, channel, number, 0);
    }

    public MidiEvent(MidiCommand command, int channel, int number, int velocity)
    {
        ShortMessage msg = new ShortMessage();
        try {
            msg.setMessage(command.getValue(), channel, number, velocity);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        message = msg;
    }

    public MidiMessage getMessage() {
        return message;
    }

    public MidiCommand getCommand()
    {
        return MidiCommand.fromInteger(message.getMessage()[0]);
    }

    public ShortMessage getShort()
    {
        return (ShortMessage)message;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder h = new HashCodeBuilder(17, 31);

        // use only channel and command and notenumber for ShortMessage
        if(message instanceof ShortMessage)
        {
            ShortMessage m = (ShortMessage)message;
            h.append(m.getCommand());
            h.append(m.getChannel());
            h.append(m.getData1());
        }
        else {
            for (byte b : message.getMessage())
                h.append(b);
        }

        return h.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MidiEvent))
            return false;
        if (obj == this)
            return true;

        MidiEvent rhs = (MidiEvent) obj;

        // check message length
        if(message.getLength() != rhs.getMessage().getLength())
            return false;

        // check message fields
        EqualsBuilder e = new EqualsBuilder();

        // use only channel and command and notenumber for ShortMessage
        if(message instanceof ShortMessage && rhs.getMessage() instanceof ShortMessage)
        {
            ShortMessage m = (ShortMessage)message;
            ShortMessage rhsm = (ShortMessage) rhs.getMessage();
            e.append(m.getCommand(), rhsm.getCommand());
            e.append(m.getChannel(), rhsm.getChannel());
            e.append(m.getData1(), rhsm.getData1());
        }
        else
        {
            for(int i = 0; i < message.getLength(); i++)
                e.append(message.getMessage()[i], rhs.getMessage().getMessage()[i]);
        }

        return e.isEquals();
    }
}
