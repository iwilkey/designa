package dev.iwilkey.designa.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.creator.ItemCreator;

import java.awt.Rectangle;
import java.util.ArrayList;

public class ScrollableItemList extends Scrollable {

    public static final int SLOT_SIZE = 64, SLOT_SPACE = 4,
        ITEM_TEXTURE_SIZE = 32, SCROLL_SENSITIVITY = 15, SCROLLWHEEL_SENSITIVITY = 2; // px
    public static final float FRICTION = 0.3f;

    public TextureRegion slotTexture, selectTexture;
    public ArrayList<Slot> slots;
    public int xSlotOffset = 0, selectedSlot;
    private float velocityX = 0;

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
                collider.x + (sizeOfList() * (SLOT_SIZE + SLOT_SPACE)),
                y, SLOT_SIZE, SLOT_SIZE
        );

        slots.add(new Slot(item, itemRect, true, new UIText("1",
                18, itemRect.x + (SLOT_SIZE - 10), itemRect.y)));

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
        updateDisplay();
        selectSlot();
        act();
    }

    private void updateDisplay() {
        for(Slot slot : slots)
            slot.display.message = Integer.toString(slot.count);
    }

    private void act() {
        checkBounds();
        if(hovering) {
            isScrolling = InputHandler.leftMouseButton;
            if(isScrolling) {
                dx = (InputHandler.dx > 0) ? (float)Math.min(InputHandler.dx, SCROLL_SENSITIVITY) :
                        (float)Math.max(InputHandler.dx, -SCROLL_SENSITIVITY);
                xSlotOffset -= dx;
                velocityX =  (dx > 0) ? (float)Math.min(InputHandler.dx, SCROLL_SENSITIVITY) :
                        (float)Math.max(InputHandler.dx, -SCROLL_SENSITIVITY); // This is a ternary
            }

            if(ItemCreator.isActive) {
                velocityX -= (InputHandler.scrollWheelRequestValue * SCROLLWHEEL_SENSITIVITY != 0.0f) ?
                        InputHandler.scrollWheelRequestValue * SCROLLWHEEL_SENSITIVITY : 0;
                InputHandler.scrollWheelRequestValue = 0;
            }
        } else {
            dx = 0;
            isScrolling = false;

            if(!ItemCreator.isActive) {
                velocityX -= (InputHandler.scrollWheelRequestValue * SCROLLWHEEL_SENSITIVITY != 0.0f) ?
                        InputHandler.scrollWheelRequestValue * SCROLLWHEEL_SENSITIVITY : 0;
                InputHandler.scrollWheelRequestValue = 0;
            }

        }

        if(velocityX < 0) velocityX = (velocityX + FRICTION < 0) ? velocityX + FRICTION : 0;
        else velocityX = (velocityX - FRICTION > 0) ? velocityX - FRICTION : 0;
        xSlotOffset -= velocityX;
    }

    int s = 0, center = 0;
    private void selectSlot() {
        s = 0;
        for(Slot slot : slots) {
            center = (int)(((collider.x + (collider.width / 2)) / UIObject.XSCALE));
            if(slot.collider.x - xSlotOffset > center - ((SLOT_SIZE + SLOT_SPACE)) &&
                    slot.collider.x - xSlotOffset  < center + ((SLOT_SIZE + SLOT_SPACE))) {
                selectedSlot = s;
                return;
            }
            s++;
        }
    }

    private void checkBounds() {
        if(xSlotOffset < -(2 * (SLOT_SIZE + SLOT_SPACE)))
            xSlotOffset += FRICTION * 15;
        if(xSlotOffset > -(2 * (SLOT_SIZE + SLOT_SPACE)) + ((sizeOfList() - 1) * (SLOT_SIZE + SLOT_SPACE)))
            xSlotOffset -= FRICTION * 15;
        if(xSlotOffset < -(4 * (SLOT_SIZE + SLOT_SPACE)))
            xSlotOffset = -(4 * (SLOT_SIZE + SLOT_SPACE));
        if(xSlotOffset > -(2 * (SLOT_SIZE + SLOT_SPACE)) + ((sizeOfList() + 1) * (SLOT_SIZE + SLOT_SPACE)))
            xSlotOffset = -(2 * (SLOT_SIZE + SLOT_SPACE)) + ((sizeOfList() + 1) * (SLOT_SIZE + SLOT_SPACE));
    }

    float eval, percent;
    int s1 = 0;
    boolean selected;
    @Override
    public void render(Batch b) {
        s1 = 0;
        for(Slot slot : slots) {
            selected = s1 == selectedSlot;
            s1++;

            if(slot.collider.x - xSlotOffset <= relRect.x - SLOT_SIZE - SLOT_SPACE) continue;
            if(slot.collider.x - xSlotOffset >= relRect.x + relRect.width) continue;

            if(slot.collider.x - xSlotOffset < relRect.x) {
                eval = (slot.collider.x - xSlotOffset) - (relRect.x - SLOT_SIZE - SLOT_SPACE); // 68 start -> 0
                percent = (eval * UIObject.XSCALE) / 68;
                b.draw(slotTexture, relRect.x, slot.collider.y, eval * UIObject.XSCALE, eval * UIObject.YSCALE);
                if (slot.item != null) {
                    b.draw(slot.item.getTexture(), relRect.x + (((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) * percent),
                            slot.collider.y + (((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) * percent),
                            ((float)ITEM_TEXTURE_SIZE / SLOT_SIZE) * (eval * UIObject.XSCALE),
                            ((float)ITEM_TEXTURE_SIZE / SLOT_SIZE) * (eval * UIObject.YSCALE));
                }
                if(slot.isCountable && slot.count != 0) slot.display.render(b, (int)(relRect.x - ((SLOT_SIZE - 10) * (1 - percent)) + (SLOT_SIZE - 10)),
                        slot.display.y, (int)Math.max(slot.display.size * percent, 1));
                if(selected) b.draw(selectTexture, relRect.x, slot.collider.y, eval * UIObject.XSCALE, eval * UIObject.YSCALE);
                continue;
            } else if (slot.collider.x - xSlotOffset > relRect.x + relRect.width - SLOT_SIZE - SLOT_SPACE) {
                eval = slot.collider.x - xSlotOffset - relRect.x - relRect.width;
                percent = Math.abs((eval * UIObject.XSCALE) / 68);
                b.draw(slotTexture, slot.collider.x - xSlotOffset, slot.collider.y, -eval * UIObject.XSCALE, -eval * UIObject.YSCALE);
                if (slot.item != null) {
                    b.draw(slot.item.getTexture(), slot.collider.x - xSlotOffset + (((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) * percent),
                            slot.collider.y + (((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) * percent),
                            ((float)ITEM_TEXTURE_SIZE / SLOT_SIZE) * -(eval * UIObject.XSCALE),
                            ((float)ITEM_TEXTURE_SIZE / SLOT_SIZE) * -(eval * UIObject.YSCALE));
                }
                if(slot.isCountable && slot.count != 0) slot.display.render(b, relRect.x + relRect.width - 10,
                        slot.display.y, (int)Math.max(slot.display.size * percent, 1));
                if(selected) b.draw(selectTexture, slot.collider.x - xSlotOffset, slot.collider.y, -eval * UIObject.XSCALE, -eval * UIObject.YSCALE);
                continue;
            }

            b.draw(slotTexture, slot.collider.x - xSlotOffset, slot.collider.y, slot.collider.width, slot.collider.height);
            if (slot.item != null) {
                b.draw(slot.item.getTexture(), slot.collider.x + ((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f) - xSlotOffset,
                        slot.collider.y + ((SLOT_SIZE - ITEM_TEXTURE_SIZE) / 2f), ITEM_TEXTURE_SIZE, ITEM_TEXTURE_SIZE);
            }
            if(slot.isCountable && slot.count != 0) slot.display.render(b, slot.display.x - xSlotOffset - 10, slot.display.y, slot.display.size);
            if(selected) b.draw(selectTexture, slot.collider.x - xSlotOffset, slot.collider.y, slot.collider.width, slot.collider.height);
        }

    }

    @Override
    public void onClick() {}

}
