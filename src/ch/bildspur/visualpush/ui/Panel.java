package ch.bildspur.visualpush.ui;

import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Created by cansik on 18/08/16.
 */
public class Panel extends Control {

    public Panel()
    {
        super();
    }

    public Panel(float x, float y, float width, float height)
    {
        this();
        this.position = new PVector(x, y);
        this.width = width;
        this.height = height;
    }

    public void paint(PGraphics g)
    {
        PVector pos = getAbsolutePosition();

        g.fill(fillColor.getRGB());
        g.stroke(borderColor.getRGB());
        g.rect(pos.x, pos.y, width, height);

        super.paint(g);
    }
}
