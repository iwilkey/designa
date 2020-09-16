package com.iwilkey.designa.entities.statics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.world.World;

/**

 This class defines a tree.

 @author Ian Wilkey (iwilkey)
 @version VERSION
 @since 7/21/2020

 */

// Note: This object is a StaticEntity.
public class Tree extends StaticEntity {

    /**
     * Global vars
     */

    // The texture this tree will rendered with.
    TextureRegion texture;

    // Mock physics
    float gravity = -3.5f, timeInAir = 0f;

    /**
     * Tree constructor.
     * @param gb A tree needs the GameBuffer.
     * @param x The x location of the tree.
     * @param y The y location of the tree.
     */
    public Tree(GameBuffer gb, float x, float y) {
        // Invoke the StaticEntity constructor.
        super(gb, x, y, Tile.TILE_SIZE * 4, Tile.TILE_SIZE * 16);

        // Set the texture randomly from three different tree textures.
        int i = MathUtils.random(0, 2); texture = Assets.trees[i];

        // Set the health of this tree between the range below.
        health = MathUtils.random(35, 40);

        // Edit the x-value so as to always render on a tile instead of the middle of one.
        this.x -= (Tile.TILE_SIZE * 2) - (Tile.TILE_SIZE / 2f);

        // Set the collider.
        collider.width = width - (int)(width / 1.2f);
        collider.height = height;
        collider.x = (width / 2) - (collider.width / 2);
        collider.y = (height / 2) - (collider.height / 2) + 1;
    }

    /**
     * The tree tick method.
     */
    @Override
    public void tick() {
        // If the tree is dead, let it fall.
        if(!active) yMove();
    }

    /**
     * This method defines how a tree will tend to move along the y-axis if invoked.
     */
    private void yMove() {
        // Increment timeInAir.
        timeInAir += 0.02f;
        // Simple acceleration based on gravitational constant and the time it has been in the air.
        y = y + gravity * timeInAir;
    }

    /**
     * Thie method defines what happens when a tree is hurt (or damaged)
     * @param amt The amount of damage dealt.
     */
    @Override
    public void hurt(int amt) {
        // If the tree is hurt enough...
        if (health <= 0) {
            // Deactivate it.
            active = false;
            // Invoke die() method.
            die();
            // Leave the method.
            return;
        }
        // If the player is not in the inventory...
        if(!Inventory.active) {
            // Decrement the health by the amount dealt.
            health -= amt;

            // Make it a 33% chance bark falls.
            if (MathUtils.random(0, 2) == 2) {
                // Define some variants in the location the bark spawns.
                int yy = MathUtils.random(50, 200); int xx = MathUtils.random(-4, 26);
                // Spawn the bark.
                World.getItemHandler().addItem(Assets.barkResource.createNew((int) x + 16 + xx, (int) y + yy));
            }

            // Play a variant of the treeHit sound.
            Assets.treeHit[MathUtils.random(0, 2)].play();
        }
    }

    /**
     * This method defines what happens when a tree dies.
     */
    @Override
    public void die() {
        // Remove the tree record from the world.
        for(int t = 0; t < World.trees.size(); t++) if(World.trees.get(t) ==
                (x + (Tile.TILE_SIZE * 2) - (Tile.TILE_SIZE / 2f)) /
                        Tile.TILE_SIZE) World.trees.remove(t);

        // Create some random spawn count to determine how many bark will be spawned.
        int spawnAmount = MathUtils.random(2, 8);
        // Spawn that many bark.
        for(int i = 0; i < spawnAmount; i++) {
            // Create variation in location.
            int yy = MathUtils.random(50, 200); int xx = MathUtils.random(-4, 26);
            // Spawn bark.
            World.getItemHandler().addItem(Assets.barkResource.createNew((int)x + 16 + xx, (int)y + yy));
        }

        // Play the treeFall sound.
        Assets.treeFall[MathUtils.random(0,2)].play();
    }

    /**
     * The tree render method.
     * @param b All render methods need the graphics batch.
     */
    @Override
    public void render(Batch b) {
        // Draw the texture at the correct location. Simple.
        b.draw(texture, x, y, width, height);
    }
}
