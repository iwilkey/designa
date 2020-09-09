package com.iwilkey.designa.gui.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.input.InputHandler;

import java.util.ArrayList;

public class UIManager {

    private ArrayList<UIObject> objects;

    public UIManager() { objects = new ArrayList<>(); }

    public void tick() { for(UIObject o : objects) o.tick(); }
    public void render(Batch b) { for(UIObject o : objects) o.render(b); }
    public void onMouseMove() { for(UIObject o : objects) o.onMouseMove(); }
    public void onMouseRelease() { for(UIObject o : objects) o.onMouseRelease(); }
    public void addObject(UIObject o) { objects.add(o); }
    public void removeObject(UIObject o) { objects.remove(o); }

    public ArrayList<UIObject> getObjects() { return objects; }
    public void setObjects(ArrayList<UIObject> objects) { this.objects = objects; }

}
