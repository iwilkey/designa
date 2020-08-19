package com.iwilkey.designa.items;

public class ItemType {

    protected String type;
    public ItemType(String type) {
        this.type = type;
    }
    public String getType() { return this.type; }

    // Resource
    public static class Resource extends ItemType {
        protected String baseResource;
        protected String name;
        protected ItemRecipe itemRecipe;
        public Resource(String baseResource, String name, ItemRecipe ir) {
            super("Resource");
            this.name = name;
            this.baseResource = baseResource;
            this.itemRecipe = ir;
        }
        public String getName() { return name; }
        public String getBaseResource() { return baseResource; }
        public ItemRecipe getItemRecipe() { return itemRecipe; }
    }

    // PlaceableBlock
    public static class PlaceableBlock extends ItemType {
        private final int tileID;
        public PlaceableBlock(int ID) {
            super("PlaceableBlock");
            this.tileID = ID;
        }
        public int getTileID() { return tileID; }

        // CreatableTile
        public static class CreatableTile extends PlaceableBlock {
            protected ItemRecipe recipe;
            public CreatableTile(int ID, ItemRecipe ir) {
                super(ID);
                this.recipe = ir;
            }
            public ItemRecipe getRecipe() { return recipe; }
        }
    }

    // CreatableItem
    public static class CreatableItem extends ItemType {
        protected ItemRecipe recipe;
        public CreatableItem(String type, ItemRecipe ir) {
            super(type);
            this.recipe = ir;
        }
        public ItemRecipe getRecipe() { return recipe; }

        // Tool
        public static class Tool extends CreatableItem {
            protected int level;
            public Tool(int level, ItemRecipe recipe) {
                super("Tool", recipe);
                this.level = level;
            }
            public int getLevel() { return level; }

            // Drill
            public static class Drill extends Tool {
                protected int strength;
                protected String name;
                public Drill(String name, int strength, int level, ItemRecipe recipe) {
                    super(level, recipe);
                    this.strength = strength;
                    this.name = name;
                }
                public int getStrength() { return strength; }
                public String getName() { return name; }
            }
        }

        // Weapon
        public static class Weapon extends CreatableItem {
            protected String name;
            public Weapon(String name, ItemRecipe ir) {
                super("Weapon", ir);
            }
            public String getName() { return name; }

            // Gun
            public static abstract class Gun extends Weapon {
                protected int ammo;
                protected int strength;
                public Gun(String name, int strength, ItemRecipe ir) {
                    super(name, ir);
                    reload();
                }
                public abstract void fire();
                public abstract void reload();
                public int getAmmo() { return ammo; }
            }
        }
    }
}
