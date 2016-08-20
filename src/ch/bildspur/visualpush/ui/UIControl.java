package ch.bildspur.visualpush.ui;

import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cansik on 18/08/16.
 */
public abstract class UIControl {
    PVector position;
    float width;
    float height;
    Color fillColor = Color.GRAY;
    Color strokeColor = Color.WHITE;
    List<UIControl> controls;
    UIControl parent;

    public UIControl()
    {
        controls = new ArrayList<>();
        position = new PVector();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public List<UIControl> getControls() {
        return controls;
    }

    public UIControl getParent() {
        return parent;
    }

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public void addControl(UIControl c)
    {
        c.parent = this;
        controls.add(c);
    }

    public void removeControl(UIControl c)
    {
        c.parent = null;
        controls.remove(c);
    }

    public PVector getAbsolutePosition()
    {
        if(parent != null)
            return PVector.add(position, parent.getAbsolutePosition());
        return position;
    }

    public void paint(PGraphics g)
    {
        for(UIControl c : controls)
            c.paint(g);
    }
}
