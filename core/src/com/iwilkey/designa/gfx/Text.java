package com.iwilkey.designa.gfx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.iwilkey.designa.assets.Assets;

import java.util.ArrayList;
import java.util.List;

public class Text {

    private final static String sym = "abcdefghijklmnopqrstuvwxyz 0123456789.!?,'";
    private final static char[] chars = sym.toCharArray();

    public static List<String> runtimeMessages = new ArrayList<String>();
    public static List<TextureRegion[]> allProcessedMessages = new ArrayList<TextureRegion[]>();

    // TODO: \n makes a new line. Lowers the rendering forloop y by size * 1.12

    private static TextureRegion[] process(String mes) {
        mes = mes.toLowerCase();
        int[] letterIndex = new int[mes.length()];
        char[] message = mes.toCharArray();

        int in = 0;
        for(int i = 0; i < message.length; i++) {
            char c = message[i];
            for(int l = 0; l < chars.length; l++) {
                if(c == chars[l]) {
                    letterIndex[in] = l;
                    in++;
                }
            }
        }

        TextureRegion[] preconcat = new TextureRegion[mes.length()];
        for(int i = 0; i < letterIndex.length; i++) {
            Texture letter = new Texture(8, 8, Pixmap.Format.RGBA8888);
            TextureRegion tr = new TextureRegion(letter, 0, 0, letter.getWidth(), letter.getHeight());
            tr = Assets.font[letterIndex[i]];
            preconcat[i] = tr;
        }

        return preconcat;
    }

    public static void draw(Batch b, String message, int x, int y) {

        if(runtimeMessages.contains(message)) {
            int index = runtimeMessages.indexOf(message);
            for(int i = 0; i < message.length(); i++) {
                b.draw(allProcessedMessages.get(index)[i], x + (i * 9), y);
            }
            return;
        }

        TextureRegion[] phrase = process(message);

        recordMessage(message, phrase);

        for(int i = 0; i < message.length(); i++) {
            b.draw(phrase[i], x + (i * 9), y);
        }

    }

    // Override for above draw, just with size
    public static void draw(Batch b, String message, int x, int y, int size) {

        if(runtimeMessages.contains(message)) {
            int index = runtimeMessages.indexOf(message);
            for(int i = 0; i < message.length(); i++) {
                b.draw(allProcessedMessages.get(index)[i], x + (i * (int)(size + (1 * 1.12f))), y, size, size);
            }
            return;
        }

        TextureRegion[] phrase = process(message);

        recordMessage(message, phrase);

        for(int i = 0; i < message.length(); i++) {
            b.draw(phrase[i], x + (i * (int)(size + (1 * 1.12f))), y, size, size);
        }
    }

    private static void recordMessage(String mes, TextureRegion[] pm) {
        runtimeMessages.add(mes);
        allProcessedMessages.add(pm);
    }

}
