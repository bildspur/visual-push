package ch.bildspur.visualpush.event.midi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.sound.midi.MidiMessage;

/**
 * Created by cansik on 19/08/16.
 */
public class MidiEvent {
    MidiMessage message;

    public MidiEvent(MidiMessage message)
    {
        this.message = message;
    }

    public MidiMessage getMessage() {
        return message;
    }

    public MidiCommand getCommand()
    {
        return MidiCommand.fromInteger(message.getMessage()[0]);
    }

    @Override
    public int hashCode() {
        // todo: change it to only hash relevant parts (type and channel)
        HashCodeBuilder h = new HashCodeBuilder(17, 31);
        for(byte b : message.getMessage())
            h.append(b);
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
        // todo: change it to only compare relevant parts (type and channel)
        for(int i = 0; i < message.getLength(); i++)
            e.append(message.getMessage()[i], rhs.getMessage().getMessage()[i]);
        return e.isEquals();
    }
}
