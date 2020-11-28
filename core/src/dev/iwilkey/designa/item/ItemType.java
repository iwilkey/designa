package dev.iwilkey.designa.item;

import dev.iwilkey.designa.tile.Tile;

public abstract class ItemType {

    public abstract static class NonCreatableItem extends ItemType {
        public static class PlaceableTile extends ItemType {
            public final Tile correspondingTile;
            PlaceableTile(Tile correspondingTile) { this.correspondingTile = correspondingTile; }
        }
    }

    public abstract static class CreatableItem extends ItemType {
        public static class Resource extends CreatableItem {}
        public abstract static class Tool extends CreatableItem {
            public static class Sickle extends Tool{}
        }
    }

}
