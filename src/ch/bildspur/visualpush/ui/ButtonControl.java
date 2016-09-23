package ch.bildspur.visualpush.ui;

import ch.bildspur.visualpush.event.ButtonClickedHandler;
import ch.bildspur.visualpush.event.ControlChangeHandler;
import ch.bildspur.visualpush.push.Wayang;
import ch.bildspur.visualpush.push.color.RGBColor;
import ch.bildspur.visualpush.sketch.controller.MidiController;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;

/**
 * Created by cansik on 23.09.16.
 */
public class ButtonControl extends UIControl {

    ControlChangeHandler handler;
    ButtonClickedHandler listener;

    static final int LOWER_BUTTON_START = 20;
    static final int UPPER_BUTTON_START = 102;

    static final float buttonHeight = 20f;
    float buttonSpace = 15f;

    String text;
    boolean active = false;

    public ButtonControl(String text, int buttonPosition, boolean isTopButton, MidiController midi, ButtonClickedHandler listener)
    {
        super();

        this.fillColor = Color.white;

        int midiIndex = isTopButton ? UPPER_BUTTON_START + buttonPosition : LOWER_BUTTON_START + buttonPosition;
        midi.sendControllerChange(0, midiIndex, RGBColor.WHITE().getColor());

        this.handler = new ControlChangeHandler(0, midiIndex) {
            @Override
            public void controlChange(int channel, int number, int value) {
                if(value == 127) {
                    active = true;
                    ButtonControl.this.listener.clicked(ButtonControl.this);
                }
                else
                {
                    active = false;
                }
            }
        };

        float buttonWidth = (Wayang.DISPLAY_WIDTH - (8f * (2f * buttonSpace))) / 8f;
        float buttonX = buttonSpace + (buttonWidth + buttonSpace)  * buttonPosition;
        float buttonY = isTopButton ? 0 : Wayang.DISPLAY_HEIGHT - (1f + buttonHeight);

        this.position = new PVector(buttonX, buttonY);
        this.width = buttonWidth;
        this.height = buttonHeight;
        this.text = text;

        this.listener = listener;
    }

    public void paint(PGraphics g) {
        setTranslation(g);
        setDirection(g);

        // draw rect
        g.stroke(strokeColor.getRGB());

        if (active)
            g.fill(fillColor.getRGB());
        else
            g.noFill();

        g.rect(0, 0, width, height);

        // draw text
        g.noStroke();
        g.fill(fillColor.getRGB());

        g.textAlign(PApplet.CENTER, PApplet.CENTER);
        g.text(text, width / 2f, height / 2f);

        resetDirection(g);
        resetTranslation(g);

        super.paint(g);
    }

    public ControlChangeHandler getHandler() {
        return handler;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
