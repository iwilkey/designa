package dev.iwilkey.designa.item;

import com.badlogic.gdx.graphics.g2d.Batch;
import dev.iwilkey.designa.entity.creature.passive.Player;
import dev.iwilkey.designa.gfx.Camera;
import dev.iwilkey.designa.tile.Tile;
import dev.iwilkey.designa.world.World;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveItemHandler {

    public final float GRAVITY = -6.5f;
    public HashMap<Item, List<Rectangle>> activeItems;
    public ArrayList<Float> timeInAir;

    World world;

    public ActiveItemHandler(World world) {
        this.world = world;
        activeItems = new HashMap<>();
        timeInAir = new ArrayList<>();
    }

    List<Rectangle> rectangles;
    public void spawn(Item i, int x, int y) {

        if(activeItems.containsKey(i)) {
            rectangles = activeItems.get(i);
            rectangles.add(new Rectangle(x, y, Item.ITEM_WIDTH, Item.ITEM_HEIGHT));
        } else {
            rectangles = new ArrayList<>();
        }

        activeItems.put(i, rectangles);
        timeInAir.add(0.0f);
    }

    private void pickup(Item item, Player player) {
        // Add to player inventory
        player.inventory.add(item);
    }

    int c1, c2;
    public void tick() {
        c1 = 0;
        for(Map.Entry<Item, List<Rectangle>> i : activeItems.entrySet()) {
            c2 = 0;
            for(Rectangle rect : i.getValue()) {
                if(world.player.collider.intersects(rect)) {
                    i.getValue().remove(rect);
                    timeInAir.remove(c1);
                    pickup(i.getKey(), world.player);
                    break;
                }
                i.getValue().set(c2, moveY(rect, c1));
                c1++; c2++;
            }
        }
    }

    int ty;
    public Rectangle moveY(Rectangle collider, int index) {
        Rectangle newPosition = new Rectangle(collider.x, collider.y, collider.width, collider.height);

        ty = (int)(collider.y + GRAVITY);
        if(ty <= -6) {
            timeInAir.set(index, 0.0f);
            return newPosition;
        }

        if(!collisionWithTile((collider.x + 4) / Tile.TILE_SIZE, ty / Tile.TILE_SIZE) &&
                !collisionWithTile((collider.x + collider.width + 4) / Tile.TILE_SIZE, ty / Tile.TILE_SIZE) &&
                !checkItemAt(collider.x, ty) && !checkItemAt(collider.x + collider.width, ty)) {
            timeInAir.set(index, timeInAir.get(index) + 0.02f);
            newPosition.y += GRAVITY * (timeInAir.get(index));
        } else if (collisionWithTile((collider.x + 4) / Tile.TILE_SIZE, ty / Tile.TILE_SIZE) &&
                collisionWithTile((collider.x + collider.width + 4) / Tile.TILE_SIZE, ty / Tile.TILE_SIZE)) {
            timeInAir.set(index, 0.0f);
            newPosition.y = (((int)((newPosition.y + 8) / Tile.TILE_SIZE)) * Tile.TILE_SIZE);
        } else timeInAir.set(index, 0.0f);

        return newPosition;
    }

    public boolean collisionWithTile(int x, int y) {
        return world.getTile(x, y).isSolid();
    }

    private boolean checkItemAt(int x, int y) {
        y += 6;
        for(Map.Entry<Item, List<Rectangle>> i : activeItems.entrySet()) {
            for(Rectangle rect : i.getValue()) {
                if(rect.contains(new Point(x, y))) return true;
            }
        }
        return false;
    }

    private boolean itemInView(Rectangle rect) {
        return (rect.x > xStart * Tile.TILE_SIZE && rect.x < xEnd * Tile.TILE_SIZE &&
                rect.y > yStart * Tile.TILE_SIZE && rect.y < yEnd * Tile.TILE_SIZE);
    }

    int xStart, xEnd, yStart, yEnd;
    public void render(Batch b) {
        xStart = (int) Math.max(0, ((-Camera.position.x / Camera.scale.x) / Tile.TILE_SIZE) - 2);
        xEnd = (int) Math.min(world.WIDTH, ((((-Camera.position.x + Camera.GW) / Camera.scale.x) / Tile.TILE_SIZE)) + 2);
        yStart = (int) Math.max(0, ((-Camera.position.y / Camera.scale.y) / Tile.TILE_SIZE) - 2);
        yEnd = (int) Math.min(world.HEIGHT, ((((-Camera.position.y + Camera.GH) / Camera.scale.y) / Tile.TILE_SIZE)) + 2);
        for(Map.Entry<Item, List<Rectangle>> i : activeItems.entrySet()) {
            Item item = i.getKey();
            for(Rectangle rect : i.getValue()) {
                if(!itemInView(rect)) continue;
                item.render(b, rect.x, rect.y);
            }
        }
    }
}
