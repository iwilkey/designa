package dev.iwilkey.designa.entity.creature.passive;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.Settings;
import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.building.BuildingHandler;
import dev.iwilkey.designa.entity.creature.Creature;
import dev.iwilkey.designa.gfx.Animation;
import dev.iwilkey.designa.gfx.Geometry;
import dev.iwilkey.designa.gfx.Renderer;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.inventory.Inventory;
import dev.iwilkey.designa.item.Item;
import dev.iwilkey.designa.item.ItemType;
import dev.iwilkey.designa.item.creator.CategoryItemRecipeList;
import dev.iwilkey.designa.item.creator.ItemCreator;
import dev.iwilkey.designa.world.World;

public class Player extends Creature {

    private final Animation[] animations;
    public Inventory inventory;
    public ItemCreator itemCreator;
    public BuildingHandler buildingHandler;

    public Player(World world, float x, float y) {
        super(world, x, y);

        animations = new Animation[2];
        animations[0] = new Animation(5, Assets.walk_right);
        animations[1] = new Animation(5, Assets.walk_left);

        inventory = new Inventory(world, this);
        itemCreator = new ItemCreator(this, 10, Game.WINDOW_HEIGHT - 10,
                ((Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING) * 5),
                ((Settings.GUI_SLOT_SIZE + Settings.GUI_SLOT_SPACING) * 5));
        buildingHandler = new BuildingHandler(world, this);

        collider.width -= 12;
        collider.x += 4;
    }

    private void control() {
        if(InputHandler.moveRightRequest) xMove = speed;
        else if(InputHandler.moveLeftRequest) xMove = -speed;
        else xMove = 0;
        if(InputHandler.jumpRequest && !isJumping && isGrounded) {
            jump();
            InputHandler.jumpRequest = false;
        }
    }

    @Override
    public void tick() {
        if(!ItemCreator.isActive) control();
        else {
        	xMove = 0;
        	InputHandler.jumpRequest = false;
        }
        move();
        Renderer.getCamera().centerOnEntity(this);
        buildingHandler.tick();
        itemCreator.tick();
        // drawCollider();
    }

    private void drawCollider() {
        Geometry.requests.add(new Geometry.RectangleOutline(collider.x, collider.y, collider.width, collider.height));
    }

    @Override
    public void render(Batch b) {
        b.draw(currentSprite(), x, y, width, height);
        renderWield(b);
    }

    Item currentWield = null;
    ItemType wieldType = null;
    private void renderWield(Batch b) {

        currentWield = (inventory.selectedSlot().item != null) ? inventory.selectedSlot().item : null;
        if(currentWield == null) {
            wieldType = null;
            return;
        }
        wieldType = currentWield.getType();

        // Facing right
        if(facingRight) {
            if (wieldType instanceof ItemType.NonCreatableItem.PlaceableTile)
                b.draw(currentWield.getTexture(), x + 1, y + 4, Item.ITEM_WIDTH, Item.ITEM_HEIGHT);
            if (wieldType instanceof ItemType.CreatableItem.Resource)
            	b.draw(currentWield.getTexture(), x + 1, y + 5, Item.ITEM_WIDTH, Item.ITEM_HEIGHT);
            if (wieldType instanceof ItemType.CreatableItem.Tool)
            	b.draw(currentWield.getTexture(), x + 4, y + 9, Item.ITEM_WIDTH, Item.ITEM_HEIGHT);

        // Facing left
        } else {
            if (wieldType instanceof ItemType.NonCreatableItem.PlaceableTile)
                b.draw(currentWield.getTexture(), x + 12, y + 4, Item.ITEM_WIDTH, Item.ITEM_HEIGHT);
            if (wieldType instanceof ItemType.CreatableItem.Resource)
            	b.draw(currentWield.getTexture(), x + 12, y + 5, Item.ITEM_WIDTH, Item.ITEM_HEIGHT);
            if (wieldType instanceof ItemType.CreatableItem.Tool)
            	b.draw(currentWield.getTexture(), x + 18, y + 10, 0, 0, Item.ITEM_WIDTH, Item.ITEM_HEIGHT, 1, 1, 112);
        }

    }

    private TextureRegion currentSprite() {
        if(isGrounded) {
            if (isMoving) {
                if (facingRight) return animations[0].getCurrentFrame();
                else return animations[1].getCurrentFrame();
            } else {
                if (facingRight) return Assets.player[1];
                else return Assets.player[0];
            }
        } else {
            if (facingRight) return Assets.player[1];
            else return Assets.player[0];
        }
    }

    @Override
    public void die() {

    }

}
