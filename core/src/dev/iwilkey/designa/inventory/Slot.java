package dev.iwilkey.designa.inventory;

import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.Tool;
import dev.iwilkey.designa.ui.UIText;

import java.awt.Rectangle;

public class Slot {

    public Item item;
    public Rectangle collider;
    public byte count;
    public boolean isCountable;
    public UIText display;
    
    public Tool tool;

    public Slot(Item item, Rectangle rectangle, boolean isCountable) {
        this.item = item;
        this.collider = rectangle;
        this.isCountable = isCountable;
        if(item != null)
        	if(!item.getType().stackable) this.isCountable = false;
        if(this.isCountable) count = 0;
        else count = 1;
        this.tool = null;
        this.display = null;
    }

    public Slot(Item item, Rectangle rectangle, boolean isCountable, UIText display) {
        this.item = item;
        this.collider = rectangle;
        this.isCountable = isCountable;
        if(item != null)
        	if(!item.getType().stackable) this.isCountable = false;
        if(this.isCountable) count = 0;
        else count = 1;
        this.tool = null;
        this.display = display;
    }

}
