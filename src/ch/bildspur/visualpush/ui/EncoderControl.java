package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.data.DataModel;
import ch.bildspur.visualpush.ui.style.NumberDisplayStyle;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Created by cansik on 20/08/16.
 */
public class EncoderControl extends NumberDisplayControl {
    float start = -240;
    float end = 60;

    public EncoderControl(DataModel<Integer> model, int ccChannel, int ccNumber, int nnChannel, int nnNumber)
    {
        super(model, ccChannel, ccNumber, nnChannel, nnNumber);
    }

    public void paint(PGraphics g)
    {
        setTranslation(g);
        setDirection(g);

        float diaX = width;
        float diaY = height;

        float radX = diaX / 2f;
        float radY = diaY / 2f;

        float partSize = 5;

        float angle = PApplet.map(model.getValue(), minimumValue, maximumValue, 0, 300);

        int c = isActive ? this.strokeColor.getRGB() : this.strokeColor.darker().getRGB();

        if (isActive)
            g.fill(this.fillColor.getRGB(), opacity);
        else
            g.fill(this.fillColor.darker().getRGB(), opacity);

        // draw value
        g.noStroke();
        g.strokeWeight(1.5f);

        if (displayStyle == NumberDisplayStyle.FILL)
            g.arc(radX, radY, diaX, diaY, PApplet.radians(start), PApplet.radians(start + angle), PApplet.PIE);
        else
            g.arc(radX, radY, diaX, diaY, PApplet.radians(start + angle - partSize), PApplet.radians(start + angle + partSize), PApplet.PIE);

        // draw border
        g.stroke(c, opacity);
        g.noFill();
        g.arc(radX, radY, diaX, diaY, PApplet.radians(start), PApplet.radians(end), PApplet.PIE);
        g.fill(0, opacity);
        g.arc(radX, radY, radX, radY, PApplet.radians(start), PApplet.radians(end), PApplet.PIE);
        g.noStroke();
        g.ellipse(radX, radY, radX-1, radY-1);

        if (showLabel)
        {
            g.fill(c, opacity);
            g.textSize(12);
            g.textAlign(PApplet.CENTER, PApplet.CENTER);
            g.text(model.getValue(), radX, radY);
        }

        resetDirection(g);
        resetTranslation(g);

        super.paint(g);
    }
}
