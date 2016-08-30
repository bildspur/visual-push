package ch.bildspur.visualpush.ui;

/**
 * Created by cansik on 30/08/16.
 */
public class ListElement
{
    int value;
    String name;

    public ListElement(int value, String name)
    {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}