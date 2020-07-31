package com.iwilkey.designa.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.iwilkey.designa.Game;
import com.iwilkey.designa.gfx.Camera;

public class InputHandler {

    // Class Vars
    private static boolean[] keys, jp, cp;
    private static boolean lmbd, rmbd, jcl, ccl, jcr, ccr;

    // Controls
    public static boolean moveLeft, moveRight, jumpRequest;
    public static float zoomRequest = 0;
    public static int cursorX, cursorY;
    public static boolean leftMouseButtonDown, rightMouseButtonDown; // iOS will trigger left version as pointer.
    public static boolean leftMouseButton, rightMouseButton;


    public InputHandler() {
        keys = new boolean[256];
        jp = new boolean[256];
        cp = new boolean[256];
    }

    public static void initGameStateInput() {

        switch(Gdx.app.getType()) {

            case Desktop:
                Gdx.input.setInputProcessor(new InputAdapter(){

                    @Override
                    public boolean keyDown(int key) {

                        keyJustPressed(key);

                        if(key == Input.Keys.D) moveRight = true;
                        if(key == Input.Keys.A) moveLeft = true;
                        if(key == Input.Keys.SPACE) jumpRequest = true;


                        return true;
                    }

                    @Override
                    public boolean keyUp(int key) {

                        if(key == Input.Keys.D) moveRight = false;
                        if(key == Input.Keys.A) moveLeft = false;
                        if(key == Input.Keys.SPACE) jumpRequest = false;

                        return true;
                    }

                    @Override
                    public boolean scrolled(int amount) {
                        //zoomRequest += amount / Camera.scrollSensitivity;
                        return true;
                    }

                    @Override
                    public boolean mouseMoved(int x, int y) {
                        cursorX = x; cursorY = Game.h - y;
                        return true;
                    }

                    @Override
                    public boolean touchDown(int x, int y, int pointer, int button) {
                        justClicked(button);
                        if(button == Input.Buttons.LEFT) {
                            lmbd = true;
                        }
                        if(button == Input.Buttons.RIGHT) {
                            rmbd = true;
                        }
                        return true;
                    }

                    @Override
                    public boolean touchUp(int x, int y, int pointer, int button) {
                        if(button == Input.Buttons.LEFT) lmbd = false;
                        if(button == Input.Buttons.RIGHT) rmbd = false;
                        return true;
                    }

                    // 'Just' methods
                    public boolean keyJustPressed(int KeyCode) {
                        if(KeyCode < 0 || KeyCode >= keys.length) return false;
                        if(KeyCode == Input.Keys.E && !jp[KeyCode]) zoomRequest -= 0.1f;
                        if(KeyCode == Input.Keys.Q && !jp[KeyCode]) zoomRequest += 0.1f;
                        return true;
                    }

                    public boolean justClicked(int button) {
                        switch(button) {
                            case 0:
                                return jcl;

                            case 1:
                                return jcr;

                            default:
                                return false;
                        }
                    }

                });
                break;

            case iOS:
                break;

            case Android:
                break;

        }
    }

    public void tick() {
       switch(Gdx.app.getType()) {
           case Desktop:

               for(int i = 0; i < keys.length; i++) {
                   if(cp[i] && !keys[i]) {
                       cp[i] = false;
                   } else if (jp[i]) {
                       cp[i] = true;
                       jp[i] = false;
                   }

                   if(!cp[i] && keys[i]) {
                       jp[i] = true;
                   }
               }

               if(ccl && !lmbd) {
                   ccl = false;
               } else if (jcl) {
                   ccl = true;
                   jcl = false;
               }

               if(!ccl && lmbd) {
                   jcl = true;
               }

               if(ccr && !rmbd) {
                   ccr = false;
               } else if (jcr) {
                   ccr = true;
                   jcr = false;
               }

               if(!ccr && rmbd) {
                   jcr = true;
               }

               leftMouseButton = lmbd;
               rightMouseButton = rmbd;
               leftMouseButtonDown = jcl;
               rightMouseButtonDown = jcr;

               break;

           case iOS:
               break;

           case Android:
               break;

       }
    }
}
