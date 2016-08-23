package ch.bildspur.visualpush.ui.animation;

/**
 * Created by cansik on 23/08/16.
 */
public class Animation
{
    float value;
    float time;
    float start;
    float end;
    float frameTime;

    int animationTimeCount;

    float incValue;

    boolean running;

    AnimationCallback callback;

    public Animation(float time, float start, float end)
    {
        this.time = time;
        this.start = start;
        this.end = end;
        this.value = start;
        this.frameTime = secondsToFrames(time);
    }

    public Animation(float time, float start, float end, AnimationCallback callback)
    {
        this(time, start, end);
        this.callback = callback;
    }

    public float getValue()
    {
        return value;
    }

    public boolean isRunning()
    {
        return running;
    }

    public void update()
    {
        if (!running)
            return;

        value += incValue;
        animationTimeCount++;

        if (animationTimeCount >= frameTime)
            stop();
    }

    public void start()
    {
        incValue = (1f / frameTime) * (end-start);
        running = true;
        value = start;
        animationTimeCount = 0;
    }

    public void stop()
    {
        running = false;
        animationTimeCount = 0;

        if (callback != null)
            callback.animationFinished(this);
    }

    int secondsToFrames(float seconds)
    {
        return (int)(seconds * 60);
    }
}