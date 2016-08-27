package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.video.Clip;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Created by cansik on 20/08/16.
 */
public class ClipViewerControl extends UIControl {
    Clip clip;

    public ClipViewerControl(Clip clip)
    {
        super();
        this.clip = clip;
    }

    public ClipViewerControl(float x, float y, float width, float height)
    {
        this(null, x, y, width, height);
    }

    public ClipViewerControl(Clip clip, float x, float y, float width, float height)
    {
        this(clip);
        this.position = new PVector(x, y);
        this.width = width;
        this.height = height;
    }

    public Clip getClip() {
        return clip;
    }

    public void setClip(Clip clip) {
        this.clip = clip;
    }

    public void paint(PGraphics g)
    {
        PVector pos = getAbsolutePosition();

        //g.fill(fillColor.getRGB());
        g.noFill();
        g.stroke(strokeColor.getRGB());
        g.rect(pos.x, pos.y, width, height);

        if(clip != null)
            g.image(clip.getPreview(), pos.x, pos.y, width, height);

        super.paint(g);
    }
}
