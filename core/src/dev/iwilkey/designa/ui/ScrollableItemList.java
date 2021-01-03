package dev.iwilkey.designa.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.Settings;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.audio.Audio;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.inventory.ComprehensiveInventory;
import dev.iwilkey.designa.inventory.Inventory;
import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.ItemType;
import dev.iwilkey.designa.item.creator.ItemCreator;

import java.awt.Rectangle;
import java.util.ArrayList;

public class ScrollableItemList extends Scrollable {

    public TextureRegion slotTexture, selectTexture;
    public ArrayList<Slot> slots;
    public int xSlotOffset = 0, selectedSlot;
    public float velocityX = 0;

    public ScrollableItemList(int x, int y, int width, int height) {
        super(x, y, width, height);
        slots = new ArrayList<>();
        slotTexture = Assets.inventorySlot; selectTexture = Assets.inventorySelector;
    }

    public int sizeOfList() {
        return slots.size();
    }

    public void add(Item item) {

        Rectangle itemRect = new Rectangle(
                collider.x + (sizeOfList() * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)),
                y, Settings.GUI_SLOT_SIZE, Settings.GUI_SLOT_SIZE
        );

        slots.add(new Slot(item, itemRect, true, new UIText("1",
                0, itemRect.x + (Settings.GUI_SLOT_SIZE - 10), itemRect.y)));

    }

    public void remove(Item item, Rectangle r) {
        for(Slot slot : slots)
            if(slot.item == item && slot.collider == r)
                slots.remove(slot);
    }

    public Slot selectedSlot() {
        for(int i = 0; i < slots.size(); i++)
            if(i == selectedSlot) return slots.get(i);
        return null;
    }

    @Override
    public void tick() {
        selectSlot();
        if(this instanceof Inventory && !ItemCreator.isActive) act();
        else if (!(this instanceof Inventory)) act();
    }


    private void act() {

        checkBounds();
        if(hovering) {
            isScrolling = InputHandler.leftMouseButton;
            if(isScrolling) {
                dx = (InputHandler.dx > 0) ? (float)Math.min(InputHandler.dx, Settings.GUI_SCROLL_SENSITIVITY) :
                        (float)Math.max(InputHandler.dx, -Settings.GUI_SCROLL_SENSITIVITY);
                xSlotOffset -= dx;
                velocityX =  (dx > 0) ? (float)Math.min(InputHandler.dx, Settings.GUI_SCROLL_SENSITIVITY) :
                        (float)Math.max(InputHandler.dx, -Settings.GUI_SCROLL_SENSITIVITY); // This is a ternary
            }

            if(ItemCreator.isActive) {
                velocityX -= (InputHandler.scrollWheelRequestValue * Settings.GUI_SCROLLWHEEL_SENSITIVITY != 0.0f) ?
                        InputHandler.scrollWheelRequestValue * Settings.GUI_SCROLLWHEEL_SENSITIVITY : 0;
                InputHandler.scrollWheelRequestValue = 0;
            }
        } else {
            dx = 0;
            isScrolling = false;

            if(!ItemCreator.isActive) {
                velocityX -= (InputHandler.scrollWheelRequestValue * Settings.GUI_SCROLLWHEEL_SENSITIVITY != 0.0f) ?
                        InputHandler.scrollWheelRequestValue * Settings.GUI_SCROLLWHEEL_SENSITIVITY : 0;
                InputHandler.scrollWheelRequestValue = 0;
            }

        }

        if(velocityX < 0) velocityX = (velocityX + Settings.GUI_ITEM_LIST_FRICTION < 0) ? velocityX + Settings.GUI_ITEM_LIST_FRICTION : 0;
        else velocityX = (velocityX - Settings.GUI_ITEM_LIST_FRICTION > 0) ? velocityX - Settings.GUI_ITEM_LIST_FRICTION : 0;
        xSlotOffset -= velocityX;
    }

    int s = 0, center = 0;
    protected void selectSlot() {
        s = 0;
        for (Slot slot : slots) {
            center = (int) (((collider.x + (collider.width / 2)) / UIObject.XSCALE));
            if (slot.collider.x - xSlotOffset > center - (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING) &&
                    slot.collider.x - xSlotOffset < center + (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)) {
                if(selectedSlot == s) return;
                selectedSlot = s;
                Audio.playSFX(Assets.invClick, 0.3f);
                return;
            }
            s++;
        }
    }

    private void checkBounds() {
        if(xSlotOffset < -(2 * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)))
            xSlotOffset += Settings.GUI_ITEM_LIST_FRICTION * 15;
        if(xSlotOffset > -(2 * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)) +
                ((sizeOfList() - 1) * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)))
            xSlotOffset -= Settings.GUI_ITEM_LIST_FRICTION * 15;
        if(xSlotOffset < -(4 * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)))
            xSlotOffset = -(4 * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING));
        if(xSlotOffset > -(2 * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)) +
                ((sizeOfList() + 1) * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)))
            xSlotOffset = -(2 * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING)) +
                    ((sizeOfList() + 1) * (Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING));
    }

    public void move(float x, float y) {
        relRect.x += x; relRect.y += y;
        this.x += x; this.y += y;

        for(Slot s : slots) {
            s.collider.x += x;
            s.collider.y += y;
        }

        onResize(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);

    }

    public float eval, percent;
    public int s1 = 0;
    public boolean selected;
    @Override
    public void render(Batch b) {
        s1 = 0;
        for(Slot slot : slots) {
            selected = s1 == selectedSlot;
            s1++;

            if(slot.collider.x - xSlotOffset <= relRect.x - Settings.GUI_SLOT_SIZE - Settings.GUI_SLOT_SPACING) continue;
            if(slot.collider.x - xSlotOffset >= relRect.x + relRect.width) continue;

            if(slot.collider.x - xSlotOffset < relRect.x) {
                eval = (slot.collider.x - xSlotOffset) - (relRect.x - Settings.GUI_SLOT_SIZE - Settings.GUI_SLOT_SPACING); // 68 start -> 0
                percent = (eval * UIObject.XSCALE) / 68;
                b.draw(slotTexture, relRect.x, slot.collider.y, eval * UIObject.XSCALE, eval * UIObject.YSCALE);
                if (slot.item != null && slot != ComprehensiveInventory.slotCurrentlyUp) {
                    b.draw(slot.item.getTexture(), relRect.x + (((Settings.GUI_SLOT_SIZE - Settings.GUI_ITEM_TEXTURE_SIZE) / 2f) * percent),
                            slot.collider.y + (((Settings.GUI_SLOT_SIZE - Settings.GUI_ITEM_TEXTURE_SIZE) / 2f) * percent),
                            ((float)Settings.GUI_ITEM_TEXTURE_SIZE / Settings.GUI_SLOT_SIZE) * (eval * UIObject.XSCALE),
                            ((float)Settings.GUI_ITEM_TEXTURE_SIZE / Settings.GUI_SLOT_SIZE) * (eval * UIObject.YSCALE));
                    if(slot.isCountable && slot.count != 0) slot.display.renderExact(b, (int)(relRect.x - ((Settings.GUI_SLOT_SIZE - 10) * (1 - percent)) + (Settings.GUI_SLOT_SIZE - 10)),
                            slot.display.y, (int)Math.max(Settings.GUI_FONT_SIZE * percent, 1));
                }
                if(selected) b.draw(selectTexture, relRect.x, slot.collider.y, eval * UIObject.XSCALE, eval * UIObject.YSCALE);
                continue;
            } else if (slot.collider.x - xSlotOffset > relRect.x + relRect.width - Settings.GUI_SLOT_SIZE - Settings.GUI_SLOT_SPACING) {
                eval = slot.collider.x - xSlotOffset - relRect.x - relRect.width;
                percent = Math.abs((eval * UIObject.XSCALE) / 68);
                b.draw(slotTexture, slot.collider.x - xSlotOffset, slot.collider.y, -eval * UIObject.XSCALE, -eval * UIObject.YSCALE);
                if (slot.item != null && slot != ComprehensiveInventory.slotCurrentlyUp) {
                    b.draw(slot.item.getTexture(), slot.collider.x - xSlotOffset + (((Settings.GUI_SLOT_SIZE - Settings.GUI_ITEM_TEXTURE_SIZE) / 2f) * percent),
                            slot.collider.y + (((Settings.GUI_SLOT_SIZE - Settings.GUI_ITEM_TEXTURE_SIZE) / 2f) * percent),
                            ((float)Settings.GUI_ITEM_TEXTURE_SIZE / Settings.GUI_SLOT_SIZE) * -(eval * UIObject.XSCALE),
                            ((float)Settings.GUI_ITEM_TEXTURE_SIZE / Settings.GUI_SLOT_SIZE) * -(eval * UIObject.YSCALE));
                    if(slot.isCountable && slot.count != 0) slot.display.renderExact(b, relRect.x + relRect.width - 10,
                            slot.display.y, (int)Math.max(Settings.GUI_FONT_SIZE * percent, 1));
                }
                if(selected) b.draw(selectTexture, slot.collider.x - xSlotOffset, slot.collider.y, -eval * UIObject.XSCALE, -eval * UIObject.YSCALE);
                continue;
            }

            b.draw(slotTexture, slot.collider.x - xSlotOffset, slot.collider.y, Settings.GUI_SLOT_SIZE, Settings.GUI_SLOT_SIZE);
            if (slot.item != null && slot != ComprehensiveInventory.slotCurrentlyUp) {
                b.draw(slot.item.getTexture(), slot.collider.x + ((Settings.GUI_SLOT_SIZE - Settings.GUI_ITEM_TEXTURE_SIZE) / 2f) - xSlotOffset,
                        slot.collider.y + ((Settings.GUI_SLOT_SIZE - Settings.GUI_ITEM_TEXTURE_SIZE) / 2f), Settings.GUI_ITEM_TEXTURE_SIZE, Settings.GUI_ITEM_TEXTURE_SIZE);
                if(slot.isCountable && slot.count != 0) slot.display.renderExact(b, slot.display.x - xSlotOffset - 10, slot.display.y, Settings.GUI_FONT_SIZE);
            }
            if(selected) b.draw(selectTexture, slot.collider.x - xSlotOffset, slot.collider.y, slot.collider.width, slot.collider.height);
        }

    }

    @Override
    public void onClick() {}

}
