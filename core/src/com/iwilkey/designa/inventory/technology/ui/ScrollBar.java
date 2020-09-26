package com.iwilkey.designa.inventory.technology.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.input.InputHandler;

import java.awt.Rectangle;
import java.util.ArrayList;

public class ScrollBar {

    public ArrayList<SelectableItemCell> itemCells;
    public int lineX, lineY, lineHeight;
    public int barX, barY, barWidth, barHeight;
    public int itemCellH;
    public Rectangle collider;
    public boolean isSliding = false;

    public ScrollBar(int x, int y, int h, ArrayList<SelectableItemCell> itemCells, int itemCellH) {
        this.lineX = x; this.lineY = y; this.lineHeight = h;
        barWidth = 16; barHeight = 100;
        barX = x - (barWidth / 2);
        barY = y + h - barHeight;
        collider = new Rectangle(barX, barY, barWidth, barHeight);
        this.itemCells = itemCells;
        this.itemCellH = itemCellH;
    }

    public void tick() {
        input();
        collider.x = barX; collider.y = barY;
    }

    private float deferScrollCompletion() {
        return Math.min(100, (Math.abs(((float) (barY - (lineY + lineHeight - barHeight)) / lineY) * 3.03f)) * 100);
    }

    private void scrollCells() {
        for(SelectableItemCell si : itemCells) {
            int off = (itemCellH < 4) ? 0 : itemCellH - 3;
            si.offY = ((deferScrollCompletion() / 100)) * (off * 64);
        }
    }

    private int clamp(int newBarY) {
        if(newBarY > lineY + lineHeight - barHeight) {
            return lineY + lineHeight - barHeight;
        } else return Math.max(newBarY, lineY);
    }

    int lastY = 0;
    private void input() {
        Rectangle mouse = new Rectangle(InputHandler.cursorX, InputHandler.cursorY, 1, 1);
        if(mouse.intersects(collider) && InputHandler.leftMouseButtonDown) isSliding = true;

        if(isSliding) {
            int dy = InputHandler.cursorY - lastY;
            barY = clamp(barY + dy);
            scrollCells();
            if(!InputHandler.leftMouseButton) isSliding = false;
        }

        lastY = InputHandler.cursorY;
    }

    public void render(Batch b) {
        b.draw(Assets.line, lineX, lineY, 2, lineHeight);
        b.draw(Assets.square, barX, barY, barWidth, barHeight);
    }

}
