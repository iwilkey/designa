package com.iwilkey.designa.inventory.resource;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.MathUtils;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.InventorySlot;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.utils.Utils;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerticalTree {

    public static class Node {
        private String label;
        private int resource;
        private int level;
        private boolean isAvailable;
        private boolean canCreate = false;

        public boolean isSelected;
        public int x, y, w, h;
        public Rectangle collider;

        public Item item;

        public Node(String label, int level, Item item, int resource) {
            this.label = label;
            this.level = level;
            this.item = item;
            this.resource = resource;
            isAvailable = false;
            isSelected = false;
            w = label.length() * 14;
            h = 24;

            collider = new Rectangle(x, y, w,h);
        }

        public void setLabel(String label) { this.label = label; }
        public void setLevel(int lvl) { this.level = lvl; }
        public void setAvailable(boolean a) { this.isAvailable = a; }
        public int getLevel() { return level; }
        public String getLabel() { return label; }
        public boolean isAvailable() { return isAvailable; }

        public void updateCollider(int x, int y) {
            int dif = Math.abs(label.length() * 9 - w);
            collider.x = x - (dif / 2) - 2; collider.y = y - ((h / 2) - (h / 5));
        }
    }

    private Inventory inventory;
    private String name;
    public ArrayList<Node> nodes;
    private TextureRegion nodeConnector;
    private Rectangle createCollider;

    public VerticalTree(String name, Inventory inventory) {
        this.name = name;
        this.nodes = new ArrayList<>();
        this.inventory = inventory;
        nodeConnector = Assets.nodeConnector;

        createCollider = new Rectangle(Inventory.BLUEPRINT_X - 344, Inventory.BLUEPRINT_Y - 360 - 116, 82, 42);;
    }

    public void tick() {
        input();
    }

    private void input() {
        if(InputHandler.leftMouseButtonDown) {
            Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
            for(Node node : nodes) {
                if(rect.intersects(node.collider)) {
                    clearSelected();
                    node.isSelected = true;
                    checkResources(node);
                    Assets.invClick.play(0.35f);
                }

                if(rect.intersects(createCollider) && node.canCreate && node.isSelected && (node.resource == ResourceTree.currentResource)) {
                    create(node);
                    Assets.createItem[MathUtils.random(0, 2)].play(0.35f);
                    for(Node n : nodes) checkResources(n);
                    break;
                }
            }
        }
    }

    private void clearSelected() {
        for(Node node : nodes) node.isSelected = false;
    }

    public void renderUnavailable(Batch b, int x, int y, int w, int h) {
        for(Node node : nodes) {
            if(node.isSelected) {
                b.draw(Assets.selector, node.collider.x, node.collider.y, node.collider.width, node.collider.height);
                renderRecipe(b, node, x, y, w, h);
            }

            if(!node.isAvailable()) {
                int yy = y + (node.getLevel() * 44);
                int xx = x + Math.abs(((node.getLabel().length() * 8) / 2) - (w / 2));
                checkNodeCords(node, xx, yy);
                Text.draw(b, node.getLabel(), xx, yy, 8);
                if (node.getLevel() + 1 < nodes.size()) b.draw(nodeConnector, x + (w / 2f), yy + 12, 8, 32);
            }
        }
    }

    public void renderAvailable(Batch b, int x, int y, int w, int h) {
        for(Node node : nodes) {
            if(node.isSelected) {
                b.draw(Assets.selector, node.collider.x, node.collider.y, node.collider.width, node.collider.height);
                renderRecipe(b, node, x, y, w, h);
            }

            if(node.isAvailable()) {
                int yy = y + (node.getLevel() * 44);
                int xx = x + Math.abs(((node.getLabel().length() * 8) / 2) - (w / 2));
                checkNodeCords(node, xx, yy);
                Text.draw(b, node.getLabel(), xx, yy, 8);
                if(node.getLevel() + 1 < nodes.size()) b.draw(nodeConnector, x + (w / 2f), yy + 12, 8, 32);
            }

            if(node.canCreate && node.isSelected) {
                if(node.getLevel() > 0) {
                    b.draw(Assets.inventorySlot, createCollider.x, createCollider.y, createCollider.width, createCollider.height);
                    Text.draw(b, "Create", createCollider.x + 6, createCollider.y + 14, 11);
                }
            }
        }
    }

    private void renderRecipe(Batch b, Node node, int x, int y, int w, int h) {
        Text.draw(b, "Resources Required", x + 100, y - 40, 8);
        ItemRecipe r = ((ItemType.Resource)(node.item.getItemType())).getItemRecipe();

        if(r == null) {
            if(node.item == Assets.barkResource) {
                Text.draw(b, "Find bark by chopping at trees!", x + 100
                        - (("Find bark by chopping at trees!".length() - "Resources Required".length()) * 8 / 2), y - 60, 8);
                return;
            }
            else if (node.item == Assets.rockResource) {
                Text.draw(b, "Find rocks underground!", x + 100
                        - (("Find rocks underground!".length() - "Resources Required".length()) * 8 / 2), y - 60, 8);
                return;
            }
            else if (node.item == Assets.copperScrapResource) {
                Text.draw(b, "Find copper scrap by mining underground!", x + 100
                        - (("Find copper scrap by mining underground!".length() - "Resources Required".length()) * 8 / 2), y - 60, 8);
                return;
            }
            else if (node.item == Assets.silverScrapResource) {
                Text.draw(b, "Find silver scrap by mining underground!", x + 100
                        - (("Find silver scrap by mining underground!".length() - "Resources Required".length()) * 8 / 2), y - 60, 8);
                return;
            }
            else if (node.item == Assets.ironScrapResource) {
                Text.draw(b, "Find iron scrap by mining underground!", x + 100
                        - (("Find iron scrap by mining underground!".length() - "Resources Required".length()) * 8 / 2), y - 60, 8);
                return;
            }
            else if (node.item == Assets.carbonSampleResource) {
                Text.draw(b, "Find carbon samples by killing life", x + 100
                        - (("Find carbon samples by killing life".length() - "Resources Required".length()) * 8 / 2), y - 60, 8);
                return;
            }
        }

        int recipeSize = r.getRecipe().size();
        int c = 0;
        for(Map.Entry<Item, String> entry : r.getRecipe().entrySet()) {
            b.draw(entry.getKey().getTexture(), x + 100 + 68 + c, y - 74, 24, 24);
            Text.draw(b, "x" + Utils.toString(Utils.parseInt(entry.getValue())), x + 100 + 68 + c + 8, y - 74 - 8, 8);
            Text.draw(b, entry.getKey().getName(), x + 100 + Math.abs(((entry.getKey().getName().length() * 8) / 2) -
                    (("Resources Required".length() * 8) / 2)) + 4, y - 100, 8);
            c += 40;
            if(c + 40 > 80) c = 0;
        }
    }

    public void checkResources(Node node) {

        InventorySlot[][] slots = Inventory.slots;
        HashMap<String, Integer> invTally = new HashMap<String, Integer>();

        try {
            ((ItemType.Resource) node.item.getItemType()).getItemRecipe().getRecipe();
        } catch (NullPointerException e) { return; }

        for(Map.Entry<Item, String> entry : ((ItemType.Resource) node.item.getItemType()).getItemRecipe().getRecipe().entrySet()) {

            for(int y = 0; y < 500 / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < 700 / InventorySlot.SLOT_WIDTH; x++) {
                    if (slots[x][y] != null) {
                        try {
                            if (slots[x][y].getItem().getItemID() == entry.getKey().getItemID()) {
                                if (!(invTally.containsKey(slots[x][y].getItem().getName()))) {
                                    invTally.put(slots[x][y].getItem().getName(), slots[x][y].itemCount);
                                } else {
                                    invTally.put(slots[x][y].getItem().getName(),
                                            invTally.get(slots[x][y].getItem().getName()) + slots[x][y].itemCount);
                                }
                            }
                        } catch (NullPointerException ignored) {}
                    }
                }
            }

        }

        int checkout = 0;
        for(Map.Entry<String, Integer> invTal : invTally.entrySet()) {
            for(Map.Entry<Item, String> recipe : ((ItemType.Resource)node.item.getItemType()).getItemRecipe().getRecipe().entrySet()) {
                if(invTal.getKey().equals(recipe.getKey().getName())) {
                    if(invTal.getValue() >= Utils.parseInt(recipe.getValue())) {
                        checkout++;
                        break;
                    }
                }
            }
        }

        node.canCreate = checkout >= ((ItemType.Resource)node.item.getItemType()).getItemRecipe().getRecipe().size();
        node.isAvailable = node.canCreate;

    }

    public void create(Node node) {

        InventorySlot[][] slots = Inventory.slots;

        try {
            ((ItemType.Resource) node.item.getItemType()).getItemRecipe().getRecipe();
        } catch (NullPointerException e) { return; }

        for (Map.Entry<Item, String> entry : ((ItemType.Resource)node.item.getItemType()).getItemRecipe().getRecipe().entrySet()) {
            yLoop: for (int y = 0; y < 500 / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < 700 / InventorySlot.SLOT_WIDTH; x++) {
                    if (slots[x][y] != null) {
                        try {
                            if (slots[x][y].getItem().getItemID() == entry.getKey().getItemID()) {
                                slots[x][y].itemCount -= Math.min(slots[x][y].itemCount, Utils.parseInt(entry.getValue()));
                                break yLoop;
                            }
                        } catch (NullPointerException ignored) {}
                    }
                }
            }
        }

        inventory.addItem(node.item);
    }

    private void checkNodeCords(Node node, int x, int y) {
        if(node.x != x && node.y != y) {
            node.x = x; node.y = y;
            node.updateCollider(x, y);
        }
    }

    public void createNode(Node node) {
        nodes.add(node);
    }

    public void createNode(String label, Item item, int resource) {
        Node node = new Node(label, nodes.size(), item, resource);
        nodes.add(node);
    }

    public void createNode(String label, Item item, boolean available, int resource) {
        Node node = new Node(label, nodes.size(), item, resource);
        node.setAvailable(available);
        nodes.add(node);
    }

    public Node getNode(int index) {
        for(int i = 0; i < nodes.size(); i++) {
            if(i == index) return nodes.get(i);
        }

        return null;
    }

}
