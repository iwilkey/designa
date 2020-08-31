package com.iwilkey.designa.items;

import com.iwilkey.designa.entities.creature.passive.Player;
import com.iwilkey.designa.inventory.crate.Crate;

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
        public Resource(String baseResource, String name) {
            super("Resource");
            this.name = name;
            this.baseResource = baseResource;
        }
        public String getName() { return name; }
        public String getBaseResource() { return baseResource; }
        public ItemRecipe getItemRecipe() { return itemRecipe; }
        public void setItemRecipe(ItemRecipe r) { this.itemRecipe = r; }
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
            protected ItemRecipe itemRecipe;
            public CreatableTile(int ID) {
                super(ID);
            }
            public ItemRecipe getRecipe() { return itemRecipe; }
            public void setItemRecipe(ItemRecipe r) { this.itemRecipe = r; }

            public static class Storage extends CreatableTile {
                public Storage(int ID) { super(ID); }
            }

            public static class MechanicalDrill extends CreatableTile {
                public MechanicalDrill(int ID) { super(ID); }
            }

            public static class Pipe extends CreatableTile {
                public Pipe(int ID) { super(ID); }
            }

        }
    }

    // CreatableItem
    public static class CreatableItem extends ItemType {
        protected ItemRecipe itemRecipe;
        public CreatableItem(String type) {
            super(type);
        }
        public ItemRecipe getRecipe() { return itemRecipe; }
        public void setItemRecipe(ItemRecipe r) { this.itemRecipe = r; }

        // Tool
        public static class Tool extends CreatableItem {
            protected int level;
            public Tool(int level) {
                super("Tool");
                this.level = level;
            }
            public int getLevel() { return level; }

            // Drill
            public static class Drill extends Tool {
                protected int strength;
                protected String name;
                public Drill(String name, int strength, int level) {
                    super(level);
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
            public Weapon(String name) {
                super("Weapon");
            }
            public String getName() { return name; }

            // Gun
            public static abstract class Gun extends Weapon {
                protected int ammo;
                protected int strength;
                public Gun(String name, int strength) {
                    super(name);
                    reload();
                }
                public abstract void fire();
                public abstract void reload();
                public int getAmmo() { return ammo; }
            }
        }
    }
}
