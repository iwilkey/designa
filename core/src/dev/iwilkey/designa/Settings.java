package dev.iwilkey.designa;

import com.badlogic.gdx.Input;

public class Settings {

    public static int GUI_FONT_SIZE,
        GUI_FONT_SIZE_BIG,
        GUI_SLOT_SIZE,
        GUI_SLOT_SPACING,
        GUI_ITEM_TEXTURE_SIZE,
        GUI_SCROLL_SENSITIVITY,
        GUI_SCROLLWHEEL_SENSITIVITY,

        LEFT_KEY,
        RIGHT_KEY,
        OPEN_INVENTORY_KEY,
        JUMP_KEY,
        ZOOM_IN_KEY,
        ZOOM_OUT_KEY,
        PAUSE_KEY;

    public static float GUI_ITEM_LIST_FRICTION;

    public static void initSettings() {

        // GUI
        GUI_FONT_SIZE = 18;
        GUI_FONT_SIZE_BIG = 22;
        GUI_SLOT_SIZE = 64;
        GUI_SLOT_SPACING = 16;
        GUI_ITEM_TEXTURE_SIZE = GUI_SLOT_SIZE / 2;
        GUI_SCROLL_SENSITIVITY = 15;
        GUI_SCROLLWHEEL_SENSITIVITY = 2;
        GUI_ITEM_LIST_FRICTION = 0.3f;

        // Key Bindings (Computer)
        LEFT_KEY = Input.Keys.A;
        RIGHT_KEY = Input.Keys.D;
        OPEN_INVENTORY_KEY = Input.Keys.F;
        JUMP_KEY = Input.Keys.SPACE;
        ZOOM_IN_KEY = Input.Keys.Q;
        ZOOM_OUT_KEY = Input.Keys.E;
        PAUSE_KEY = Input.Keys.ESCAPE;

    }





}
