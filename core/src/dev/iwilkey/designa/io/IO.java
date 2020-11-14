package dev.iwilkey.designa.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import dev.iwilkey.designa.world.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.Writer;

public class IO {

    public static boolean SaveGame(World world) {

        FileHandle clr = Gdx.files.local("game/");
        if(clr.exists()) clr.delete();
        FileHandle file = Gdx.files.local("game/");

        JSONObject data = new JSONObject();

        data.put("front-tiles", tilesToJSONArray(world.FRONT_TILES, world.WIDTH, world.HEIGHT));
        data.put("back-tiles", tilesToJSONArray(world.BACK_TILES, world.WIDTH, world.HEIGHT));

        Writer writer = file.writer(true);
        try {
            data.writeJSONString(writer);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
            return false;
        }

        return true;

    }

    public static JSONArray tilesToJSONArray(byte[][][] arr, int width, int height) {
        JSONArray array = new JSONArray();
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++)
                for(int z = 0; z < 2; z++)
                    array.add(x + ":" + y + ":" + z + ":" + arr[x][y][z]);
        return array;
    }

}
