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

}
