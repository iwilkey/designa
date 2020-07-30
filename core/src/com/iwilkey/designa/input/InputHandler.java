package com.iwilkey.designa.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputHandler {

    // Controls
    public static boolean moveLeft, moveRight, jumpRequest;

    public static void initGameStateInput() {

        switch(Gdx.app.getType()) {

            case Desktop:
                Gdx.input.setInputProcessor(new InputAdapter(){

                    @Override
                    public boolean keyDown(int key) {

                        if(key == Input.Keys.D) moveRight = true;
                        if(key == Input.Keys.A) moveLeft = true;
                        if(key == Input.Keys.SPACE) jumpRequest = true;

                        return false;
                    }

                    @Override
                    public boolean keyUp(int key) {

                        if(key == Input.Keys.D) moveRight = false;
                        if(key == Input.Keys.A) moveLeft = false;
                        if(key == Input.Keys.SPACE) jumpRequest = false;

                        return false;
                    }

                });
                break;

            case iOS:
                break;

            case Android:
                break;

        }
    }
}
