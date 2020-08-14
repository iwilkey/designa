package com.iwilkey.designa.items;

import com.iwilkey.designa.utils.Utils;

import java.util.HashMap;

public abstract class ItemRecipe {

    // Recipes
    public static class SimpleDrillRecipe extends ItemRecipe {
        public SimpleDrillRecipe(Item item) { super(item); }
        @Override
        public void create() {
            add(Item.oakWoodItem, 64);
            add(Item.stoneItem, 32);
        }
    }

    public static class TorchRecipe extends  ItemRecipe {
        public TorchRecipe(Item item) { super(item); }
        @Override
        public void create() {
            add(Item.oakWoodItem, 4);
        }
    }

    // Statics (Shell)
    public static ItemRecipe SIMPLE_DRILL_RECIPE = new SimpleDrillRecipe(Item.simpleDrill);
    public static ItemRecipe TORCH_RECIPE = new TorchRecipe(Item.torchItem);

    // Class
    public Item item;
    protected HashMap<Item, String> recipe = new HashMap<Item, String>();
    public ItemRecipe(Item item) {
        this.item = item;
        create();
    }
    public abstract void create();
    public void add(Item i, int amount) {
        String a = Utils.toString(amount);
        this.recipe.put(i, a);
    }
    public void setRecipe(HashMap<Item, String> recipe) {
        this.recipe = recipe;
    }
    public HashMap<Item, String> getRecipe() { return this.recipe; }

}
