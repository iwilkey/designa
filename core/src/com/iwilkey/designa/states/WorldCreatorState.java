package com.iwilkey.designa.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.gfx.Text;
import com.iwilkey.designa.gui.ui.ClickListener;
import com.iwilkey.designa.gui.ui.InputField;
import com.iwilkey.designa.gui.ui.TextButton;
import com.iwilkey.designa.gui.ui.UIManager;
import com.iwilkey.designa.input.InputHandler;

public class WorldCreatorState extends State {

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

        // World name
        // Difficulty
        // Player name

        uiManager.addObject(new InputField((w / 2), h - 280, 700, 64));
        uiManager.addObject(new TextButton((w / 2), h - 436, 32, "Easy", new ClickListener() {
            @Override
            public void onClick() {
                TextButton button = ((TextButton)(uiManager.getObjects().get(1)));
                switch(button.getText()) {
                    case "Easy":
                        button.setText("Okay");
                        break;
                    case "Okay":
                        button.setText("Hard");
                        break;
                    case "Hard":
                        button.setText("Easy");
                        break;
                }
            }
        }));
        uiManager.addObject(new InputField((w / 2), h - 580, 700, 64));
        uiManager.addObject(new TextButton((w / 2), h - 700, 32, "Create", new ClickListener() {
            @Override
            public void onClick() {
                String name = ((InputField)(uiManager.getObjects().get(0))).getText();
                if(!checkName(name)) return;
                switchState(3); // Switch to game state
                GameState.createNewWorld(name, 1, "Aaron");
            }
        }));
        uiManager.addObject(new TextButton((w / 2), h - 800, 32, "Back", new ClickListener() {
            @Override
            public void onClick() {
                switchState(1);
            }
        }));


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
        Text.draw(b, "New World", (w / 2) - ("New World".length() * 38) / 2, h - 120, 38);

        // Labels
        Text.draw(b, "World Name", (w / 2) - ("World Name".length() * 24) / 2, h - 220, 24);
        Text.draw(b, "Difficulty", (w / 2) - ("Difficulty".length() * 24) / 2, h - 360, 24);
        Text.draw(b, "Player Name", (w / 2) - ("Player Name".length() * 24) / 2, h - 510, 24);


        uiManager.render(b);
        Text.draw(b, "Designa " + Assets.VERSION, 10, 10, 11);

    }

    private boolean checkName(String name) {
        if(name.length() == 0) return false;
        for(FileHandle f : Gdx.files.local("worlds/").list()) {
            if(f.isDirectory()) {
                if(name.equals(f.name())) return false;
            }
        }
        return true;
    }

    @Override
    public void onGUI(Batch b) {

    }

    @Override
    public void dispose() {

    }

}
