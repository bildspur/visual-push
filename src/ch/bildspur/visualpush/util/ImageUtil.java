package ch.bildspur.visualpush.util;

import ch.bildspur.visualpush.video.BlendMode;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Created by cansik on 17/08/16.
 */
public class ImageUtil {

    public static void centerImage(PGraphics g, PImage img)
    {
        centerImage(g, img, img.width, img.height);
    }

    public static void blendImage(PGraphics g, PImage img, BlendMode mode)
    {
        centerBlend(g, img, img.width, img.height, mode);
    }


    public static void centerImageAdjusted(PGraphics g, PImage img)
    {
        float scaleFactor;

        if(img.height > img.width)
            scaleFactor = (float)g.height / img.height;
        else
            scaleFactor = (float)g.width / img.width;

        centerImage(g, img, scaleFactor * img.width, scaleFactor * img.height);
    }

    public static void centerImage(PGraphics g, PImage img, float width, float height)
    {
        centerBlend(g, img, width, height, BlendMode.NONE);
    }

    public static void centerBlend(PGraphics g, PImage img, float width, float height, BlendMode mode)
    {
        if(mode == BlendMode.NONE)
            g.image(img, (g.width / 2.0f) - (width / 2.0f), (g.height / 2.0f) - (height / 2.0f), width, height);
        else
            g.blend(img, 0, 0,
                    img.width, img.height,
                    (int)((g.width / 2.0f) - (width / 2.0f)),
                    (int)((g.height / 2.0f) - (height / 2.0f)),
                    (int)width, (int)height,
                    mode.getValue());

    }
}
