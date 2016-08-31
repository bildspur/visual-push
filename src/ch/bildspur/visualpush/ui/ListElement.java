package ch.bildspur.visualpush.ui;

import java.util.Objects;

/**
 * Created by cansik on 30/08/16.
 */
public class ListElement
{
    Object value;
    String name;

    public ListElement(Object value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}