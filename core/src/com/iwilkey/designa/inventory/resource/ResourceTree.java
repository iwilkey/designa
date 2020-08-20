package com.iwilkey.designa.inventory.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.items.Item;

import java.awt.Rectangle;
import java.util.Arrays;

public class ResourceTree {

    private final Inventory inventory;
    private final int x, y;

    private final int[] resourceCheckout;
    private final String[] resourceNames;

    private final int buttonW = 64, buttonH = 32;
    private final Rectangle[] buttonColliders;

    private final VerticalTree[] trees;

    public ResourceTree(Inventory inv) {
        this.inventory = inv;
        x = ((Gdx.graphics.getWidth()) - Inventory.bw - (Inventory.bw + 64)) - 19;
        y = (Gdx.graphics.getHeight() / 2) - (Inventory.bh / 2) + 15;

        resourceCheckout = new int[6];
        resourceCheckout[0] = 1;

        int xx = ((buttonW * 3) / 2) - 12;
        int yy = Gdx.graphics.getHeight() - 120 + buttonH + 4;
        buttonColliders = new Rectangle[6];
        buttonColliders[0] = new Rectangle(xx + x, yy - y, buttonW, buttonH);
        buttonColliders[1] = new Rectangle(xx + x + (buttonW), yy - y, buttonW, buttonH);
        buttonColliders[2] = new Rectangle(xx + x + (buttonW * 2), yy - y, buttonW, buttonH);
        buttonColliders[3] = new Rectangle(xx + x, yy - y - (buttonH + 4), buttonW, buttonH);
        buttonColliders[4] = new Rectangle(xx + x + (buttonW), yy - y - (buttonH + 4), buttonW, buttonH);
        buttonColliders[5] = new Rectangle(xx + x + (buttonW * 2), yy - y - (buttonH + 4), buttonW, buttonH);

        resourceNames = new String[6];
        resourceNames[0] = "Wood";
        resourceNames[1] = "Stone";
        resourceNames[2] = "Copper";
        resourceNames[3] = "Silver";
        resourceNames[4] = "Iron";
        resourceNames[5] = "Carbon";

        trees = new VerticalTree[6];
        trees[0] = new VerticalTree("Wood");

        // TODO: Make items for all these resources
            trees[0].createNode("Bark", Assets.barkResource, true);
            trees[0].createNode("Sticks", Assets.stickResource);
            trees[0].createNode("Plywood", Assets.plywoodResource);
            trees[0].createNode("Hardwood", Assets.hardwoodResource);
            trees[0].createNode("Reinforced Hardwood", Assets.reinforcedHardwoodResource);
            trees[0].createNode("Strongwood", Assets.strongwoodResource);
            trees[0].createNode("Reinforced Strongwood", Assets.reinforcedStrongwoodResource);

        trees[1] = new VerticalTree("Stone");
            trees[1].createNode("Rocks", Assets.rockResource, true);
            trees[1].createNode("Gravel", Assets.gravelResource);
            trees[1].createNode("Weak Concrete", Assets.concreteResource);
            trees[1].createNode("Reinforced Concrete", Assets.reinforcedConcreteResource);
            trees[1].createNode("Condensed Slab", Assets.condensedSlabResource);
            trees[1].createNode("Strongstone", Assets.strongstoneResource);
            trees[1].createNode("Reinforced Strongstone", Assets.reinforcedStrongstoneResource);

        trees[2] = new VerticalTree("Copper");
            trees[2].createNode("Copper Scrap", Assets.copperScrapResource, true);
            trees[2].createNode("Recycled Copper", Assets.recycledCopperResource);
            trees[2].createNode("Bluestone", Assets.bluestoneResource);
            trees[2].createNode("Reinforced Bluestone", Assets.reinforcedBluestoneResource);
            trees[2].createNode("Roman Vitriol", Assets.romanVitriolResource);

        trees[3] = new VerticalTree("Silver");
            trees[3].createNode("Silver Scrap", Assets.silverScrapResource, true);
            trees[3].createNode("Recycled Silver", Assets.recycledSilverResource);
            trees[3].createNode("Coin Silver", Assets.coinSilverResource);
            trees[3].createNode("Sterling Silver", Assets.sterlingSilverResource);
            trees[3].createNode("Reinforced Sterling Silver", Assets.reinforcedSterlingSilverResource);
            trees[3].createNode("Fine Silver", Assets.fineSilverResource);
            trees[3].createNode("Reinforced Fine Silver", Assets.reinforcedFineSilverResource);

        trees[4] = new VerticalTree("Iron");
            trees[4].createNode("Iron Scrap", Assets.ironScrapResource, true);
            trees[4].createNode("Recycled Iron", Assets.recycledIronResource);
            trees[4].createNode("Cast Iron", Assets.castIronResource);
            trees[4].createNode("Reinforced Cast Iron", Assets.reinforcedCastIronResource);
            trees[4].createNode("Steel", Assets.steelResource);
            trees[4].createNode("Reinforced Steel", Assets.reinforcedSteelResource);

        trees[5] = new VerticalTree("Carbon");
            trees[5].createNode("Carbon Sample", Assets.carbonSampleResource,true);
            trees[5].createNode("Graphite", Assets.graphiteResource);
            trees[5].createNode("Compressed Graphite", Assets.compressedGraphite);
            trees[5].createNode("Weak Diamond", Assets.weakDiamondResource);
            trees[5].createNode("Diamond", Assets.diamondResource);
            trees[5].createNode("Reinforced Diamond", Assets.reinforcedDiamondResource);

    }

    public void tick() {
        input();
        for(VerticalTree tree : trees) tree.tick();
    }

    private void input() {
        if(InputHandler.leftMouseButtonDown) {
            Rectangle rect = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
            for(int i = 0; i < buttonColliders.length; i++) {
                if(rect.intersects(buttonColliders[i])) chooseResource(i);
            }
        }
    }

    public void render(Batch b) {
        renderUnavailableNodes(b, selected());
        b.draw(Assets.blueprintGUI, x, y,
                Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
        renderAvailableNodes(b, selected());
        Text.draw(b, "Resource", x + 130, Game.h - y - 12, 11);
        Text.draw(b, "Tree", x + 130 + (2 * 11), Game.h - y - 12 - 16, 11);

        renderButtons(b);
    }

    private void renderButtons(Batch b) {
        for(int ii = 0; ii < buttonColliders.length; ii++) {
            b.draw(Assets.inventorySlot, buttonColliders[ii].x, buttonColliders[ii].y,
                    buttonColliders[ii].width, buttonColliders[ii].height);
            Text.draw(b, resourceNames[ii], (buttonColliders[ii].x + Math.abs(((resourceNames[ii].length() * 8) / 2) - (buttonW / 2))) - 2,
                    buttonColliders[ii].y + (buttonH / 2) - 4, 8);
        }

        for(int i = 0; i < resourceCheckout.length; i++) {
            if(resourceCheckout[i] == 1) {
                b.draw(Assets.inventorySelector, buttonColliders[i].x, buttonColliders[i].y,
                        buttonColliders[i].width, buttonColliders[i].height);
            }
        }
    }

    private void renderAvailableNodes(Batch b, int resource) {
        int yy = 196;
        switch(resource) {
            case 0:
                trees[0].renderAvailable(b, x - 4, y + yy, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 1:
                trees[1].renderAvailable(b, x - 4, y + yy, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 2:
                trees[2].renderAvailable(b, x - 4, y + yy + 44, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 3:
                trees[3].renderAvailable(b, x - 4, y + yy, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 4:
                trees[4].renderAvailable(b, x - 4, y + yy + 22, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 5:
                trees[5].renderAvailable(b, x - 4, y + yy + 22, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
        }
    }

    private void renderUnavailableNodes(Batch b, int resource) {
        int yy = 196;
        switch(resource) {
            case 0:
                trees[0].renderUnavailable(b, x - 4, y + yy, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 1:
                trees[1].renderUnavailable(b, x - 4, y + yy, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 2:
                trees[2].renderUnavailable(b, x - 4, y + yy + 44, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 3:
                trees[3].renderUnavailable(b, x - 4, y + yy, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 4:
                trees[4].renderUnavailable(b, x - 4, y + yy + 22, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
            case 5:
                trees[5].renderUnavailable(b, x - 4, y + yy + 22, Inventory.BLUEPRINT_SIZE.width, Inventory.BLUEPRINT_SIZE.height);
                break;
        }
    }

    private void chooseResource(int index) {
        Assets.invClick.play(0.35f);
        Arrays.fill(resourceCheckout, 0);
        resourceCheckout[index] = 1;
    }

    private int selected() {
        for(int i = 0; i < resourceCheckout.length; i++) if(resourceCheckout[i] == 1) return i;
        return 0;
    }

}
