package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.data.DataModel;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Created by cansik on 23/08/16.
 */
public class FaderControl extends NumberDisplayControl {

    public FaderControl(DataModel<Float> model, int ccChannel, int ccNumber, int nnChannel, int nnNumber)
    {
        super(model, ccChannel, ccNumber, nnChannel, nnNumber);
        height = width / 2;
    }

    public void paint(PGraphics g)
    {
        setTranslation(g);
        setDirection(g);

        float cornerRoundValue = 7;
        float fillWidth = PApplet.map(model.getValue(), minimumValue, maximumValue, 0, width);

        int c = isActive ? this.strokeColor.getRGB() : this.strokeColor.darker().getRGB();

        if (isActive)
            g.fill(this.fillColor.getRGB(), opacity);
        else
            g.fill(this.fillColor.darker().getRGB(), opacity);

        // draw value
        g.noStroke();
        g.rect(0, 0, fillWidth, height, cornerRoundValue);

        // draw border
        g.stroke(c, opacity);
        g.strokeWeight(1.5f);
        g.noFill();
        g.rect(0, 0, width, height, cornerRoundValue - 2);

        if (showLabel)
        {
            g.fill(c, opacity);
            g.textAlign(PApplet.CENTER, PApplet.CENTER);
            g.text(model.getValue(), width/2, height/2);
        }

        resetDirection(g);
        resetTranslation(g);

        super.paint(g);
    }
}