package dev.iwilkey.designa.ui.menu;

import com.badlogic.gdx.Input;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.Settings;
import dev.iwilkey.designa.input.InputHandler;
import dev.iwilkey.designa.inventory.Inventory;
import dev.iwilkey.designa.inventory.Slot;
import dev.iwilkey.designa.scene.Scene;
import dev.iwilkey.designa.ui.*;

import java.awt.*;

public class PauseMenu extends Menu {

    public enum PauseMenuLevel {

        ALL (-1),
        HOME (0),
        SETTINGS (1),
        KEY_BINDINGS (2),
        USER_INTERFACE_SETTINGS (3),
        FONT_SIZE (4),
        SLOT_SIZE (5);

        int level;
        PauseMenuLevel(int level) {
            this.level = level;
        }

    }

    final UIButton WALK_RIGHT_BUTTON, WALK_LEFT_BUTTON,
        JUMP_BUTTON, OPEN_INV_BUTTON, BACK_BUTTON,
        ZOOM_IN_BUTTON, ZOOM_OUT_BUTTON;

    final UIText FONT_SIZE_TEXT, SLOT_SIZE_TEXT,
            SLOT_SPACING_TEXT, GUI_ITEM_SIZE_TEXT;

    public static boolean choosingBinding = false;

    public PauseMenu() {
        super(new UIManager("Pause Menu"));

        // ALL
        manager.addText(new UIText("Game Paused", PauseMenuLevel.ALL.level, 48, 40, Game.WINDOW_HEIGHT - 200));

        // HOME
        manager.addText(new UIText("Home", PauseMenuLevel.HOME.level, 8, 40, Game.WINDOW_HEIGHT - 250));

        manager.addButton(new UIButton("Back To Game", PauseMenuLevel.HOME.level, 40, (Game.WINDOW_HEIGHT / 2) + 30, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                Scene.SinglePlayerGameScene.isPaused = false;
            }
        }));

        manager.addButton(new UIButton("Settings", PauseMenuLevel.HOME.level, 40, (Game.WINDOW_HEIGHT / 2) - 30, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.SETTINGS.level;
                timer = 0;
            }
        }));

        // SETTINGS
        manager.addText(new UIText("Settings", PauseMenuLevel.SETTINGS.level, 8, 40, Game.WINDOW_HEIGHT - 250));

        manager.addButton(new UIButton("Key Bindings", PauseMenuLevel.SETTINGS.level, 40, (Game.WINDOW_HEIGHT / 2) + 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.KEY_BINDINGS.level;
                timer = 0;
            }
        }));

        manager.addButton(new UIButton("User Interface Settings", PauseMenuLevel.SETTINGS.level, 40, (Game.WINDOW_HEIGHT / 2), 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.USER_INTERFACE_SETTINGS.level;
                timer = 0;
            }
        }));

        manager.addButton(new UIButton("Back", PauseMenuLevel.SETTINGS.level, 40, (Game.WINDOW_HEIGHT / 2) - 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.HOME.level;
                timer = 0;
            }
        }));

        // Key Bindings
        manager.addText(new UIText("Key Bindings", PauseMenuLevel.KEY_BINDINGS.level, 8, 40, Game.WINDOW_HEIGHT - 250));

        manager.addButton(new UIButton("Back",
                PauseMenuLevel.KEY_BINDINGS.level, 40, (Game.WINDOW_HEIGHT / 2) - 100, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.SETTINGS.level;
                timer = 0;
            }
        }));

        WALK_RIGHT_BUTTON = manager.addButton(new UIButton("Walk Right: " + Input.Keys.toString(Settings.RIGHT_KEY),
                PauseMenuLevel.KEY_BINDINGS.level, 40, (Game.WINDOW_HEIGHT / 2) + 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                choosingBinding = true;
                WALK_RIGHT_BUTTON.label = "Press a key...";
                InputHandler.lastKeyPressed = -1;
                new Thread() {
                    public void run() {
                        while(true) {
                            choosingBinding = true;
                            if(InputHandler.lastKeyPressed != -1) {
                                Settings.RIGHT_KEY = InputHandler.lastKeyPressed;
                                InputHandler.lastKeyPressed = -1;
                                WALK_RIGHT_BUTTON.label = "Walk Right: " + Input.Keys.toString(Settings.RIGHT_KEY);
                                choosingBinding = false;
                                break;
                            }
                            try { sleep(80); } catch (InterruptedException ignored) {}
                        }
                    }
                }.start();
            }
        }));

        WALK_LEFT_BUTTON = manager.addButton(new UIButton("Walk Left: " + Input.Keys.toString(Settings.LEFT_KEY),
                PauseMenuLevel.KEY_BINDINGS.level, 40, (Game.WINDOW_HEIGHT / 2), 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                choosingBinding = true;
                WALK_LEFT_BUTTON.label = "Press a key...";
                InputHandler.lastKeyPressed = -1;
                new Thread() {
                    public void run() {
                        while(true) {
                            choosingBinding = true;
                            if(InputHandler.lastKeyPressed != -1) {
                                Settings.LEFT_KEY = InputHandler.lastKeyPressed;
                                InputHandler.lastKeyPressed = -1;
                                WALK_LEFT_BUTTON.label = "Walk Left: " + Input.Keys.toString(Settings.LEFT_KEY);
                                choosingBinding = false;
                                break;
                            }
                            try { sleep(80); } catch (InterruptedException ignored) {}
                        }
                    }
                }.start();
            }
        }));

        JUMP_BUTTON = manager.addButton(new UIButton("Jump: " + Input.Keys.toString(Settings.JUMP_KEY),
                PauseMenuLevel.KEY_BINDINGS.level, 40, (Game.WINDOW_HEIGHT / 2) - 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                choosingBinding = true;
                JUMP_BUTTON.label = "Press a key...";
                InputHandler.lastKeyPressed = -1;
                new Thread() {
                    public void run() {
                        while(true) {
                            choosingBinding = true;
                            if(InputHandler.lastKeyPressed != -1) {
                                Settings.JUMP_KEY = InputHandler.lastKeyPressed;
                                InputHandler.lastKeyPressed = -1;
                                JUMP_BUTTON.label = "Jump: " + Input.Keys.toString(Settings.JUMP_KEY);
                                break;
                            }
                            try { sleep(80); } catch (InterruptedException ignored) {}
                        }
                    }
                }.start();
            }
        }));

        OPEN_INV_BUTTON = manager.addButton(new UIButton("Open Item Manager: " + Input.Keys.toString(Settings.OPEN_INVENTORY_KEY),
                PauseMenuLevel.KEY_BINDINGS.level, 280 + 60, (Game.WINDOW_HEIGHT / 2) + 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                choosingBinding = true;
                OPEN_INV_BUTTON.label = "Press a key...";
                InputHandler.lastKeyPressed = -1;
                new Thread() {
                    public void run() {
                        while(true) {
                            choosingBinding = true;
                            if(InputHandler.lastKeyPressed != -1) {
                                Settings.OPEN_INVENTORY_KEY = InputHandler.lastKeyPressed;
                                InputHandler.lastKeyPressed = -1;
                                OPEN_INV_BUTTON.label = "Open Item Manager: " + Input.Keys.toString(Settings.OPEN_INVENTORY_KEY);
                                choosingBinding = false;
                                break;
                            }
                            try { sleep(80); } catch (InterruptedException ignored) {}
                        }
                    }
                }.start();
            }
        }));

        BACK_BUTTON = manager.addButton(new UIButton("Exit Button: " + Input.Keys.toString(Settings.PAUSE_KEY),
                PauseMenuLevel.KEY_BINDINGS.level, 280 + 60, (Game.WINDOW_HEIGHT / 2), 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                choosingBinding = true;
                BACK_BUTTON.label = "Press a key...";
                InputHandler.lastKeyPressed = -1;
                new Thread() {
                    public void run() {
                        while(true) {
                            choosingBinding = true;
                            if(InputHandler.lastKeyPressed != -1) {
                                Settings.PAUSE_KEY = InputHandler.lastKeyPressed;
                                InputHandler.lastKeyPressed = -1;
                                BACK_BUTTON.label = "Exit Button: " + Input.Keys.toString(Settings.PAUSE_KEY);
                                choosingBinding = false;
                                break;
                            }
                            try { sleep(80); } catch (InterruptedException ignored) {}
                        }
                    }
                }.start();
            }
        }));

        ZOOM_IN_BUTTON = manager.addButton(new UIButton("Zoom Out: " + Input.Keys.toString(Settings.ZOOM_IN_KEY),
                PauseMenuLevel.KEY_BINDINGS.level, 280 + 60, (Game.WINDOW_HEIGHT / 2) - 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                choosingBinding = true;
                ZOOM_IN_BUTTON.label = "Press a key...";
                InputHandler.lastKeyPressed = -1;
                new Thread() {
                    public void run() {
                        while(true) {
                            choosingBinding = true;
                            if(InputHandler.lastKeyPressed != -1) {
                                Settings.ZOOM_IN_KEY = InputHandler.lastKeyPressed;
                                InputHandler.lastKeyPressed = -1;
                                ZOOM_IN_BUTTON.label = "Zoom Out: " + Input.Keys.toString(Settings.ZOOM_IN_KEY);
                                choosingBinding = false;
                                break;
                            }
                            try { sleep(80); } catch (InterruptedException ignored) {}
                        }
                    }
                }.start();
            }
        }));

        ZOOM_OUT_BUTTON = manager.addButton(new UIButton("Zoom In: " + Input.Keys.toString(Settings.ZOOM_OUT_KEY),
                PauseMenuLevel.KEY_BINDINGS.level, 280 + (280 - 40) + 120 + 60, (Game.WINDOW_HEIGHT / 2) + 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                choosingBinding = true;
                ZOOM_OUT_BUTTON.label = "Press a key...";
                InputHandler.lastKeyPressed = -1;
                new Thread() {
                    public void run() {
                        while(true) {
                            choosingBinding = true;
                            if(InputHandler.lastKeyPressed != -1) {
                                Settings.ZOOM_OUT_KEY = InputHandler.lastKeyPressed;
                                InputHandler.lastKeyPressed = -1;
                                ZOOM_OUT_BUTTON.label = "Zoom In: " + Input.Keys.toString(Settings.ZOOM_OUT_KEY);
                                choosingBinding = false;
                                break;
                            }
                            try { sleep(80); } catch (InterruptedException ignored) {}
                        }
                    }
                }.start();
            }
        }));

        // User Interface Settings
        manager.addText(new UIText("User Interface Settings", PauseMenuLevel.USER_INTERFACE_SETTINGS.level, 8, 40, Game.WINDOW_HEIGHT - 250));

        manager.addButton(new UIButton("Text Size",
                PauseMenuLevel.USER_INTERFACE_SETTINGS.level, 40, (Game.WINDOW_HEIGHT / 2) + 40, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.FONT_SIZE.level;
                timer = 0;
            }
        }));

        manager.addButton(new UIButton("Item Slot Size",
                PauseMenuLevel.USER_INTERFACE_SETTINGS.level, 40, (Game.WINDOW_HEIGHT / 2), 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.SLOT_SIZE.level;
                timer = 0;
            }
        }));

        manager.addButton(new UIButton("Back",
                PauseMenuLevel.USER_INTERFACE_SETTINGS.level, 40, (Game.WINDOW_HEIGHT / 2) - 100, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.SETTINGS.level;
                timer = 0;
            }
        }));


        // Font Size
        manager.addText(new UIText("Text Size", PauseMenuLevel.FONT_SIZE.level, 8, 40, Game.WINDOW_HEIGHT - 250));

        FONT_SIZE_TEXT = manager.addText(new UIText("Text Size: " + Settings.GUI_FONT_SIZE,
                PauseMenuLevel.FONT_SIZE.level, 0, 40, (Game.WINDOW_HEIGHT / 2) + 40 + 20));

        manager.addButton(new UIButton("+",
                PauseMenuLevel.FONT_SIZE.level, 40, (Game.WINDOW_HEIGHT / 2) - 60, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                Settings.GUI_FONT_SIZE++;
                FONT_SIZE_TEXT.message = "Text Size: " + Settings.GUI_FONT_SIZE;
            }
        }));

        manager.addButton(new UIButton("-",
                PauseMenuLevel.FONT_SIZE.level, 120, (Game.WINDOW_HEIGHT / 2) - 60, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                Settings.GUI_FONT_SIZE--;
                FONT_SIZE_TEXT.message = "Text Size: " + Settings.GUI_FONT_SIZE;
            }
        }));

        manager.addButton(new UIButton("Back",
                PauseMenuLevel.FONT_SIZE.level, 40, (Game.WINDOW_HEIGHT / 2) - 100, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.USER_INTERFACE_SETTINGS.level;
                timer = 0;
            }
        }));

        // Slot Size
        manager.addText(new UIText("Item Slot Size", PauseMenuLevel.SLOT_SIZE.level, 8, 40, Game.WINDOW_HEIGHT - 250));

        SLOT_SIZE_TEXT = manager.addText(new UIText("Item Slot Size: " + Settings.GUI_SLOT_SIZE + "px",
                PauseMenuLevel.SLOT_SIZE.level, 0, 40, (Game.WINDOW_HEIGHT / 2) + 80 + 20));

        SLOT_SPACING_TEXT = manager.addText(new UIText("Item Slot Spacing: " + Settings.GUI_SLOT_SPACING + "px",
                PauseMenuLevel.SLOT_SIZE.level, 0, 40, (Game.WINDOW_HEIGHT / 2) + 40 + 20));

        GUI_ITEM_SIZE_TEXT = manager.addText(new UIText("Item Texture Size: " + Settings.GUI_ITEM_TEXTURE_SIZE + "px",
                PauseMenuLevel.SLOT_SIZE.level, 0, 40, (Game.WINDOW_HEIGHT / 2) + 20));

        manager.addButton(new UIButton("+",
                PauseMenuLevel.SLOT_SIZE.level, 40, (Game.WINDOW_HEIGHT / 2) - 60, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                Settings.GUI_SLOT_SIZE += 2;
                setSettings();
            }
        }));

        manager.addButton(new UIButton("-",
                PauseMenuLevel.SLOT_SIZE.level, 120, (Game.WINDOW_HEIGHT / 2) - 60, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                Settings.GUI_SLOT_SIZE -= 2;
                setSettings();
            }
        }));

        manager.addButton(new UIButton("Back",
                PauseMenuLevel.SLOT_SIZE.level, 40, (Game.WINDOW_HEIGHT / 2) - 100, 30, 30, new ClickListener() {
            @Override
            public void onClick() {
                level = PauseMenuLevel.USER_INTERFACE_SETTINGS.level;
                timer = 0;
            }
        }));

    }

    private void setSettings() {
        Settings.GUI_SLOT_SPACING = (Settings.GUI_SLOT_SIZE / 4);
        Settings.GUI_ITEM_TEXTURE_SIZE = (Settings.GUI_SLOT_SIZE / 2);
        SLOT_SIZE_TEXT.message = "Item Slot Size: " + Settings.GUI_SLOT_SIZE + "px";
        SLOT_SPACING_TEXT.message = "Item Slot Spacing: " + Settings.GUI_SLOT_SPACING + "px";
        GUI_ITEM_SIZE_TEXT.message = "Item Texture Size: " + Settings.GUI_ITEM_TEXTURE_SIZE + "px";
    }

}
