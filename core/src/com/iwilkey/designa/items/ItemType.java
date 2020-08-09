package com.iwilkey.designa.items;

public class ItemType {

    protected String type;

    public ItemType(String type) {
        this.type = type;
    }

    public String getType() { return this.type; }

    public static class PlaceableBlock extends ItemType {

        private int tileID;

        public PlaceableBlock(int ID) {
            super("PlaceableBlock");
            this.tileID = ID;
        }

        public int getTileID() { return tileID; }

    }

    public static class Tool extends ItemType {

        protected int level;

        public Tool(int level) {
            super("Tool");
            this.level = level;
        }

        public int getLevel() { return level; }

    }

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
