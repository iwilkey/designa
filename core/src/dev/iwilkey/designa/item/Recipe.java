package dev.iwilkey.designa.item;

import java.util.HashMap;

public enum Recipe {

    DIRT ("Dirt"),
    GRASS ("Grass");

    private HashMap<String, Integer> recipe;

    Recipe(String name) {

        recipe = new HashMap<>();

        switch(name) {

            case "Dirt":
                add("Dirt", 2);
                add("Grass", 32);
                add("Stone", 12);
                break;

            case "Grass":
                add("Grass", 12);
                add("Stone", 99);
                break;

        }

    }

    public void add(String name, int amount) {
        this.recipe.put(name, amount);
    }
    public HashMap<String, Integer> getRecipe() { return recipe; }

}
