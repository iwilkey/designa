package dev.iwilkey.designa.input;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import dev.iwilkey.designa.Game;
import dev.iwilkey.designa.Settings;
import dev.iwilkey.designa.building.BuildingHandler;
import dev.iwilkey.designa.ui.UIObject;

public class InputHandler {

    private static boolean[] keys, jp, cp;
    private static boolean lmbd, rmbd, jcl, ccl, jcr, ccr;

    public static double dMouse, // Change in MousePos (magnitude)
        dx, dy;
    public static float cursorX, cursorY;
    public static boolean rightMouseButton, leftMouseButton, rightMouseButtonDown,
            leftMouseButtonDown, mouseCurrentlyMoving, rightMouseButtonUp, leftMouseButtonUp,
            moveLeftRequest, moveRightRequest, jumpRequest, placeTileRequest, damageTileRequest,
            openInventoryRequest, pauseRequest;
    public static float zoomRequest, scrollWheelRequestValue;

    public static int lastKeyPressed = -1;

    public static void init() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            keys = new boolean[256];
            jp = new boolean[256];
            cp = new boolean[256];
            setCursorAs("textures/cursor.png");
        }
    }

    public static void setCursorAs(String path) {
        Pixmap pix = new Pixmap(Gdx.files.internal(path));
        Cursor cursor = Gdx.graphics.newCursor(pix, 4, 4);
        Gdx.graphics.setCursor(cursor);
        pix.dispose();
    }

    public static void initSinglePlayerGameSceneInput() {
        switch(Gdx.app.getType()) {
            case Desktop:
                Gdx.input.setInputProcessor(new InputAdapter() {

                    @Override
                    public boolean keyDown(int key) {
                        lastKeyPressed = key;
                        keyJustPressed(key);
                        if(key == Settings.LEFT_KEY) moveLeftRequest = true;
                        if(key == Settings.RIGHT_KEY) moveRightRequest = true;
                        if(key == Settings.OPEN_INVENTORY_KEY) openInventoryRequest = true;
                        if(key == Settings.JUMP_KEY) jumpRequest = true;
                        if(key == Settings.PAUSE_KEY) pauseRequest = true;
                        return true;
                    }

                    @Override
                    public boolean keyUp(int key) {
                        if(key == Settings.LEFT_KEY) moveLeftRequest = false;
                        if(key == Settings.RIGHT_KEY) moveRightRequest = false;
                        if(key == Settings.OPEN_INVENTORY_KEY) openInventoryRequest = false;
                        if(key == Settings.JUMP_KEY) jumpRequest = false;
                        if(key == Settings.PAUSE_KEY) pauseRequest = false;
                        return true;
                    }

                    @Override
                    public boolean mouseMoved(int x, int y) {
                        cursorX = x; cursorY = Game.WINDOW_HEIGHT - y;
                        return true;
                    }

                    @Override
                    public boolean scrolled(int amount) {
                        scrollWheelRequestValue = amount;
                        return true;
                    }

                    @Override
                    public boolean touchDragged(int x, int y, int pointer) {
                        cursorX = x; cursorY = Game.WINDOW_HEIGHT - y;
                        return true;
                    }

                    @Override
                    public boolean touchDown(int x, int y, int pointer, int button) {
                        justClicked(button);
                        if(button == Input.Buttons.LEFT) {

                            lmbd = true;
                            leftMouseButton = true;

                            if(BuildingHandler.isBuilding) damageTileRequest = true;

                        }
                        if(button == Input.Buttons.RIGHT) {
                            rmbd = true;
                            rightMouseButton = true;

                            if(BuildingHandler.isBuilding) placeTileRequest = true;

                        }
                        return true;
                    }

                    @Override
                    public boolean touchUp(int x, int y, int pointer, int button) {
                        if(button == Input.Buttons.LEFT) {
                            lmbd = false;
                            leftMouseButton = false;
                            leftMouseButtonUp = true;

                            if(BuildingHandler.isBuilding) damageTileRequest = false;

                        }
                        if(button == Input.Buttons.RIGHT) {
                            rmbd = false;
                            rightMouseButton = false;
                            rightMouseButtonUp = true;

                            if(BuildingHandler.isBuilding) damageTileRequest = false;

                        }
                        return true;
                    }

                    public boolean keyJustPressed(int KeyCode) {
                        if(KeyCode < 0 || KeyCode >= keys.length) return false;
                        if(KeyCode == Settings.ZOOM_OUT_KEY && !jp[KeyCode]) zoomRequest -= 1f;
                        if(KeyCode == Settings.ZOOM_IN_KEY && !jp[KeyCode]) zoomRequest += 1f;
                        return true;
                    }

                    public boolean justClicked(int button) {
                        switch(button) {
                            case 0: return jcl;
                            case 1: return jcr;
                            default: return false;
                        }
                    }

                });
                break;

            case Android:
            case iOS:
                break;
		default:
			break;
        }
    }

    static int[][] mousePosTwoFrames = new int[2][2];
    static byte frameCounter = 0, frameCounter2 = 0;
    public static void tick() {
        frameCounter++;
        if(frameCounter == 1) {
            mousePosTwoFrames[0][0] = (int)(cursorX * UIObject.XSCALE); mousePosTwoFrames[0][1] = (int)(cursorY * UIObject.YSCALE);
        } else if (frameCounter == 2) {
            mousePosTwoFrames[1][0] = (int)(cursorX * UIObject.XSCALE); mousePosTwoFrames[1][1] = (int)(cursorY * UIObject.YSCALE);
            dx = mousePosTwoFrames[1][0] - mousePosTwoFrames[0][0];
            dy = mousePosTwoFrames[1][1] - mousePosTwoFrames[0][1];
            dMouse = Math.sqrt(Math.pow((mousePosTwoFrames[1][0] - mousePosTwoFrames[0][0]), 2) +
                    Math.pow((mousePosTwoFrames[1][1] - mousePosTwoFrames[0][1]), 2));
            frameCounter = 0;
            mouseCurrentlyMoving = dMouse != 0.0f;
        }

        if(rightMouseButtonUp || leftMouseButtonUp) {
            frameCounter2++;
            if(rightMouseButtonUp && frameCounter2 == 2) {
                rightMouseButtonUp = false;
                frameCounter2 = 0;
            } else if (leftMouseButtonUp && frameCounter2 == 2) {
                leftMouseButtonUp = false;
                frameCounter2 = 0;
            }
        }

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            for(int i = 0; i < keys.length; i++) {
                if(cp[i] && !keys[i]) cp[i] = false;
                else if (jp[i]) {
                    cp[i] = true; jp[i] = false;
                }
                if(!cp[i] && keys[i]) jp[i] = true;
            }
            if(ccl && !lmbd) ccl = false;
            else if (jcl) {
                ccl = true; jcl = false;
            }
            if(!ccl && lmbd) jcl = true;
            if(ccr && !rmbd) ccr = false;
            else if (jcr) {
                ccr = true; jcr = false;
            }
            if(!ccr && rmbd) jcr = true;

            leftMouseButton = lmbd;
            rightMouseButton = rmbd;
            leftMouseButtonDown = jcl;
            rightMouseButtonDown = jcr;
        }

    }
}
