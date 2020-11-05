package dev.iwilkey.designa.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import dev.iwilkey.designa.input.InputHandler;

import java.util.ArrayList;

public class UIButton extends UIObject {

    public BitmapFont font;
    int relX, relY, relW, relH;
    short textX, textY;
    ClickListener clickListener;
    Texture background, outline;
    public String label;
    public int textSize = 22;
    public Color outlineColor;
    public Color backgroundColor;

    public ArrayList<Color> colors;

    public UIButton(String label, int x, int y, int width, int height, ClickListener clickListener) {
        super(UIObjectType.BUTTON, x, y, width, height);
        font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
        this.clickListener = clickListener;
        this.label = label;
        relX = x; relY = y;
        relW = width; relH = height;

        colors = new ArrayList<>();
        colors.add(new Color(196f/255,196f/255,196f/255,1)); // Normal background color
        colors.add(new Color(100f/255,100f/255,100f/255,1)); // Outline color
        colors.add(new Color(120f/255,120f/255,120f/255,1)); // Hovering color
        colors.add(new Color(80f/255,80f/255,80f/255,1)); // Clicked color

        this.backgroundColor = colors.get(0);
        this.outlineColor = colors.get(1);
        outline = new Texture("textures/ui/button_outline.png");
        background = new Texture("textures/ui/button_background.png");
        font.getData().setScale(textSize / 128.0f);
        textX = (short)(x + (((textSize * label.length()) - relW) / 2f) - 4);
        textY = (short)(y + (relH / 2f) + 6);
    }

    @Override
    public void tick() {
        if(hovering) {
            if(InputHandler.leftMouseButton) backgroundColor = colors.get(3);
            else backgroundColor = colors.get(2);
        } else backgroundColor = colors.get(1);
    }

    @Override
    public void render(Batch b) {
       b.setColor(backgroundColor);
       b.draw(background, relX, relY, relW, relH);
       b.setColor(Color.WHITE);
       b.setColor(outlineColor);
       b.draw(outline, relX, relY, relW, relH);
       b.setColor(Color.WHITE);

       font.getData().setScale(textSize / 128.0f);
       font.draw(b, label, textX, textY);
    }

    @Override
    public void onClick() {
        clickListener.onClick();
    }
}
