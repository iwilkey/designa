package com.iwilkey.designa.defense;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.math.MathUtils;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.entities.Entity;
import com.iwilkey.designa.entities.creature.violent.Enemy;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gfx.Geometry;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.ToolSlot;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.utils.Utils;
import com.iwilkey.designa.world.World;

import java.awt.Rectangle;
import java.util.ArrayList;

public class WeaponType {

    public enum AmmoType {
        COPPER, SILVER, IRON, DIAMOND;
    }

    public static class Pellet {
        AmmoType type;
        short damagePotential;
        Rectangle collider;

        int x, y;
        float velX, velY;
        boolean active;

        public Pellet(AmmoType type, int x, int y, float impulseForceX, float impulseForceY) {
            this.x = x; this.y = y;
            velX = impulseForceX; velY = impulseForceY;
            active = true;
            collider = new Rectangle(x, y, 16, 16);
            this.type = type;
            switch(this.type) {
                case COPPER: damagePotential = 2; break;
                case SILVER: damagePotential = 4; break;
                case IRON: damagePotential = 5; break;
                case DIAMOND: damagePotential = 10; break;
            }
        }

        public void tick() {

            if(active) {

                checkCollision();

                int newX = x + Math.round(velX * 25);
                velY -= 0.5f;
                float newY = y + velY;

                if(World.getTile((int)Math.floor(newX) / Tile.TILE_SIZE,
                        (int)Math.floor(newY) / Tile.TILE_SIZE).isSolid()) {
                    active = false;

                    if(World.tileBreakLevel[(int)Math.floor(newX) / Tile.TILE_SIZE]
                            [World.h - ((int)Math.floor(newY) / Tile.TILE_SIZE) - 1] - 1 == 0) {
                        World.tiles[(int)Math.floor(newX) / Tile.TILE_SIZE]
                                [World.h - ((int)Math.floor(newY) / Tile.TILE_SIZE) - 1] = 0;
                    } else World.tileBreakLevel[(int)Math.floor(newX) / Tile.TILE_SIZE]
                            [World.h - ((int)Math.floor(newY) / Tile.TILE_SIZE) - 1]--;

                    World.particleHandler.startParticle("smoke", x, y);
                    Assets.dirtHit[MathUtils.random(0,2)].play(0.10f);
                    return;
                }
                x = newX; y = Math.round(newY);
                collider.x = x; collider.y = y;

                checkCollision();

            }
        }

        private void checkCollision() {
            for(Entity e : World.getEntityHandler().getEntities()) {
                if(e instanceof Enemy) if (collider.intersects(((Enemy)e).hitBox)) {
                    e.hurt(damagePotential);
                    World.particleHandler.startParticle("explosion", x, y);
                    Assets.stoneHit[MathUtils.random(0,2)].play(0.60f);
                    active = false;
                }
            }
        }

        public void render(Batch b) {
            b.draw(Assets.copperPellet, x, y - 8, 16, 16);
        }
    }

    public static class SimpleBlaster {
        public Item bullet;
        public short x, y, range, rate,
                angle, aimSpeed; // (x and y in tiles)
        public boolean isLoaded, facingRight;
        public final short AMMO_CAP = 99;
        public ArrayList<AmmoType> ammo;
        public ArrayList<Pellet> shotPellets;

        public SimpleBlaster(short x, short y, short range, short rate, short aimSpeed) {
            this.x = x; this.y = y;
            bullet = null;
            angle = 0;
            ammo = new ArrayList<>();
            shotPellets = new ArrayList<>();
            this.range = range; this.rate = rate; this.aimSpeed = aimSpeed;
            isLoaded = false;
            facingRight = true;
        }

        public SimpleBlaster(short x, short y, ArrayList<AmmoType> ammo, int angle, int face, short range, short rate, short aimSpeed) {
            this.x = x; this.y = y;
            bullet = null;
            this.angle = (short)angle;
            this.ammo = ammo;
            shotPellets = new ArrayList<>();
            this.range = range; this.rate = rate; this.aimSpeed = aimSpeed;
            isLoaded = false;
            facingRight = face == 0;
        }

        float timer = 0.0f;
        public void tick() {
            if(playerNear()) {
                aim();
                if(InputHandler.rightMouseButtonDown) loadFromPlayer();
            }

            for(Pellet p : shotPellets) p.tick();
            shotPellets.removeIf(p -> !p.active);

            if(InputHandler.fireRequest) {
                timer++;
                if(ammo.size() > 0) {
                    if(timer > rate) {
                        fire();
                        timer = 0;
                    }
                }
            } else timer = rate;
        }

        private boolean playerNear() {
            boolean near = false;
            int playerTileX = (int)World.getEntityHandler().getPlayer().getX() / Tile.TILE_SIZE;
            int playerTileY = (int)World.getEntityHandler().getPlayer().getY() / Tile.TILE_SIZE;
            if(Math.abs((float)playerTileX - x) <= 1 && Math.abs(playerTileY - (int)y) <= 1) near = true;
            return near;
        }

        public void aim() {
            if (InputHandler.angleUp) angle += (facingRight) ? 1 : -1;
            if (InputHandler.angleDown) angle += (facingRight) ? -1 : 1;
            if (InputHandler.flipLeft) {
                facingRight = false;
                InputHandler.flipLeft = false;
            }
            if (InputHandler.flipRight) {
                facingRight = true;
                InputHandler.flipRight = false;
            }
        }

        public void loadFromPlayer() {
            if(ammo.size() > AMMO_CAP) return;
            try {
                if (ToolSlot.currentItem.getItem() == Assets.copperPelletItem) {
                    ToolSlot.currentItem.itemCount--;
                    ammo.add(AmmoType.COPPER);
                    Assets.invClick.play(0.35f);
                }
            } catch (NullPointerException ignored) {}
        }

        public void fire() {
            if(ammo.size() <= 0) return;

            AmmoType aT = ammo.get(ammo.size() - 1);
            ammo.remove(ammo.size() - 1);
            Pellet pellet; int xxx, yyy; float imX, imY;
            if(facingRight) {
                radius = (xx + Tile.TILE_SIZE + 4) - (xx + 10);
                xxx = (int)Math.round(((xx + 10) + (Math.cos(Math.toRadians(angle)) * radius)));
                yyy = (int)Math.round(((yy + 12) + (Math.sin(Math.toRadians(angle)) * radius)));
                imX = (float)Math.cos(Math.toRadians(angle));
                imY = (float)Math.sin(Math.toRadians(angle));
            } else {
                radius = xx - (xx + 10);
                xxx = (int)Math.round(((xx + 10) + (Math.cos(Math.toRadians(angle)) * radius)));
                yyy = (int)Math.round(((yy + 12) + (Math.sin(Math.toRadians(angle)) * radius)));
                imX = -(float)Math.cos(Math.toRadians(angle));
                imY = -(float)Math.sin(Math.toRadians(angle));
            }
            pellet = new Pellet(AmmoType.COPPER, xxx, yyy, imX, imY * 30);
            shotPellets.add(pellet);
            World.particleHandler.startParticle("explosion", xxx, yyy);
            Assets.blasterFire[MathUtils.random(0,2)].play(0.20f);

            switch(aT) {
                case COPPER:
                case SILVER:
                case IRON:
                case DIAMOND:  break;
            }
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

        short xx, yy;
        public void render(Batch b) {
            xx = (short)(x * Tile.TILE_SIZE);
            yy = (short)(y * Tile.TILE_SIZE);
            if(facingRight)
                b.draw(Assets.simpleBlasterBodyRight, (int)xx, (int)yy + 6, 10, 4,
                        Tile.TILE_SIZE + 4, Tile.TILE_SIZE + 4, 1, 1, angle);
            else b.draw(Assets.simpleBlasterBodyLeft, (int)xx, (int)yy + 6, 10, 4,
                    Tile.TILE_SIZE + 4, Tile.TILE_SIZE + 4, 1, 1, angle);
            renderSights();
            Text.draw(b, Utils.toString(ammo.size()), xx + 10 -
                    (Utils.toString(ammo.size()).length() * 6 / 2), yy + 18, 6);
            for(Pellet p : shotPellets) p.render(b);
        }

        float radius; double xxx1, yyy1, xxx2, yyy2;
        void renderSights() {
            if(facingRight) {
                radius = (xx + Tile.TILE_SIZE + 4) - (xx + 10);
                xxx1 = Math.round(((xx + 10) + (Math.cos(Math.toRadians(angle)) * radius)));
                yyy1 = Math.round(((yy + 12) + (Math.sin(Math.toRadians(angle)) * radius)));
                xxx2 = Math.round((xx + 10) + (range * (Math.cos(Math.toRadians(angle)))));
                yyy2 = Math.round((yy + 12) + (range * (Math.sin(Math.toRadians(angle)))));
            } else {
                radius = xx - (xx + 10);
                xxx1 = Math.round(((xx + 10) + (Math.cos(Math.toRadians(angle)) * radius)));
                yyy1 = Math.round(((yy + 12) + (Math.sin(Math.toRadians(angle)) * radius)));
                xxx2 = Math.round((xx + 10) - (range * (Math.cos(Math.toRadians(angle)))));
                yyy2 = Math.round((yy + 12) - (range * (Math.sin(Math.toRadians(angle)))));
            }

            Game.geometryRequests.add(new Geometry.LineRequest((int)xxx1, (int)yyy1,
                    (int)xxx2, (int)yyy2));

        }

    }

}
