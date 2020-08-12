package com.iwilkey.designa.items;

import com.iwilkey.designa.utils.Utils;

import java.util.HashMap;

public abstract class ItemRecipe {

    // Recipes
    public static class SimpleDrillRecipe extends ItemRecipe {
        public SimpleDrillRecipe(Item item) { super(item); }
        @Override
        public void init() {
            add(Item.Items.DIRT.ID(), 2); // 2 dirt.
            add(Item.Items.STONE.ID(), 2); // 2 stone.
        }
    }

    // Statics (Shell)
    public static ItemRecipe SIMPLE_DRILL_RECIPE = new SimpleDrillRecipe(Item.simpleDrill);

    // Class
    public Item item;
    protected HashMap<String, String> recipe = new HashMap<String, String>();
    public ItemRecipe(Item item) {
        this.item = item;
        init();
    }
    public abstract void init();
    public void add(int key, int amount) {
        String k = Utils.toString(key);
        String a = Utils.toString(amount);
        this.recipe.put(k, a);
    }
    public void setRecipe(HashMap<String, String> recipe) {
        this.recipe = recipe;
    }
    public HashMap<String, String> getRecipe() { return this.recipe; }

}
