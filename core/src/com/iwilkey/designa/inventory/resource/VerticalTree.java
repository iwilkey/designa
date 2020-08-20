package com.iwilkey.designa.inventory.resource;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.items.ItemType;
import com.iwilkey.designa.utils.Utils;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Map;

public class VerticalTree {

    public static class Node {
        private String label;
        private int level;
        private boolean isAvailable;

        public boolean isSelected;
        public int x, y, w, h;
        public Rectangle collider;

        public Item item;

        public Node(String label, int level, Item item) {
            this.label = label;
            this.level = level;
            this.item = item;
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

    private String name;
    public ArrayList<Node> nodes;
    private TextureRegion nodeConnector;

    public VerticalTree(String name) {
        this.name = name;
        this.nodes = new ArrayList<>();
        nodeConnector = Assets.nodeConnector;
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
                    Assets.invClick.play(0.35f);
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

    private void checkNodeCords(Node node, int x, int y) {
        if(node.x != x && node.y != y) {
            node.x = x; node.y = y;
            node.updateCollider(x, y);
        }
    }

    public void createNode(Node node) {
        nodes.add(node);
    }

    public void createNode(String label, int level, Item item) {
        Node node = new Node(label, level, item);
        nodes.add(node);
    }

    public void createNode(String label, Item item) {
        Node node = new Node(label, nodes.size(), item);
        nodes.add(node);
    }

    public void createNode(String label, Item item, boolean available) {
        Node node = new Node(label, nodes.size(), item);
        node.setAvailable(available);
        nodes.add(node);
    }

    public Node getNode(int index) {
        for(int i = 0; i < nodes.size(); i++) {
            if(i == index) return nodes.get(i);
        }

        return null;
    }

    public void setNodeLabel(int index, String label) {
        for(int i = 0; i < nodes.size(); i++) {
            if(i == index) nodes.get(i).setLabel(label);
        }
    }

    public void setNodeLevel(int index, int lvl) {
        for(int i = 0; i < nodes.size(); i++) {
            if(i == index) nodes.get(i).setLevel(lvl);
        }
    }
}
