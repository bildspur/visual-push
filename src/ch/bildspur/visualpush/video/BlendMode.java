package ch.bildspur.visualpush.video;

/**
 * Created by cansik on 28/08/16.
 */
public enum BlendMode {
    NONE(0),
    BLEND(1),
    ADD(2),
    SUBTRACT(4),
    LIGHTEST(8),
    DARKEST(16),
    DIFFERENCE(32),
    EXCLUSION(64),
    MULTIPLY(128),
    SCREEN(256),
    OVERLAY(512),
    HARD_LIGHT(1024),
    SOFT_LIGHT(2048),
    DODGE(4096),
    BURN(8192);

    private int value;

    BlendMode(int value){
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
