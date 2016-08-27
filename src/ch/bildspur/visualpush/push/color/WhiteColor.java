package ch.bildspur.visualpush.push.color;

/**
 * Created by cansik on 27/08/16.
 */
public class WhiteColor extends PushColor {
    public static PushColor WHITE()
    {
        return new PushColor(127);
    }

    public static PushColor LIGHT_GRAY()
    {
        return new PushColor(48);
    }

    public static PushColor DARK_GRAY()
    {
        return new PushColor(16);
    }

    public static PushColor BLACK()
    {
        return new PushColor(0);
    }
}
