package com.iwilkey.designa.items;

import com.iwilkey.designa.utils.Utils;

import java.util.HashMap;

public abstract class ItemRecipe {

    // Recipes

    // Resources
        // Wood
        public static class SticksRecipe extends ItemRecipe {
            public SticksRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.barkResource, 2);
            }
        }
        public static class PlywoodRecipe extends ItemRecipe {
            public PlywoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.stickResource, 2);
            }
        }
        public static class HardwoodRecipe extends ItemRecipe {
            public HardwoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.plywoodResource, 2);
            }
        }
        public static class ReinforcedHardwoodRecipe extends ItemRecipe {
            public ReinforcedHardwoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.hardwoodResource, 2);
            }
        }
        public static class StrongwoodRecipe extends ItemRecipe {
            public StrongwoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.reinforcedHardwoodResource, 2);
            }
        }
        public static class ReinforcedStrongwoodRecipe extends ItemRecipe {
            public ReinforcedStrongwoodRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.strongwoodResource, 2);
            }
        }

        // Stone
        public static class GravelRecipe extends ItemRecipe {
            public GravelRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.rockResource, 2);
            }
        }
        public static class ConcreteRecipe extends ItemRecipe {
            public ConcreteRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.gravelResource, 2);
            }
        }
        public static class ReinforcedConcreteRecipe extends ItemRecipe {
            public ReinforcedConcreteRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.concreteResource, 2);
            }
        }
        public static class CondensedSlabRecipe extends ItemRecipe {
            public CondensedSlabRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.reinforcedConcreteResource, 2);
            }
        }
        public static class StrongstoneRecipe extends ItemRecipe {
            public StrongstoneRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.condensedSlabResource, 2);
            }
        }
        public static class ReinforcedStrongstoneRecipe extends ItemRecipe {
            public ReinforcedStrongstoneRecipe(Item item) { super(item); }
            @Override
            public void create() {
                add(Item.strongstoneResource, 2);
            }
        }
    // CreatableItems
        // Tools
            // Drills
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
    // Resources
        // Wood
        public static ItemRecipe STICK_RESOURCE_RECIPE = new SticksRecipe(Item.stickResource);
        public static ItemRecipe PLYWOOD_RESOURCE_RECIPE = new PlywoodRecipe(Item.plywoodResource);
        public static ItemRecipe HARDWOOD_RESOURCE_RECIPE = new HardwoodRecipe(Item.hardwoodResource);
        public static ItemRecipe REINFORCED_HARDWOOD_RECIPE = new ReinforcedHardwoodRecipe(Item.reinforcedHardwoodResource);
        public static ItemRecipe STRONGWOOD_RECIPE = new StrongwoodRecipe(Item.strongwoodResource);
        public static ItemRecipe REINFORCED_STRONGWOOD_RECIPE = new ReinforcedStrongwoodRecipe(Item.reinforcedStrongwoodResource);

        // Stone
        public static ItemRecipe GRAVEL_RESOURCE_RECIPE = new GravelRecipe(Item.gravelResource);
        public static ItemRecipe CONCRETE_RESOURCE_RECIPE = new ConcreteRecipe(Item.concreteResource);
        public static ItemRecipe REINFORCED_CONCRETE_RESOURCE_RECIPE = new ReinforcedConcreteRecipe(Item.reinforcedConcreteResource);
        public static ItemRecipe CONDENSED_SLAB_RESOURCE_RECIPE = new CondensedSlabRecipe(Item.condensedSlabResource);
        public static ItemRecipe STRONGSTONE_RESOURCE_RECIPE = new StrongstoneRecipe(Item.strongstoneResource);
        public static ItemRecipe REINFORCED_STRONGSTONE_RESOURCE_RECIPE = new ReinforcedStrongstoneRecipe(Item.reinforcedStrongstoneResource);



    // CreatableItems
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
