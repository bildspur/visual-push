package ch.bildspur.visualpush.data;

/**
 * Created by cansik on 23/08/16.
 */
public interface DataModelChangeListener<T>
{
    void valueChanged(T value);
}