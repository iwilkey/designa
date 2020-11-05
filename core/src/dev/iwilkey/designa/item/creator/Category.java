package dev.iwilkey.designa.item.creator;

import dev.iwilkey.designa.item.Item;

import java.util.ArrayList;

public enum Category {

    RESOURCES ("Resources"),
    TOOLS ("Tools"),
    UTILITIES ("Utilities"),
    TILES ("Tiles"),
    DEFENSE ("Defense"),
    MACHINES ("Machines"),
    MISC ("Misc");

    private final String name;
    private final ArrayList<Item> items;
    Category(String name) {

        this.name = name;
        this.items = new ArrayList<>();

        switch (name) {

            case "Resources":
                break;

            case "Tools":
                break;

            case "Utilities":
                break;

            case "Tiles":
                this.items.add(Item.DIRT);
                this.items.add(Item.GRASS);
                break;

            case "Defense":
                break;

            case "Machines":
                break;

            case "Misc":
                break;

        }

    }

    public String getName() { return name; }
    public ArrayList<Item> getItems() { return items; }

}
