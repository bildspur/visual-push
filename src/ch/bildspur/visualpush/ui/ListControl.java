package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.data.DataModel;
import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.List;

/**
 * Created by cansik on 30/08/16.
 */
public class ListControl extends UIControl {

    DataModel<Integer> model;
    List<ListElement> items;

    float textSize = 14f;
    float padding = 1.5f;
    int textHorizontalAlignment = PApplet.LEFT;
    int textVerticalAlignment = PApplet.TOP;

    String arrowSignDown = "\u25BC";
    String arrowSignLeft = "\u25C4";

    boolean isActive = false;
    boolean alwaysActive = false;

    public ListControl(DataModel<Integer> model,
                       List<ListElement> items)
    {
        super();

        this.model = model;
        this.items = items;

        width = 100;
        height = 80;
    }

    public void selectItemByValue(Object value)
    {
        for(int i = 0; i < items.size(); i++) {
            if (value.equals(items.get(i).getValue())) {
                model.setValue(i);
                return;
            }
        }
    }

    public void paint(PGraphics g)
    {
        setTranslation(g);
        setDirection(g);

        // item
        if (isActive || alwaysActive)
            activePaint(g);
        else
            inactivePaint(g, true);

        resetDirection(g);
        resetTranslation(g);

        super.paint(g);
    }

    private void inactivePaint(PGraphics g, boolean showArrow)
    {
        // get current item
        ListElement selected = items.get(model.getValue());

        // draw rect
        g.stroke(this.strokeColor.getRGB());
        g.noFill();
        g.rect(0 - padding, 0 - padding, width + (4 * padding), textSize + (4 * padding));

        // draw value
        g.fill(this.fillColor.getRGB());
        g.textSize(textSize);
        g.textAlign(textHorizontalAlignment, textVerticalAlignment);
        g.text(selected.name, 0, 0);

        if (showArrow)
            g.text(arrowSignDown, width - g.textWidth(arrowSignDown), 0);
    }

    private void activePaint(PGraphics g)
    {
        float itemHeight = 5 + textSize;

        // calculate items to show
        int itemsToShow = (int)((height / itemHeight));
        int start = model.getValue();
        int end = PApplet.min(start + itemsToShow, items.size());

        // draw options
        int counter = 0;
        for (int i = start; i < end; i++)
        {
            ListElement item = items.get(i);

            if (model.getValue() == i)
                g.fill(this.fillColor.getRGB());
            else
                g.fill(this.strokeColor.getRGB());

            g.textSize(textSize);
            g.textAlign(textHorizontalAlignment, textVerticalAlignment);
            g.text(item.name, 0, itemHeight * (counter));

            if (model.getValue() == i)
            {
                g.text(arrowSignLeft, width - g.textWidth(arrowSignLeft), itemHeight * (counter));
            }

            counter++;
        }

        // draw scrollbar
        g.fill(this.fillColor.getRGB());
        g.noStroke();
        float scrollBarPosition = height / items.size();
        g.rect(width + padding, model.getValue() * scrollBarPosition, 2, scrollBarPosition);

        // draw border
        g.stroke(this.strokeColor.getRGB());
        g.noFill();
        g.rect(0 - padding, 0 - padding, width + (4 * padding), height + (4 * padding));
    }

    public DataModel<Integer> getModel() {
        return model;
    }

    public void setModel(DataModel<Integer> model) {
        this.model = model;
    }

    public List<ListElement> getItems() {
        return items;
    }

    public void setItems(List<ListElement> items) {
        this.items = items;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getPadding() {
        return padding;
    }

    public void setPadding(float padding) {
        this.padding = padding;
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

    public boolean isActive() {
        return isActive;
    }

    public boolean isAlwaysActive() {
        return alwaysActive;
    }

    public void setAlwaysActive(boolean alwaysActive) {
        this.alwaysActive = alwaysActive;
    }
}