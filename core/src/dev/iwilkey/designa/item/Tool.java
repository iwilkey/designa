package dev.iwilkey.designa.item;

import dev.iwilkey.designa.inventory.Slot;

public class Tool {

	public Slot slot;
	public ItemType.CreatableItem.Tool toolType;
	public int timesUsed = 0;
	
	public Tool(ItemType.CreatableItem.Tool it, Slot s) {
		this.toolType = it;
		this.slot = s;
	}
	
	public void use() {
		timesUsed++;
	}
	
	public int calculate() {
		float percent = 100.0f - (((float)timesUsed /
    			((toolType)).strength) * 100.0f);
    	return (int)Math.round(percent);
	}
	
}
