package ch.bildspur.visualpush.push.color;

/**
 * Created by cansik on 27/08/16.
 */
public class PushColor {
    int pulsing;
    int color;

    public PushColor()
    {
        this(0);
    }

    public PushColor(int color)
    {
        this(color, 0);
    }

    public PushColor(int color, int pulsing)
    {
        this.pulsing = pulsing;
        this.color = color;
    }

    public int getPulsing() {
        return pulsing;
    }

    public void setPulsing(int pulsing) {
        this.pulsing = pulsing;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
