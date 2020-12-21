package dev.iwilkey.designa.item.creator;

import dev.iwilkey.designa.item.Item;

import java.util.ArrayList;

public enum Category {

    RESOURCES ("Resources"),
    TOOLS ("Tools"),
    UTILITIES ("Utilities"),
    TILES ("Tiles"),
    DEFENSE ("Defense"),
    MACHINES ("Machines"),
    MISC ("Misc");

    private final String name;
    private final ArrayList<Item> items;
    Category(String name) {

        this.name = name;
        this.items = new ArrayList<>();

        switch (name) {

            case "Resources":
                this.items.add(Item.GRAVEL);
                this.items.add(Item.CONCRETE);
                this.items.add(Item.REINFORCED_CONCRETE);
                this.items.add(Item.CONDENSED_SLAB);
                this.items.add(Item.STRONGSTONE);
                this.items.add(Item.REINFORCED_STRONGSTONE);

                this.items.add(Item.RECYCLED_COPPER);
                this.items.add(Item.BLUESTONE);
                this.items.add(Item.REINFORCED_BLUESTONE);
                this.items.add(Item.ROMAN_VITRIOL);

                this.items.add(Item.RECYCLED_SILVER);
                this.items.add(Item.COIN_SILVER);
                this.items.add(Item.STERLING_SILVER);
                this.items.add(Item.REINFORCED_STERLING_SILVER);
                this.items.add(Item.FINE_SILVER_RECIPE);
                this.items.add(Item.REINFORCED_FINE_SILVER);

                this.items.add(Item.RECYCLED_IRON);
                this.items.add(Item.CAST_IRON);
                this.items.add(Item.REINFORCED_CAST_IRON);
                this.items.add(Item.STEEL);
                this.items.add(Item.REINFORCED_STEEL);

                this.items.add(Item.GRAPHITE);
                this.items.add(Item.COMPRESSED_GRAPHITE);
                this.items.add(Item.WEAK_DIAMOND);
                this.items.add(Item.DIAMOND);

                break;

            case "Tools":
                this.items.add(Item.STONE_SICKLE);
                this.items.add(Item.COPPER_SICKLE);
                break;

            case "Utilities":
                break;

            case "Tiles":
                break;

            case "Defense":
                break;

            case "Machines":
                break;

            case "Misc":
                break;

        }

    }

    public String getName() { return name; }
    public ArrayList<Item> getItems() { return items; }

}
