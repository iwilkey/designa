package com.iwilkey.designa.inventory.technology.subregion;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.input.InputHandler;
import com.iwilkey.designa.inventory.technology.TechnologyRegion;
import com.iwilkey.designa.inventory.technology.ui.ScrollBar;
import com.iwilkey.designa.inventory.technology.ui.SelectableItemCell;
import com.iwilkey.designa.items.Item;

import java.util.ArrayList;

public class ItemRepresentationGrid extends SubRegion {

    String itemType;
    ArrayList<Item> itemRepList;
    ArrayList<SelectableItemCell> itemCells;
    ScrollBar scrollBar;

    public ItemRepresentationGrid(String itemType, ArrayList<Item> itemRepList, int x, int y, int w, int h) {
        super("item-rep-grid", x, y, w, h);
        this.itemType = itemType;
        this.itemRepList = itemRepList;
        itemCells = new ArrayList<>();

        int[][] dimensions = deferDimensions(itemRepList);
        int height = dimensions[0][0];
        int remain = dimensions[0][1];
        height = (remain == 0) ? height : height + 1;
        int width = 8;

        System.out.println("Height: " + height + " remain: " + remain);

        try {
            for (int yy = 0; yy < height; yy++) {
                int yyy = ((y + h) - ((yy + 1) * 68));
                if (remain == 0) {
                    for (int xx = 1; xx <= width; xx++) {
                        int index = ((xx - 1) + (yy * width));
                        int xxx = x + (xx * 56);
                        System.out.println(index);
                        itemCells.add(new SelectableItemCell(itemRepList.get(index), xxx, yyy, 48, 48));
                    }
                } else {
                    if (yy != height - 1) {
                        for (int xx = 1; xx <= width; xx++) {
                            int index = ((xx - 1) + (yy * width));
                            int xxx = x + (xx * 56);
                            System.out.println(index);
                            itemCells.add(new SelectableItemCell(itemRepList.get(index), xxx, yyy, 48, 48));
                        }
                    } else {
                        for (int xx = 1; xx <= remain; xx++) {
                            int index = ((xx - 1) + (yy * width));
                            int xxx = x + (xx * 56);
                            System.out.println(index);
                            itemCells.add(new SelectableItemCell(itemRepList.get(index), xxx, yyy, 48, 48));
                        }
                    }
                }
            }
        } catch(IndexOutOfBoundsException ignored) {}

        scrollBar = new ScrollBar((x + w) - 30, y + 10, h - 20, itemCells, dimensions[0][0]);
    }

    private int[][] deferDimensions(ArrayList<Item> itemRepList) {
        int height = (int)(Math.floor(((double)itemRepList.size() / 8)));
        if(height < 1) height = 1;
        int remainder = itemRepList.size() % 8;
        int[][] returner = new int[1][2];
        returner[0][0] = height;
        returner[0][1] = remainder;
        return returner;
    }


    @Override
    public void tick() {
        if(!TechnologyRegion.itemTypeLookat.equals(itemType)) return;
        scrollBar.tick();
        if(InputHandler.leftMouseButtonDown) for(SelectableItemCell si : itemCells) si.isSelected = false;
        for(SelectableItemCell si : itemCells) si.tick(itemCells, y, h);
    }

    @Override
    public void render(Batch b) {
        if(!TechnologyRegion.itemTypeLookat.equals(itemType)) return;
        renderBorder(b);
        scrollBar.render(b);

        int count = 0;
        for(SelectableItemCell si : itemCells) if(si.isSelected) {
            count++; break;
        }
        if(count == 0) {
            Text.draw(b, "No Item Selected!", ((x + w) - (w / 2) -
                    (("No Item Selected!".length() * 12) / 2)), y - 22, 12);
            Text.draw(b, "Click an item to observe it", ((x + w) - (w / 2) -
                    (("Click an item to observe it".length() * 8) / 2)) - 6, y - 44, 8);
        }

        for(SelectableItemCell si : itemCells) {
            si.render(b, x, y, h, w);
        }

    }

    @Override
    public void renderBorder(Batch b) {
        // b.draw(Assets.square, x, y, w, h);
        b.draw(Assets.line, x, y, w, 2);
    }

}
