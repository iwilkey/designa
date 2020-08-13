package com.iwilkey.designa.entities.creature;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.building.BuildingHandler;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.gui.Hud;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.ToolSlot;
import com.iwilkey.designa.items.ItemType;

import java.awt.*;

public class Player extends Creature {

    // Animations
    private final Animation[] animations;

    // HUD
    private final Hud hud;

    // Building
    private final BuildingHandler buildingHandler;

    // Inventory
    private final Inventory inventory;
    private final ToolSlot toolSlot;

    public Player(GameBuffer gb, float x, float y) {
        super(gb, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);

        // Setting the collider
        collider.width = width - 9;
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

        // Init Animations
        animations = new Animation[2];
        animations[0] = new Animation(100, Assets.walk_right);
        animations[1] = new Animation(100, Assets.walk_left);

        // HUD
        hud = new Hud(this);

        // Building
        buildingHandler = new BuildingHandler(gb, this);

        // Inventory
        inventory = new Inventory(gb);
        toolSlot = new ToolSlot(inventory);

        facingLeft = true;

    }

    private void control() {
        xMove = 0;
        yMove = 0;

        if(InputHandler.moveRight) xMove += speed;
        if(InputHandler.moveLeft) xMove -= speed;
        if(InputHandler.jumpRequest && !isJumping && isGrounded) jump();
        InputHandler.jumpRequest = false;

    }

    @Override
    public void tick() {

        // Tick animations
        for(Animation anim : animations) {
            anim.tick();
        }

        // Tick HUD
        hud.tick();

        // Move
        control();
        move();

        // Center camera
        gb.getCamera().centerOnEntity(this);

        // Building
        buildingHandler.tick();

        // Attack
        checkAttacks();

        // Inventory
        inventory.tick();
        toolSlot.tick();

    }

    private void checkAttacks() {

        Rectangle cb = getCollisionBounds(0,0); // Get coords of the colision box of player
        Rectangle ar = new Rectangle();
        int arSize = 20; // If a player is within 20 px of an entity and they attack, they will hurt the entity
        ar.width = arSize;
        ar.height = arSize;

        if(InputHandler.attack && facingLeft) {
            ar.x = cb.x - arSize;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        } else if(InputHandler.attack && facingRight) {
            ar.x = cb.x + cb.width;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        } else {
            return;
        }

        InputHandler.attack = false;

        for (Entity e : gb.getWorld().getEntityHandler().getEntities()) {
            if(e.equals(this)) continue;
            if(e.getCollisionBounds(0, 0).intersects(ar)) {
                e.hurt(1);
                return;
            }
        }

    }

    @Override
    public void render(Batch b) {
        if(isFlashing && flashInterval >= flashIntervalTime) {
            b.draw(currentSprite(), x, y, width, height);
            if(ToolSlot.currentItem != null) {
                try {
                    if (facingRight)  {
                        if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.Drill) {
                            b.draw(ToolSlot.currentItem.getItem().getTexture(), x, y + 5, 12, 12);
                        } else {
                            b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 2, y + 5, 6, 6);
                        }
                    }

                    else {
                        if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.Drill) {
                            b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 10, y + 4,
                                    6, 6, 12, 12, 1, 1, 180);
                        } else {
                            b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 14, y + 5, 6, 6);
                        }
                    }
                } catch (NullPointerException ignored) {}
            }
        } else if (!isFlashing) {
            b.draw(currentSprite(), x, y, width, height);
            if(ToolSlot.currentItem != null) {
                try {
                    if (facingRight)  {
                        if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.Drill) {
                            b.draw(ToolSlot.currentItem.getItem().getTexture(), x, y + 5, 12, 12);
                        } else {
                            b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 2, y + 5, 6, 6);
                        }
                    }

                    else {
                        if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.Drill) {
                            b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 10, y + 4,
                                    6, 6, 12, 12, 1, 1, 180);
                        } else {
                            b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 14, y + 5, 6, 6);
                        }
                    }
                } catch (NullPointerException ignored) {}
            }
        }

        // renderCollider(b);
    }

    @Override
    public void die() {

    }

    private TextureRegion currentSprite() {
        if(isMoving && isGrounded) {
            if(facingLeft) {
                return animations[1].getCurrentFrame();
            } else if (facingRight){
                return animations[0].getCurrentFrame();
            }
        } else if (isMoving) {
            if(facingLeft) {
                return Assets.player_jump[0];
            } else {
                return Assets.player_jump[1];
            }
        } else {
            if(facingLeft) {
                return Assets.player[0];
            } else {
                return Assets.player[1];
            }
        }

        return null;
    }

    public void setFace(int face) {
        if(face == 0) {
            facingLeft = true;
            facingRight = false;
        } else {
            facingLeft = false;
            facingRight = true;
        }
    }

    public GameBuffer getGameBuffer() { return gb; }
    public Hud getHUD() { return hud; }
    public BuildingHandler getBuildingHandler() { return buildingHandler; }
    public Inventory getInventory() { return inventory; }
    public ToolSlot getToolSlot() { return toolSlot; }

}
