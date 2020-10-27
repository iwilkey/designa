package dev.iwilkey.designa.item;

public abstract class ItemType {

    public abstract static class NonCreatableItem extends ItemType {
        public static class PlaceableTile extends ItemType {
            public final byte correspondingTileID;
            PlaceableTile(int correspondingTileID) { this.correspondingTileID = (byte)correspondingTileID; }
        }
    }

    public abstract static class CreatableItem extends ItemType {

    }

}
