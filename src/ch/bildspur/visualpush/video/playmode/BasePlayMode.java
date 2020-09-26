package ch.bildspur.visualpush.video.playmode;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class BasePlayMode implements PlayMode  {
    public static AtomicInteger clipCounter = new AtomicInteger(0);
}
