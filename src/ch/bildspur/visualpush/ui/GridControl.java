package ch.bildspur.visualpush.ui;

import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Created by cansik on 28/08/16.
 */
public class GridControl extends UIControl {
    float resolutionX;
    float resolutionY;

    public GridControl(float resolutionX, float resolutionY)
    {
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
    }

    public void paint(PGraphics g)
    {
        g.noFill();
        g.stroke(strokeColor.getRGB());

        float x = 0;
        while(x < g.width) {
            g.text((int)x, x, 0);
            g.line(x, 0, x, g.height);
            x += resolutionX;
        }

        float y = 0;
        while(y < g.height) {
            g.text((int)y, 0, y);
            g.line(0, y, g.width, y);
            y += resolutionY;
        }

        super.paint(g);
    }
}
