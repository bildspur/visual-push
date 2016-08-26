package ch.bildspur.visualpush.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cansik on 23/08/16.
 */
public class DataModel<T>
{
    List<DataModelChangeListener<T>> listener;
    T value;

    public DataModel(T initialValue)
    {
        value = initialValue;
        listener = new ArrayList<>();
    }

    public T getValue()
    {
        return this.value;
    }

    public void setValue(T value)
    {
        this.value = value;

        for (int i = listener.size() - 1; i >= 0; i--)
            listener.get(i).valueChanged(value);
    }

    public void addListener(DataModelChangeListener<T> l)
    {
        listener.add(l);
    }

    public void removeListener(DataModelChangeListener<T> l)
    {
        listener.remove(l);
    }
}