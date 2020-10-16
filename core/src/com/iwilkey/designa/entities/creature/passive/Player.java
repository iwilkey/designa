package com.iwilkey.designa.entities.creature.passive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.building.BuildingHandler;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.entities.creature.Creature;
import com.iwilkey.designa.entities.creature.violent.Enemy;
import com.iwilkey.designa.entities.creature.violent.TerraBot;
import com.iwilkey.designa.gfx.Animation;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.gui.Hud;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.ToolSlot;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.states.GameState;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.AmbientCycle;
import com.iwilkey.designa.world.World;

import java.awt.Rectangle;
import java.util.ArrayList;

/**

 This class defines the player. It includes user-control, animation, over-all functionality and other game logic.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

// Note: This object is a Creature.
public class Player extends Creature {

    /**
     * Global vars
     */

    // Final vars
    private final BuildingHandler buildingHandler;
    private final Inventory inventory;
    private final ToolSlot toolSlot;
    private final Hud hud;
    private final Animation[] animations;

    // Player-owned crates
    public ArrayList<Crate> crates = new ArrayList<Crate>();

    // Longs
    private long timer, actionCooldown = 20;

    // Booleans
    private boolean onLadder = false, ladderMode = false;

    private Rectangle hitBox;

    /**
     * Player constructor.
     * @param gb This class needs the GameBuffer.
     * @param x The x spawn point of the player.
     * @param y The y spawn point of the player.
     */
    public Player(GameBuffer gb, float x, float y, AmbientCycle ac) {
        // Call the Creature constructor.
        super(gb, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);

        // Create an HUD (Heads up display) instance.
        hud = new Hud(this, ac);

        // Create a BuildingHandler instance.
        buildingHandler = new BuildingHandler(gb, this);

        // Create an Inventory instance.
        inventory = new Inventory(gb);

        // Create a ToolSlot instance.
        toolSlot = new ToolSlot(inventory);

        // Set the spawn direction.
        facingLeft = true;

        // Set the collider.
        collider.width = (width - 3);
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;

        hitBox = new Rectangle(collider.x, collider.y, collider.width, collider.height);

        // Init animations
        animations = new Animation[4];
        animations[0] = new Animation(100, Assets.walk_right);
        animations[1] = new Animation(100, Assets.walk_left);
        animations[2] = new Animation(100, Assets.playerGunWalkRight);
        animations[3] = new Animation(100, Assets.playerGunWalkLeft);
    }

    /**
     * This method converts user input into player movement.
     */
    private void control() {
        // Set the the accelerations to zero each tick.
        xMove = 0; yMove = 0;

        // Convert control into movement.
        if(InputHandler.moveRight) xMove += speed;
        if(InputHandler.moveLeft) xMove -= speed;
        if(InputHandler.jumpRequest && !isJumping && isGrounded) {
            jump();
            InputHandler.jumpRequest = false;
        }
    }

    private void ladderMovement() {
        timeInAir = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            int ty = (int) (y + 0.75f + collider.y + collider.height) / Tile.TILE_SIZE;
            // If there is nothing to stop the Creature...
            if(!collisionWithTile((int) (x + collider.x) / Tile.TILE_SIZE, ty) &&
                    !collisionWithTile((int) (x + collider.x + collider.width) / Tile.TILE_SIZE, ty)) {
                y += 0.75f;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) y -= 0.75f;
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += 0.50f;
            setFace(1);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= 0.50f;
            setFace(0);
        }
    }

    /**
     * Player tick method. Called every frame.
     */
    @Override
    public void tick() {

        checkEnemyCollision();

        // Tick all animations forward.
        for (Animation anim : animations) anim.tick();

        // Tick HUD
        hud.tick();

        // Tick movement.
        checkLadder();
        control();
        if(!ladderMode || !onLadder) move();
        else ladderMovement();

        hitBox.x = (int)x; hitBox.y = (int)y;

        if(InputHandler.backBuildingToggleRequest) {
            addEnemy(pointerOnTileX(), pointerOnTileY());
        }

        try {
            if (InputHandler.rightMouseButtonDown && ToolSlot.currentItem.getItem() == Assets.carbonSampleResource) {
                if (BuildingHandler.inRange) {
                    if (ToolSlot.currentItem.itemCount - 1 >= 0) {
                        World.getEntityHandler().addEntity(new Npc(gb, pointerOnTileX() * Tile.TILE_SIZE,
                                LightManager.highestTile[pointerOnTileX()] * Tile.TILE_SIZE));
                        ToolSlot.currentItem.itemCount--;
                        Assets.invClick.play(1f);
                    }
                }
            }
        } catch (NullPointerException ignored) {}

        flashCheck();

        // Center the camera.
        gb.getCamera().centerOnEntity(this);

        // Tick BuildingHandler.
        buildingHandler.tick();

        // Check for user attack requests.
        checkAttacks();

        // Tick the Inventory.
        inventory.tick();

        // Tick the ToolSlot.
        toolSlot.tick();

        // Tick crates, if they exist in the world.
        if (crates.size() != 0) for (Crate crate : crates) crate.tick();

    }

    private void checkLadder() {
        int x = (int) (this.x + 12) / Tile.TILE_SIZE; int y = (int) (this.y) / Tile.TILE_SIZE;
        Tile tile = World.getTile(x, y);
        onLadder = (tile == Assets.ladderTile);
        ladderMode = onLadder;
    }

    private void checkEnemyCollision() {
        for(Entity e : World.getEntityHandler().getEntities()) {
            if(e instanceof Enemy) {
                if(((Enemy) e).hitBox.intersects(hitBox)) {
                    hurt((((Enemy) e).damagePotential));
                    World.particleHandler.startParticle("large-explosion", hitBox.x, hitBox.y);
                    Assets.explosion[MathUtils.random(0,2)].play(1f);
                    World.getEntityHandler().getEntities().remove(e);
                    flashDurationTimer = 0; flashInterval = 0; isFlashing = true;
                }
            }
        }
    }

    private void addEnemy(int x, int y) {
        World.getEntityHandler().addEntity(new TerraBot(gb, x * Tile.TILE_SIZE,
                LightManager.highestTile[x] * Tile.TILE_SIZE));
    }



    /**
     * This method will take into account where the cursor compared to the camera and evaluate
     * the proper x coordinate for the tile selected.
     * @return The proper x coordinate of the tile selected in the tile map.
     */
    private int pointerOnTileX() {
        return (int) ((((InputHandler.cursorX) - Camera.position.x) /
                Tile.TILE_SIZE) / Camera.scale.x);
    }

    /**
     * This method will take into account where the cursor compared to the camera and evaluate
     * the proper y coordinate for the tile selected.
     * @return The proper y coordinate of the tile selected in the tile map.
     */
    private int pointerOnTileY() {
        return (int) ((((InputHandler.cursorY) - Camera.position.y) /
                Tile.TILE_SIZE) / Camera.scale.y);
    }

    /**
     * This method checks and handles user attack requests.
     */
    private void checkAttacks() {

        // Create a rectangle that is the current collider of the player.
        Rectangle cb = getCollisionBounds(0,0);
        // Create a rectangle that will be the attack collider, so that anything inside of it will be damaged.
        Rectangle ar = new Rectangle();
        // Make the width and height a tile.
        int arSize = Tile.TILE_SIZE;
        ar.width = arSize; ar.height = arSize;

        // If there is a request to attack and the player is facing left...
        if(InputHandler.attack && facingLeft) {
            // Move the xValue over the arSize.
            ar.x = cb.x - arSize;
            // Move the yValue to be in the middle of the player body.
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        // Same algorithm for the right side.
        } else if(InputHandler.attack && facingRight) {
            ar.x = cb.x + cb.width;
            ar.y = cb.y + cb.height / 2 - arSize / 2;
        }

        // If the user requests a prolonged action...
        if(InputHandler.prolongedActionRequest) {
            // Increment the time until the action cool down then attack.
            timer++;
            if(timer > actionCooldown) {
                if(facingLeft) {
                    ar.x = cb.x - arSize;
                    ar.y = cb.y + cb.height / 2 - arSize / 2;
                } else if(facingRight) {
                    ar.x = cb.x + cb.width;
                    ar.y = cb.y + cb.height / 2 - arSize / 2;
                }

                // Attack
                attack(ar, 1);
                timer = 0;
                return;
            }
        } else timer = 0;

        // Reset request
        InputHandler.attack = false;

        // Attack
        attack(ar, 1);

    }

    /**
     * This method actually attacks the correct entity when called.
     * @param ar The rectangle representing the attack area.
     * @param amt The amount of damage needed to be done to the entity.
     */
    private void attack(Rectangle ar, int amt) {
        // Loop through all entities...
        for (Entity e : World.getEntityHandler().getEntities()) {
            if(e.equals(this)) continue; // Skip player...
            // Hurt the entity if it is colliding with the attack collider.
            if(e.getCollisionBounds(0, 0).intersects(ar)) {
                e.hurt(amt);
                return;
            }
        }
    }

    /**
     * This method will record a new crate into the player-owned crates list.
     * @param x The x location of the crate (in tiles).
     * @param y The y location of the crate (in tiles).
     */
    public void addCrate(int x, int y) {
        for(Crate crate : crates) if(crate.x == x && crate.y == y) return;
        crates.add(new Crate(inventory, x, y));
    }

    /**
     * This method will remove a crate from the player-owned crates list if it exists.
     * @param x The x location of the crate (in tiles).
     * @param y The y location of the crate (in tiles).
     */
    public void removeCrate(int x, int y) { crates.removeIf(crate -> crate.x == x && crate.y == y); }

    /**
     * Player render method.
     * @param b Every render method needs the graphics batch.
     */
    @Override
    public void render(Batch b) {
        // Render the body if not flashing.
        if(isFlashing && flashInterval >= flashIntervalTime) renderBody(b);
        else if (!isFlashing) renderBody(b);
        // b.draw(Assets.errorSelector, hitBox.x, hitBox.y, collider.width, collider.height);
    }

    /**
     * Player sub-render method. It specifically draws the current sprite in the animation and the tools held by the player.
     * @param b Every render, or sub-render method needs the graphics batch.
     */
    private void renderBody(Batch b) {
        // Draw the correct sprite.
        b.draw(currentSprite(), x, y, width, height);

        // Below handles drawing the item held.
        if(ToolSlot.currentItem != null) {
            try {
                // If facing right, draw items correctly based on logic below.
                if (facingRight)  {
                    if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.CreatableItem.Tool.Drill)
                        b.draw(ToolSlot.currentItem.getItem().getTexture(), x, y + 5, 12, 12);
                    else if (ToolSlot.currentItem.getItem().getName().equals("Torch"))
                        b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 4, y + 2,
                                6, 6, 8, 8, 1, 1, -90);
                    else if (ToolSlot.currentItem.getItem().getName().equals("Wrench"))
                        b.draw(ToolSlot.currentItem.getItem().getTexture(), x - 2.5f, y,
                                6, 6, 8, 8, 1, 1, 180);
                    else b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 2, y + 5, 6, 6);
                // Otherwise, they were facing left so draw items the way defined below.
                } else {
                    if(ToolSlot.currentItem.getItem().getItemType() instanceof ItemType.CreatableItem.Tool.Drill)
                        b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 10, y + 4,
                                6, 6, 12, 12, 1, 1, 180);
                    else if (ToolSlot.currentItem.getItem().getName().equals("Torch"))
                        b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 6, y + 6,
                                6, 6, 8, 8, 1, 1, 90);
                    else if (ToolSlot.currentItem.getItem().getName().equals("Wrench"))
                        b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 8.5f, y,
                                6, 6, 8, 8, 1, 1, 180);
                    else b.draw(ToolSlot.currentItem.getItem().getTexture(), x + 14, y + 5, 6, 6);
                }
            } catch (NullPointerException ignored) {}
        }
    }

    /**
     * This method defines what happens when the player dies.
     */
    @Override
    public void die() {
        GameState.hasLost = true;
    }

    /**
     * This method defers the proper animation or sprite to return to the render method based on the state of itself.
     * @return A TextureRegion denoting the sprite that needs to be rendered.
     */
    private TextureRegion currentSprite() {
        // If the player is not holding a gun...
        if(!gunWielding) {
            // If the player is moving and grounded...
            if (isMoving && isGrounded) {
                // Return the proper sprite.
                if (facingLeft) return animations[1].getCurrentFrame();
                else if (facingRight) return animations[0].getCurrentFrame();
            // If the player isn't grounded but still moving...
            } else if (isMoving) {
                // Return the proper sprite.
                if (facingLeft) return Assets.player_jump[0];
                else return Assets.player_jump[1];
            // Otherwise, the player is idle.
            } else {
                // So, return that.
                if (facingLeft) return Assets.player[0];
                else return Assets.player[1];
            }
        // Otherwise, the player does have a gun.
        } else {
            // Same algorithm as above, just replaced with the gun-wielding animations.
            if (isMoving && isGrounded) {
                if (facingLeft) return animations[3].getCurrentFrame();
                else if (facingRight) return animations[2].getCurrentFrame();
            } else if (isMoving) {
                if (facingLeft) return Assets.playerGun[0];
                else return Assets.playerGun[1];
            } else {
                if (facingLeft) return Assets.playerGun[0];
                else return Assets.playerGun[1];
            }
        }

        return null;
    }

    /**
     * This method will take in an integer value to defer what direction the player should face.
     * @param face The direction the player should face.
     */
    public void setFace(int face) {
        // 0 for left, anything else for right.
        if(face == 0) {
            facingLeft = true; facingRight = false;
        } else {
            facingLeft = false; facingRight = true;
        }
    }

    /*
     * GETTERS AND SETTERS
     */

    /**
     * GameBuffer getter.
     * @return The GameBuffer.
     */
    public GameBuffer getGameBuffer() { return gb; }

    /**
     * BuildingHandler getter.
     * @return The BuildingHandler.
     */
    public BuildingHandler getBuildingHandler() { return buildingHandler; }

    /**
     * Inventory getter.
     * @return The instance of the player inventory.
     */
    public Inventory getInventory() { return inventory; }

    /**
     * ToolSlot getter.
     * @return The instance of the ToolSlot.
     */
    public ToolSlot getToolSlot() { return toolSlot; }

    /**
     * HUD getter.
     * @return The instance of the HUD.
     */
    public Hud getHUD() { return hud; }

}
