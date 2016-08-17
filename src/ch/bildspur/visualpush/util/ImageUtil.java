package ch.bildspur.visualpush.util;

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
        g.image(img, (g.width / 2.0f) - (width / 2.0f), (g.height / 2.0f) - (height / 2.0f), width, height);
    }
}
