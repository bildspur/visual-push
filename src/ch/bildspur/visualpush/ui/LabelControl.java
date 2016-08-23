package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.data.DataModel;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Created by cansik on 23/08/16.
 */
public class LabelControl extends UIControl {

    DataModel<String> model;
    float textSize = 12;
    int textHorizontalAlignment = PApplet.LEFT;
    int textVerticalAlignment = PApplet.TOP;

    public LabelControl(DataModel<String> model)
    {
        super();

        this.model = model;

        width = 80;
        height = 80;
    }

    public void paint(PGraphics g)
    {
        setTranslation(g);
        setDirection(g);

        // draw value
        g.fill(this.strokeColor.getRGB());
        g.textSize(textSize);
        g.textAlign(textHorizontalAlignment, textVerticalAlignment);
        g.text(model.getValue(), 0, 0);

        resetDirection(g);
        resetTranslation(g);

        super.paint(g);
    }

    public DataModel<String> getModel() {
        return model;
    }

    public void setModel(DataModel<String> model) {
        this.model = model;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextHorizontalAlignment() {
        return textHorizontalAlignment;
    }

    public void setTextHorizontalAlignment(int textHorizontalAlignment) {
        this.textHorizontalAlignment = textHorizontalAlignment;
    }

    public int getTextVerticalAlignment() {
        return textVerticalAlignment;
    }

    public void setTextVerticalAlignment(int textVerticalAlignment) {
        this.textVerticalAlignment = textVerticalAlignment;
    }
}
