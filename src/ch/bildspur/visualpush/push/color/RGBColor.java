package ch.bildspur.visualpush.push.color;

/**
 * Created by cansik on 27/08/16.
 */
public class RGBColor extends PushColor
{
    public static PushColor RED()
    {
        return new PushColor(127);
    }

    public static PushColor GREEN()
    {
        return new PushColor(126);
    }

    public static PushColor BLUE()
    {
        return new PushColor(125);
    }

    public static PushColor WHITE()
    {
        return new PushColor(122);
    }

    public static PushColor BLACK()
    {
        return new PushColor(0);
    }

    public static PushColor PINK()
    {
        return new PushColor(25);
    }
}
