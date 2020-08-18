package com.iwilkey.designa.inventory.resource;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;

import java.awt.Rectangle;
import java.util.ArrayList;

public class VerticalTree {

    public static class Node {
        private String label;
        private int level;
        private boolean isAvailable;

        public boolean isSelected;
        public int x, y, w, h;
        public Rectangle collider;

        public Node(String label, int level) {
            this.label = label;
            this.level = level;
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
            collider.x = x - (dif / 2); collider.y = y - ((h / 2) - (h / 5));
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

    private void checkNodeCords(Node node, int x, int y) {
        if(node.x != x && node.y != y) {
            node.x = x; node.y = y;
            node.updateCollider(x, y);
        }
    }


    public void createNode(Node node) {
        nodes.add(node);
    }

    public void createNode(String label, int level) {
        Node node = new Node(label, level);
        nodes.add(node);
    }

    public void createNode(String label) {
        Node node = new Node(label, nodes.size());
        nodes.add(node);
    }

    public void createNode(String label, boolean available) {
        Node node = new Node(label, nodes.size());
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
