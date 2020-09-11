package com.iwilkey.designa.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.gui.ui.ClickListener;
import com.iwilkey.designa.gui.ui.TextButton;
import com.iwilkey.designa.gui.ui.UIManager;
import com.iwilkey.designa.input.InputHandler;

import java.util.ArrayList;

public class WorldLoaderState extends State {

    int w, h;
    Texture bg;
    private UIManager uiManager;

    @Override
    public void start() {

        InputHandler.initMainMenuStateInput();
        bg = new Texture("textures/mainmenu/mainmenubg.png");
        w = Gdx.graphics.getWidth(); h = Gdx.graphics.getHeight();

        uiManager = new UIManager();
        InputHandler.setUIManager(uiManager);

        uiManager.addObject(new TextButton((w / 2), h - 800, 32, "Back", new ClickListener() {
            @Override
            public void onClick() {
                switchState(1);
            }
        }));

        initSaves();

    }

    private void initSaves() {

        ArrayList<String> titles = new ArrayList<>();
        for(FileHandle f : Gdx.files.local("worlds/").list()) {
            if(f.isDirectory()) {
                titles.add(f.name());
            }
        }

        for(int s = 0; s < titles.size(); s++) {
            int finalS = s;
            TextButton button = new TextButton(w / 2, (h / 2) - (30 * (s - (titles.size()) / 2)), 20, titles.get(s), new ClickListener() {
                @Override
                public void onClick() {
                    switchState(4); // Switch to game state
                    GameState.loadWorld("worlds/" + titles.get(finalS) + "/");
                }
            });
            uiManager.addObject(button);
        }

    }

    int bgOff = 0;
    boolean pos = true;
    @Override
    public void tick() {

        uiManager.tick();

        if(pos) bgOff += 1.0f;
        else bgOff -= 1.0f;
        if(bgOff >= 1400) pos = !pos;
        else if (bgOff <= 0) pos = !pos;

    }

    @Override
    public void render(Batch b) {

        b.draw(bg, -50 - bgOff, 0, bg.getWidth() * 2, bg.getHeight() * 2);
        Text.draw(b, "Load World", (w / 2) - ("Load World".length() * 38) / 2, h - 120, 38);

        uiManager.render(b);
        Text.draw(b, "Designa " + Assets.VERSION, 10, 10, 11);

    }

    @Override
    public void onGUI(Batch b) {

    }

    @Override
    public void dispose() {

    }


}
