package dev.iwilkey.designa.item;

import java.util.HashMap;

public class Recipe {

    public interface Mixer {
        void create(Recipe recipe);
    }

    public HashMap<Item, Integer> recipe;

    public Recipe(Mixer mixer) {
        recipe = new HashMap<>();
        mixer.create(this);
    }

    public void add(Item item, int amount) {
        this.recipe.put(item, amount);
    }

}
