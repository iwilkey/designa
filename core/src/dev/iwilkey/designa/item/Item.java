package dev.iwilkey.designa.item;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.tile.Tile;

public enum Item {

    DIRT (
            "Dirt",
            0,
            new ItemType.NonCreatableItem.PlaceableTile(Tile.DIRT),
            null,
            Assets.dirt
    ),

    GRASS (
            "Grass",
            1,
            new ItemType.NonCreatableItem.PlaceableTile(Tile.GRASS),
            null,
            Assets.grass
    );

    public static final int ITEM_WIDTH = 8, ITEM_HEIGHT = 8;

    private final String name;
    private final byte itemID;
    private final ItemType type;
    private final Recipe recipe;
    private TextureRegion texture;

    Item(String name, int itemID, ItemType type, Recipe recipe, TextureRegion texture) {
        this.name = name;
        this.itemID = (byte)itemID;
        this.type = type;
        this.recipe = recipe;
        this.texture = texture;
    }

    public void render(Batch b, int x, int y) {
        b.draw(texture, x, y, ITEM_WIDTH, ITEM_HEIGHT);
    }

    public static Tile getTileFromItem(Item i) {
        if(i.getType() instanceof ItemType.NonCreatableItem.PlaceableTile)
            return ((ItemType.NonCreatableItem.PlaceableTile)i.getType()).correspondingTile;
        return null;
    }

    public byte getID() { return itemID; }
    public ItemType getType() { return type; }
    public Recipe getRecipe() { return recipe; }
    public TextureRegion getTexture() { return texture; }

}
