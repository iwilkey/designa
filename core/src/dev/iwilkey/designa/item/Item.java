package dev.iwilkey.designa.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.iwilkey.designa.assets.Assets;
import dev.iwilkey.designa.tile.Tile;

public enum Item {

    // Placeable tiles
    DIRT (
            "Dirt",
            0,
            new ItemType.PlaceableTile(Tile.DIRT),
            Recipe.DIRT,
            Assets.dirt
    ),

    GRASS (
            "Grass",
            1,
            new ItemType.PlaceableTile(Tile.GRASS),
            Recipe.GRASS,
            Assets.grass
    ),

    STONE (
            "Stone",
            2,
            new ItemType.PlaceableTile(Tile.STONE),
            null,
            Assets.stone
    ),

    // Resources

    // Stone
    ROCK (
        "Rock",
        3,
        new ItemType.CreatableItem.Resource(),
        null,
        Assets.rock[0]
    ),

    GRAVEL (
        "Gravel",
        4,
        new ItemType.CreatableItem.Resource(),
        Recipe.GRAVEL,
        Assets.rock[1]
    ),

    CONCRETE (
            "Concrete",
            5,
            new ItemType.CreatableItem.Resource(),
            Recipe.CONCRETE,
            Assets.rock[2]
    ),

    REINFORCED_CONCRETE (
            "Reinforced Concrete",
            6,
            new ItemType.CreatableItem.Resource(),
            Recipe.REINFORCED_CONCRETE,
            Assets.rock[3]
    ),

    CONDENSED_SLAB (
            "Condensed Slab",
            7,
            new ItemType.CreatableItem.Resource(),
            Recipe.CONDENSED_SLAB,
            Assets.rock[4]
    ),

    STRONGSTONE (
            "Strongstone",
            8,
            new ItemType.CreatableItem.Resource(),
            Recipe.STRONGSTONE,
            Assets.rock[5]
    ),

    REINFORCED_STRONGSTONE (
            "Reinforced Strongstone",
            9,
            new ItemType.CreatableItem.Resource(),
            Recipe.REINFORCED_STRONGSTONE,
            Assets.rock[6]
    ),

    // Copper
    COPPER_SCRAP (
            "Copper Scrap",
            10,
            new ItemType.CreatableItem.Resource(),
            null,
            Assets.copper[0]
    ),

    RECYCLED_COPPER (
        "Recycled Copper",
        11,
        new ItemType.CreatableItem.Resource(),
        Recipe.RECYCLED_COPPER,
        Assets.copper[1]
    ),

    BLUESTONE (
        "Bluestone",
        12,
        new ItemType.CreatableItem.Resource(),
        Recipe.BLUESTONE,
        Assets.copper[2]
    ),

    REINFORCED_BLUESTONE (
        "Reinforced Bluestone",
        13,
        new ItemType.CreatableItem.Resource(),
        Recipe.REINFORCED_BLUESTONE,
        Assets.copper[3]
    ),

    ROMAN_VITRIOL (
    		
        "Roman Vitriol",
        14,
        new ItemType.CreatableItem.Resource(),
        Recipe.ROMAN_VITRIOL,
        Assets.copper[4]
    ),

    // Silver
    SILVER_SCRAP (
      "Silver Scrap",
      15,
      new ItemType.CreatableItem.Resource(),
      null,
      Assets.silver[0]
    ),

    RECYCLED_SILVER (
      "Recycled Silver",
      16,
      new ItemType.CreatableItem.Resource(),
      Recipe.RECYCLED_SILVER,
      Assets.silver[1]
    ),

    COIN_SILVER (
            "Coin Silver",
            17,
            new ItemType.CreatableItem.Resource(),
            Recipe.COIN_SILVER,
            Assets.silver[2]
    ),

    STERLING_SILVER (
        "Sterling Silver",
        18,
        new ItemType.CreatableItem.Resource(),
        Recipe.STERLING_SILVER,
        Assets.silver[3]
    ),

    REINFORCED_STERLING_SILVER (
        "Reinforced Sterling Silver",
        19,
        new ItemType.CreatableItem.Resource(),
        Recipe.REINFORCED_STERLING_SILVER,
        Assets.silver[4]
    ),

    FINE_SILVER_RECIPE (
            "Fine Silver",
            20,
            new ItemType.CreatableItem.Resource(),
            Recipe.FINE_SILVER,
            Assets.silver[5]
    ),

    REINFORCED_FINE_SILVER (
        "Reinforced Fine Silver",
        21,
        new ItemType.CreatableItem.Resource(),
        Recipe.REINFORCED_FINE_SILVER,
        Assets.silver[6]
    ),

    // Iron
    IRON_SCRAP (
            "Iron Scrap",
            22,
            new ItemType.CreatableItem.Resource(),
            null,
            Assets.iron[0]
    ),

    RECYCLED_IRON (
            "Recycled Iron",
            23,
            new ItemType.CreatableItem.Resource(),
            Recipe.RECYCLED_IRON,
            Assets.iron[1]
    ),

    CAST_IRON (
            "Cast Iron",
            24,
            new ItemType.CreatableItem.Resource(),
            Recipe.CAST_IRON,
            Assets.iron[2]
    ),

    REINFORCED_CAST_IRON (
            "Reinforced Cast Iron",
            25,
            new ItemType.CreatableItem.Resource(),
            Recipe.REINFORCED_CAST_IRON,
            Assets.iron[3]
    ),

    STEEL (
            "Steel",
            26,
            new ItemType.CreatableItem.Resource(),
            Recipe.STEEL,
            Assets.iron[4]
    ),

    REINFORCED_STEEL (
            "Reinforced Steel",
            27,
            new ItemType.CreatableItem.Resource(),
            Recipe.REINFORCED_STEEL,
            Assets.iron[5]
    ),

    // Carbon
    CARBON_SAMPLE (
            "Carbon Sample",
            28,
            new ItemType.CreatableItem.Resource(),
            null,
            Assets.carbon[0]
    ),

    GRAPHITE (
            "Graphite",
            29,
            new ItemType.CreatableItem.Resource(),
            Recipe.GRAPHITE,
            Assets.carbon[1]
    ),

    COMPRESSED_GRAPHITE (
        "Compressed Graphite",
        30,
        new ItemType.CreatableItem.Resource(),
        Recipe.COMPRESSED_GRAPHITE,
        Assets.carbon[2]
    ),

    WEAK_DIAMOND (
            "Weak Diamond",
            31,
            new ItemType.CreatableItem.Resource(),
            Recipe.WEAK_DIAMOND,
            Assets.carbon[3]
    ),

    DIAMOND (
            "Diamond",
            32,
            new ItemType.CreatableItem.Resource(),
            Recipe.DIAMOND,
            Assets.carbon[4]
    ),

    // Tools
    STONE_SICKLE (
            "Stone Sickle",
            33,
            new ItemType.CreatableItem.Tool.Sickle(33, 50, 4, 10),
            Recipe.STONE_SICKLE,
            Assets.stoneSickle
    ),

    COPPER_SICKLE (
            "Copper Sickle",
            34,
            new ItemType.CreatableItem.Tool.Sickle(34, 100, 8, 25),
            Recipe.COPPER_SICKLE,
            Assets.copperSickle
    ),
    
    SILVER_SICKLE (
    		"Silver Sickle",
    		35,
    		new ItemType.CreatableItem.Tool.Sickle(35, 150, 15, 40),
    		Recipe.SILVER_SICKLE,
    		Assets.silverSickle
    ),
    
    IRON_SICKLE (
    		"Iron Sickle",
    		36,
    		new ItemType.CreatableItem.Tool.Sickle(36, 200, 24, 60),
    		Recipe.IRON_SICKLE,
    		Assets.ironSickle
    ),
    
    DIAMOND_SICKLE (
    		"Diamond Sickle",
    		37,
    		new ItemType.CreatableItem.Tool.Sickle(37, 250, 40, 70),
    		Recipe.DIAMOND_SICKLE,
    		Assets.diamondSickle
    ),

    STONE_CRATE (
            "Stone Crate",
            38,
            new ItemType.PlaceableTile.Crate(Tile.STONE_CRATE, 5),
            Recipe.STONE_CRATE,
            Assets.stoneCrate
    ),

    COPPER_CRATE (
        "Copper Crate",
        39,
        new ItemType.PlaceableTile.Crate(Tile.COPPER_CRATE, 10),
        Recipe.COPPER_CRATE,
        Assets.copperCrate
    ),

    SILVER_CRATE (
        "Silver Crate",
        40,
        new ItemType.PlaceableTile.Crate(Tile.SILVER_CRATE, 15),
        Recipe.SILVER_CRATE,
        Assets.silverCrate
    ),

    IRON_CRATE (
            "Iron Crate",
            41,
            new ItemType.PlaceableTile.Crate(Tile.IRON_CRATE, 20),
            Recipe.IRON_CRATE,
            Assets.ironCrate
    ),

    DIAMOND_CRATE (
            "Diamond Crate",
            41,
            new ItemType.PlaceableTile.Crate(Tile.DIAMOND_CRATE, 30),
            Recipe.DIAMOND_CRATE,
            Assets.diamondCrate
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

    Item(String name, int itemID, ItemType type, Recipe recipe, Texture texture) {
        this.name = name;
        this.itemID = (byte)itemID;
        this.type = type;
        this.recipe = recipe;
        this.texture = new TextureRegion(texture, texture.getWidth(), texture.getHeight());
    }

    public void render(Batch b, int x, int y) {
        b.draw(texture, x, y, ITEM_WIDTH, ITEM_HEIGHT);
    }

    // Converters
    public static Tile getTileFromItem(Item i) {
        if(i.getType() instanceof ItemType.NonCreatableItem.PlaceableTile)
            return ((ItemType.NonCreatableItem.PlaceableTile)i.getType()).correspondingTile;
        return null;
    }

    public static Item getItemFromString(String name) {
        for(Item i : values())
            if(i.getName().equals(name)) return i;
        return null;
    }
    
    public static Item getItemFromID(int id) {
    	for(Item i : Item.values()) 
    		if(i.itemID == (byte)id) return i;
    	return null;
    }

    public byte getID() { return itemID; }
    public String getName() { return name; }
    public ItemType getType() { return type; }
    public Recipe getRecipe() { return recipe; }
    public TextureRegion getTexture() { return texture; }

}
