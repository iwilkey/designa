package dev.iwilkey.designa.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.input.InputHandler;

import java.util.ArrayList;

public class UIManager {

    public String name;
    public ArrayList<UIText> texts;
    public ArrayList<UIButton> buttons;
    public ArrayList<ScrollableItemList> itemLists;

    public UIManager(String name) {
        this.name = name;
    }

    public void addText(UIText text) {
        if(texts == null) texts = new ArrayList<>();
        texts.add(text);
    }

    public void addButton(UIButton button) {
        if(buttons == null) buttons = new ArrayList<>();
        buttons.add(button);
    }

    public void addScrollableItemList(ScrollableItemList list) {
        if(itemLists == null) itemLists = new ArrayList<>();
        itemLists.add(list);
    }

    public void tick() {
        if(InputHandler.mouseCurrentlyMoving) onMouseMove();
        if(InputHandler.leftMouseButtonUp) onMouseRelease();
        if(buttons != null) for(UIButton button : buttons) button.tick();
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.tick();
    }

    public void onMouseMove() {
        if(texts != null) for(UIText text : texts) text.onMouseMove();
        if(buttons != null) for(UIButton button : buttons) button.onMouseMove();
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.onMouseMove();
    }

    public void onMouseRelease() {
        if(texts != null) for(UIText text : texts) text.onMouseRelease();
        if(buttons != null) for(UIButton button : buttons) button.onMouseRelease();
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.onMouseRelease();
    }

    public void onResize(int width, int height) {
        if(texts != null) for(UIText text : texts) text.onResize(width, height);
        if(buttons != null) for(UIButton button : buttons) button.onResize(width, height);
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.onResize(width, height);
    }

    public void render(Batch b) {
        if(texts != null) for(UIText text : texts) text.render(b);
        if(buttons != null) for(UIButton button : buttons) button.render(b);
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.render(b);
    }

}
