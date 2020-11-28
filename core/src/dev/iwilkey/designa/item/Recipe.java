package dev.iwilkey.designa.item;

import java.util.HashMap;

public enum Recipe {

    DIRT ("Dirt"),
    GRASS ("Grass"),

    GRAVEL ("Gravel"),
    CONCRETE ("Concrete"),
    REINFORCED_CONCRETE ("Reinforced Concrete"),
    CONDENSED_SLAB ("Condensed Slab"),
    STRONGSTONE ("Strongstone"),
    REINFORCED_STRONGSTONE ("Reinforced Strongstone"),

    RECYCLED_COPPER ("Recycled Copper"),
    BLUESTONE ("Bluestone"),
    REINFORCED_BLUESTONE ("Reinforced Bluestone"),
    ROMAN_VITRIOL ("Roman Vitriol"),

    RECYCLED_SILVER ("Recycled Silver"),
    COIN_SILVER ("Coin Silver"),
    STERLING_SILVER ("Sterling Silver"),
    REINFORCED_STERLING_SILVER ("Reinforced Sterling Silver"),
    FINE_SILVER ("Fine Silver"),
    REINFORCED_FINE_SILVER ("Reinforced Fine Silver"),

    RECYCLED_IRON ("Recycled Iron"),
    CAST_IRON ("Cast Iron"),
    REINFORCED_CAST_IRON ("Reinforced Cast Iron"),
    STEEL ("Steel"),
    REINFORCED_STEEL ("Reinforced Steel"),

    GRAPHITE ("Graphite"),
    COMPRESSED_GRAPHITE ("Compressed Graphite"),
    WEAK_DIAMOND ("Weak Diamond"),
    DIAMOND ("Diamond"),
    REINFORCED_DIAMOND ("Reinforced Diamond"),

    STONE_SICKLE ("Stone Sickle"),
    COPPER_SICKLE ("Copper Sickle");

    private HashMap<String, Integer> recipe;

    Recipe(String name) {

        recipe = new HashMap<>();

        switch(name) {

            // Placeable Tiles
            case "Dirt":
                add("Dirt", 2);
                add("Grass", 32);
                add("Stone", 12);
                break;

            case "Grass":
                add("Grass", 12);
                add("Stone", 99);
                break;

            // Resources
            case "Gravel":
                add("Rock", 2);
                break;
            case "Concrete":
                add("Gravel", 2);
                break;
            case "Reinforced Concrete":
                add("Concrete", 2);
                break;
            case "Condensed Slab":
                add("Reinforced Concrete", 2);
                break;
            case "Strongstone":
                add("Condensed Slab", 2);
                break;
            case "Reinforced Strongstone":
                add("Strongstone", 2);
                break;

            case "Recycled Copper":
                add("Copper Scrap", 2);
                break;
            case "Bluestone":
                add("Recycled Copper", 2);
                break;
            case "Reinforced Bluestone":
                add("Bluestone", 2);
                break;
            case "Roman Vitriol":
                add("Reinforced Bluestone", 2);
                break;

            case "Recycled Silver":
                add("Silver Scrap", 2);
                break;
            case "Coin Silver":
                add("Recycled Silver", 2);
                break;
            case "Sterling Silver":
                add("Coin Silver", 2);
                break;
            case "Reinforced Sterling Silver":
                add("Sterling Silver", 2);
                break;
            case "Fine Silver":
                add("Reinforced Sterling Silver", 2);
                break;
            case "Reinforced Fine Silver":
                add("Reinforced Fine Silver", 2);
                break;

            case "Recycled Iron":
                add("Iron Scrap", 2);
                break;
            case "Cast Iron":
                add("Recycled Iron", 2);
                break;
            case "Reinforced Cast Iron":
                add("Cast Iron", 2);
                break;
            case "Steel":
                add("Reinforced Cast Iron", 2);
                break;
            case "Reinforced Steel":
                add("Steel", 2);
                break;

            case "Graphite":
                add("Carbon Sample", 2);
                break;
            case "Compressed Graphite":
                add("Graphite", 2);
                break;
            case "Weak Diamond":
                add("Compressed Graphite", 2);
                break;
            case "Diamond":
                add("Weak Diamond", 2);
                break;

            case "Stone Sickle":
                add("Rock", 3);
                add("Gravel", 2);
                break;
            case "Copper Sickle":
                add("Stone Sickle", 1);
                add("Copper Scrap", 3);
                add("Recycled Copper", 2);
                break;

        }

    }

    public void add(String name, int amount) {
        this.recipe.put(name, amount);
    }
    public HashMap<String, Integer> getRecipe() { return recipe; }

}
