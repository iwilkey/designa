package dev.iwilkey.designa.inventory;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.ui.ScrollableItemList;
import dev.iwilkey.designa.ui.UIText;
import dev.iwilkey.designa.world.World;

public class Inventory extends ScrollableItemList {

    public final int STORAGE_CAP = 99;
    public UIText currentItemLabel;


    Player player;
    World world;

    public Inventory(World world, Player player) {
        super(Game.WINDOW_WIDTH - ((SLOT_SIZE + SLOT_SPACE) * 5) - 10,
                Game.WINDOW_HEIGHT - SLOT_SIZE - 10, (SLOT_SIZE + SLOT_SPACE) * 5, SLOT_SIZE);

        this.player = player;
        this.world = world;

        world.uiManager.addScrollableItemList(this);

        for(int i = 0; i < 20; i++) super.add(null);

        currentItemLabel = new UIText("", 22, collider.x, collider.y + 64);
    }

    @Override
    public void tick() {
        super.tick();

        for(Slot slot : slots) {

            if (slot.count <= 0) {
                slot.item = null;
                continue;
            }

            if(slot.item != null)
                if (selectedSlot() == slot)
                    updateLabel(slot.item);

        }

        if (selectedSlot().item == null)
            updateLabel(null);
    }

    private void updateLabel(Item i) {
        if(i != null)
            currentItemLabel.message = i.name();
        else currentItemLabel.message = "";
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

    @Override
    public void render(Batch b) {
        super.render(b);
        currentItemLabel.render(b, currentItemLabel.x, currentItemLabel.y, 22);
    }

}
