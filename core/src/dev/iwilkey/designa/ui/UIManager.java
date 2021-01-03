package dev.iwilkey.designa.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.item.creator.CategoryItemRecipeList;

import java.util.ArrayList;

public class UIManager {

    public String name;
    public ArrayList<UIText> texts;
    public ArrayList<UIButton> buttons;
    public ArrayList<ScrollableItemList> itemLists;
    public ArrayList<UIImageButton> imageButtons;
    public ArrayList<CategoryItemRecipeList> categoryItemList;

    public UIManager(String name) {
        this.name = name;
    }

    public UIText addText(UIText text) {
        if(texts == null) texts = new ArrayList<>();
        texts.add(text);
        return text;
    }

    public UIButton addButton(UIButton button) {
        if(buttons == null) buttons = new ArrayList<>();
        buttons.add(button);
        return button;
    }

    public ScrollableItemList addScrollableItemList(ScrollableItemList list) {
        if(itemLists == null) itemLists = new ArrayList<>();
        itemLists.add(list);
        return list;
    }

    public UIImageButton addImageButton(UIImageButton button) {
        if(imageButtons == null) imageButtons = new ArrayList<>();
        imageButtons.add(button);
        return button;
    }

    public CategoryItemRecipeList addCategoryItemRecipeList(CategoryItemRecipeList l) {
        if(categoryItemList == null) categoryItemList = new ArrayList<>();
        categoryItemList.add(l);
        return l;
    }

    public void relocate(float dx, float dy) {
        if(texts != null) for(UIText text : texts) text.move(dx, dy);
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.move(dx, dy);
        if(categoryItemList != null) for(CategoryItemRecipeList l : categoryItemList) l.move(dx, dy);
    }

    public void tick() {
        if(InputHandler.mouseCurrentlyMoving) onMouseMove();
        if(InputHandler.leftMouseButtonUp) onMouseRelease();
        if(buttons != null) for(UIButton button : buttons) button.tick();
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.tick();
        if(imageButtons != null) for(UIImageButton button : imageButtons) button.tick();
        if(categoryItemList != null) for(CategoryItemRecipeList l : categoryItemList) l.tick();
    }

    public void onMouseMove() {
        if(texts != null) for(UIText text : texts) text.onMouseMove();
        if(buttons != null) for(UIButton button : buttons) button.onMouseMove();
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.onMouseMove();
        if(imageButtons != null) for(UIImageButton button : imageButtons) button.onMouseMove();
        if(categoryItemList != null) for(CategoryItemRecipeList l : categoryItemList) l.onMouseMove();
    }

    public void onMouseRelease() {
        if(texts != null) for(UIText text : texts) text.onMouseRelease();
        if(buttons != null) for(UIButton button : buttons) button.onMouseRelease();
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.onMouseRelease();
        if(imageButtons != null) for(UIImageButton button : imageButtons) button.onMouseRelease();
        if(categoryItemList != null) for(CategoryItemRecipeList l : categoryItemList) l.onMouseRelease();
    }

    public void onResize(int width, int height) {
        if(texts != null) for(UIText text : texts) text.onResize(width, height);
        if(buttons != null) for(UIButton button : buttons) {
            button.move(10,10);
            button.onResize(width, height);
        }
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.onResize(width, height);
        if(imageButtons != null) for(UIImageButton button : imageButtons) button.onResize(width, height);
        if(categoryItemList != null) for(CategoryItemRecipeList l : categoryItemList) l.onResize(width, height);
    }

    public void render(Batch b) {
        if(buttons != null) for(UIButton button : buttons) button.render(b);
        if(itemLists != null) for(ScrollableItemList list : itemLists) list.render(b);
        if(imageButtons != null) for(UIImageButton button : imageButtons) button.render(b);
        if(categoryItemList != null) for(CategoryItemRecipeList l : categoryItemList) l.render(b);
        if(texts != null) for(UIText text : texts) text.render(b);
    }

}
