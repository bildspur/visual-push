package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.video.Clip;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

/**
 * Created by cansik on 20/08/16.
 */
public class ClipViewerControl extends UIControl {
    Clip clip;
    int padding = 1;
    float borderWeightHighlighted = 1.5f;
    float borderWeightNormal = 1f;

    float positionWidth = 2f;
    float positionAlpha = 200;

    boolean highlighted = false;

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
        this.strokeColor = Color.GRAY;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
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

        g.noFill();
        g.strokeWeight(highlighted ? borderWeightHighlighted : borderWeightNormal);
        g.stroke(strokeColor.getRGB());

        if(highlighted)
            g.stroke(fillColor.getRGB());

        g.rect(pos.x, pos.y, width, height);

        if(clip != null) {
            g.tint(clip.getRedTint().getValue(),
                    clip.getGreenTint().getValue(),
                    clip.getBlueTint().getValue(),
                    clip.getOpacity().getValue());

            if(clip.getPreview() != null)
                g.image(clip.getPreview(), pos.x + padding, pos.y + padding, width - padding, height - padding);

            // draw current position
            g.fill(fillColor.getRGB(), positionAlpha);
            g.noStroke();

            if(clip.duration() > 0)
                g.rect(pos.x, pos.y + height - positionWidth, PApplet.map(clip.time(), 0f, clip.duration(), 0f, width), positionWidth);
        }

        super.paint(g);
    }
}
