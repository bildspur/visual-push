package ch.bildspur.visualpush.video;

/**
 * Created by cansik on 28/08/16.
 */
public enum BlendMode {
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

    public static BlendMode fromInteger(int i)
    {
        switch (i) {
            case 1:
                return BLEND;
            case 2:
                return ADD;
            case 4:
                return SUBTRACT;
            case 8:
                return LIGHTEST;
            case 16:
                return DARKEST;
            case 32:
                return DIFFERENCE;
            case 64:
                return EXCLUSION;
            case 128:
                return MULTIPLY;
            case 256:
                return SCREEN;
            case 512:
                return OVERLAY;
            case 1024:
                return HARD_LIGHT;
            case 2048:
                return SOFT_LIGHT;
            case 4096:
                return DODGE;
            case 8192:
                return BURN;
            default:
                return BLEND;
        }
    }

    public int getValue()
    {
        return value;
    }
}
