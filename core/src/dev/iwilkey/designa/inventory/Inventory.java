package dev.iwilkey.designa.inventory;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.world.World;

public class Inventory extends ScrollableItemList {

    public final int STORAGE_CAP = 99;

    Player player;
    World world;

    public Inventory(World world, Player player) {
        super(Game.WINDOW_WIDTH - ((SLOT_SIZE + SLOT_SPACE) * 5) - 20,
                Game.WINDOW_HEIGHT - 100, (SLOT_SIZE + SLOT_SPACE) * 5, SLOT_SIZE);

        this.player = player;
        this.world = world;

        world.uiManager.addScrollableItemList(this);

        for(int i = 0; i < 20; i++) super.add(null);
    }

    @Override
    public void tick() {
        super.tick();
    }

    public Slot selectedSlot() {
        for(int i = 0; i < slots.size(); i++)
            if(i == selectedSlot) return slots.get(i);
        return null;
    }

    @Override
    public void add(Item item) {
        for(Slot slot : slots) {
            if(slot.item == item && slot.count + 1 <= STORAGE_CAP) {
                slot.count++;
                return;
            }
            if(slot.item == null) {
                slot.item = item;
                slot.count++;
                return;
            }
        }
    }
}
