package dev.iwilkey.designa.ui;

public abstract class Scrollable extends UIObject {

    public boolean isScrolling;
    public float dx;

    public Scrollable(int x, int y, int width, int height){
        super(UIObjectType.SCROLLABLE, x, y, width, height);
        isScrolling = false;
    }

}
