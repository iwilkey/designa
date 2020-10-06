package com.iwilkey.designa.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.iwilkey.designa.Game;
import com.iwilkey.designa.GameBuffer;
import com.iwilkey.designa.assets.Assets;
import com.iwilkey.designa.defense.WeaponHandler;
import com.iwilkey.designa.entities.EntityHandler;
import com.iwilkey.designa.entities.creature.passive.Player;
import com.iwilkey.designa.gfx.Camera;
import com.iwilkey.designa.gfx.LightManager;
import com.iwilkey.designa.gui.Hud;
import com.iwilkey.designa.inventory.Inventory;
import com.iwilkey.designa.inventory.InventorySlot;
import com.iwilkey.designa.inventory.crate.Crate;
import com.iwilkey.designa.items.Item;
import com.iwilkey.designa.items.ItemHandler;
import com.iwilkey.designa.machines.MachineHandler;
import com.iwilkey.designa.machines.MachineType;
import com.iwilkey.designa.particle.ParticleHandler;
import com.iwilkey.designa.tiles.Tile;
import com.iwilkey.designa.utils.Utils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class World {

    private final GameBuffer gb;
    public static int w, h;
    public static String dirpath = "";

    // Rendering
    public static int[][] tiles, backTiles,
            tileBreakLevel,
            backTileBreakLevel, lightMap,
            origLightMap;
    public static int[] origHighTiles, origHighBackTiles;
    public static ArrayList<Integer> trees, treesY;
    public static ParticleHandler particleHandler;

    // Entities
    private static EntityHandler entityHandler = null;

    // Items
    private static ItemHandler itemHandler;

    // Light
    private final LightManager lightManager;

    // Environment
    private final AmbientCycle ambientCycle;

    // Machines
    private final MachineHandler machineHandler;

    // Weapons
    private final WeaponHandler weaponHandler;

    public World(GameBuffer gb, String path) {
        this.gb = gb;
        lightManager = new LightManager(gb, this);
        ambientCycle = new AmbientCycle(this, gb);
        entityHandler = new EntityHandler(new Player(gb, 0,
                0));
        itemHandler = new ItemHandler(gb);
        machineHandler = new MachineHandler(gb);
        weaponHandler = new WeaponHandler();
        particleHandler = new ParticleHandler();

        loadWorld(path);

        giveItem(Assets.blasterBaseItem, 12);
        giveItem(Assets.simpleBlasterItem, 12);
        giveItem(Assets.copperPelletItem, 64);
        giveItem(Assets.copperPelletItem, 64);

        // entityHandler.addEntity(new Npc(gb, ((w / 2f) + 1) * Tile.TILE_SIZE, (LightManager.highestTile[((w / 2) + 1)]) * Tile.TILE_SIZE));
        // entityHandler.addEntity(new TerraBot(gb, ((w / 2f) + 2) * Tile.TILE_SIZE, (LightManager.highestTile[((w / 2) + 2)]) * Tile.TILE_SIZE));
        // entityHandler.addEntity(new TerraBot(gb, ((w / 2f) - 2) * Tile.TILE_SIZE, (LightManager.highestTile[((w / 2) - 2)]) * Tile.TILE_SIZE));
    }

    public void tick() {
        if(!Hud.gameMenu) {
            ambientCycle.tick();
            itemHandler.tick();
            entityHandler.tick();
            machineHandler.tick();
            weaponHandler.tick();
            particleHandler.tick();
            for (WorldGeneration.Cloud cloud : WorldGeneration.clouds) cloud.tick();
        }
    }

    private void giveItem(Item item, int amount) {
        Inventory inv = entityHandler.getPlayer().getInventory();
        for(int ii = 0; ii < amount; ii++) inv.addItem(item);
    }

    public void render(Batch b) {

        int xStart = (int) Math.max(0, ((-Camera.position.x / Camera.scale.x) / Tile.TILE_SIZE) - 1);
        int xEnd = (int) Math.min(w, ((((-Camera.position.x + Game.w) / Camera.scale.x) / Tile.TILE_SIZE) + 4));
        int yStart = (int) Math.max(0, ((-Camera.position.y / Camera.scale.y) / Tile.TILE_SIZE) - 1);
        int yEnd = (int) Math.min(h, ((((-Camera.position.y + Game.h) / Camera.scale.y) / Tile.TILE_SIZE) + 4));
        for(int y = yStart; y < yEnd; y++) {
            for(int x = xStart; x < xEnd; x++) {
                int xx = x * Tile.TILE_SIZE;
                int yy = y * Tile.TILE_SIZE;
                ambientCycle.render(b, xx, yy);
            }
        }

        // Draw environment
        for(WorldGeneration.Cloud cloud : WorldGeneration.clouds) cloud.render(b);
        // for(WorldGeneration.Mountain mountain : WorldGeneration.mountains) mountain.render(b);

        for(int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                int xx = x * Tile.TILE_SIZE;
                int yy = y * Tile.TILE_SIZE;
                if(yy < (origHighTiles[x]) * Tile.TILE_SIZE) b.draw(Assets.backDirt, xx, yy, 16, 16);
                if(yy < (origHighBackTiles[x]) * Tile.TILE_SIZE) b.draw(Assets.backDirt, xx, yy, 16, 16);
                getBackTile(x, y).renderBackTile(b, xx, yy, backTileBreakLevel[x][(h - y) - 1], getBackTile(x, y).getID());
            }
        }

        entityHandler.staticRender(b);
        itemHandler.render(b);
        machineHandler.render(b);

        for(int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                int xx = x * Tile.TILE_SIZE;
                int yy = y * Tile.TILE_SIZE;
                getTile(x, y).renderAmbientLight(b, xx, yy);
                getTile(x, y).render(b, xx, yy, tileBreakLevel[x][(h - y) - 1], getTile(x, y).getID());
                getTile(x, y).tick();
            }
        }

        entityHandler.npcRender(b);
        entityHandler.playerRender(b);
        weaponHandler.render(b);

        for(int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                lightManager.renderLight(b, x, y);
            }
        }

        entityHandler.getPlayer().getBuildingHandler().render(b);

        particleHandler.render(b);

    }

    public static Tile getTile(int x, int y) {
        if(x < 0 || y < 0 || x >= w || y >= h) return Assets.airTile;
        Tile t = Tile.tiles[tiles[x][Math.abs(h - y) - 1]];
        if(t == null) return Assets.airTile;
        return t;
    }

    public Tile getBackTile(int x, int y) {
        if(x < 0 || y < 0 || x >= w || y >= h) return Assets.airTile;
        Tile t = Tile.tiles[backTiles[x][Math.abs(h - y) - 1]];
        if(t == null) return Assets.airTile;
        return t;
    }

    private void loadWorld(String path) {
        dirpath = path;

        // Init front tiles
        String ft = Utils.loadFileAsString(path + "ft.dsw");
        String[] ftTokens = ft.split("\\s+");
        w = Utils.parseInt(ftTokens[0]);
        h = Utils.parseInt(ftTokens[1]);
        tiles = new int[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int id = Utils.parseInt(ftTokens[x + y * w + 2]);
                tiles[x][y] = id;
                if(id == Assets.torchTile.getID()) lightManager.addLight(x, h - y - 1, 8);
            }
        }

        // Init front back tiles
        String ftblf = Utils.loadFileAsString(path + "ftbl.dsw");
        String[] ftblTokens = ftblf.split("\\s+");
        tileBreakLevel = new int[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int s = Utils.parseInt(ftblTokens[x + y * w + 2]);
                tileBreakLevel[x][y] = s;
            }
        }

        // Init back tiles
        String btf = Utils.loadFileAsString(path + "bt.dsw");
        String[] btTokens = btf.split("\\s+");
        backTiles = new int[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int bid = Utils.parseInt(btTokens[x + y * w + 2]);
                backTiles[x][y] = bid;
            }
        }

        // Init back tile break level
        String btblf = Utils.loadFileAsString(path + "btbl.dsw");
        String[] btblTokens = btblf.split("\\s+");
        backTileBreakLevel = new int[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int btbl = Utils.parseInt(btblTokens[x + y * w + 2]);
                backTileBreakLevel[x][y] = btbl;
            }
        }

        // If it exits, init front high tiles
        FileHandle fhtf = Gdx.files.local(path + "fht.dsw");
        origHighTiles = new int[w];
        LightManager.highestTile = new int[w];
        if(fhtf.exists()) {
            // Init from file
            String fht = Utils.loadFileAsString(path + "fht.dsw");
            String[] fhtTokens = fht.split("\\s+");
            for(int x = 0; x < World.w; x++) {
                int t = Utils.parseInt(fhtTokens[x]);
                origHighTiles[x] = t;
                LightManager.highestTile[x] = t;
            }
        } else {
            // Init high tiles normally
            origHighTiles = LightManager.findHighestTiles();
        }

        // Same with back
        FileHandle bhtf = Gdx.files.local(path + "bht.dsw");
        origHighBackTiles = new int[w];
        if(bhtf.exists()) {
            // Init from file
            String bht = Utils.loadFileAsString(path + "bht.dsw");
            String[] bhtTokens = bht.split("\\s+");
            for(int x = 0; x < World.w; x++) {
                int t = Utils.parseInt(bhtTokens[x]);
                origHighBackTiles[x] = t;
                LightManager.highestBackTile = new int[World.w];
                LightManager.highestBackTile[x] = t;
            }
        } else {
            // Init high tiles normally
            origHighBackTiles = LightManager.findHighestBackTiles();
        }

        // Init light map
        lightMap = new int[w][h];
        origLightMap = new int[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                lightMap[x][y] = 0;
                origLightMap[x][y] = 0;
            }
        }

        lightMap = lightManager.buildAmbientLight(lightMap);
        lightManager.bakeLighting();

        // Continue generating world, if it's the first time
        String invPath = path + "inv.dsw";
        FileHandle invf = Gdx.files.local(invPath);
        if(!invf.exists()) { // First load
            trees = WorldGeneration.EnvironmentCreation(gb, entityHandler);
            WorldGeneration.OreGeneration(); // If we're loading a world, we don't need to generate ores.
            entityHandler.getPlayer().setX((w / 2f) * Tile.TILE_SIZE);
            entityHandler.getPlayer().setY((LightManager.highestTile[(w / 2)]) * Tile.TILE_SIZE);
        } else {
            // Load in the trees
            String tree = Utils.loadFileAsString(path + "tr.dsw");
            String[] treeTokens = tree.split("\\s+");
            trees = new ArrayList<Integer>();
            treesY = new ArrayList<Integer>();
            for (String treeToken : treeTokens) {
                String[] subTokens = treeToken.split("-");
                int x = Utils.parseInt(subTokens[0]);
                int y = Utils.parseInt(subTokens[1]);
                trees.add(x); treesY.add(y);
            }
            WorldGeneration.EnvironmentReformation(gb, entityHandler, trees, treesY);

            // Load in inventory, if it's not the first time
            String inv = Utils.loadFileAsString(path + "inv.dsw");
            String[] invTokens = inv.split("\\s+");
            for(int s = 0; s < invTokens.length; s++) {
                if(s > 1) {
                    String[] further = invTokens[s].split("-");
                    int itemID = Utils.parseInt(further[0]);
                    int count = Utils.parseInt(further[1]);
                    giveItem(Item.getItemByID(itemID), count);
                } else {
                    System.out.println(invTokens[0]);
                    entityHandler.getPlayer().setX(Utils.parseInt(invTokens[0]) * Tile.TILE_SIZE);
                    entityHandler.getPlayer().setY(Utils.parseInt(invTokens[1]) * Tile.TILE_SIZE);
                }
            }

            // Load in all machines
                // Drills
                String drillPath = path + "machines/drl.dsw";
                FileHandle drillF = Gdx.files.local(drillPath);
                if(drillF.exists()) {
                    String drills = Utils.loadFileAsString(drillPath);
                    String[] drillTokens = drills.split("\\s+");
                    int x, y, rt, mr;
                    for (int s = 0; s < drillTokens.length; s += 4) {
                        x = Utils.parseInt(drillTokens[s]);
                        y = Utils.parseInt(drillTokens[s + 1]);
                        rt = Utils.parseInt(drillTokens[s + 2]);
                        mr = Utils.parseInt(drillTokens[s + 3]);

                        Tile miningResource;
                        MachineType.MechanicalDrill.ResourceType resourceType;

                        switch (rt) {
                            case 0:
                            default:
                                resourceType = MachineType.MechanicalDrill.ResourceType.COPPER;
                                break;
                            case 1:
                                resourceType = MachineType.MechanicalDrill.ResourceType.SILVER;
                                break;
                            case 2:
                                resourceType = MachineType.MechanicalDrill.ResourceType.IRON;
                                break;
                            case 3:
                                resourceType = MachineType.MechanicalDrill.ResourceType.DIAMOND;
                                break;
                        }

                        miningResource = Tile.tiles[mr];

                        MachineHandler.addMechanicalDrill(x, y, miningResource, resourceType);

                    }
                }

                // Pipes
                String pipePath = path + "machines/pip.dsw";
                FileHandle pipeF = Gdx.files.local(pipePath);
                if(pipeF.exists()) {
                    String pipes = Utils.loadFileAsString(pipePath);
                    String[] pipeTokens = pipes.split("\\s+");

                    int x = 0; int y = 0; int dir = 0;
                    for (int s = 0; s < pipeTokens.length; s += 3) {
                        x = Utils.parseInt(pipeTokens[s]);
                        y = Utils.parseInt(pipeTokens[s + 1]);
                        dir = Utils.parseInt(pipeTokens[s + 2]);
                        MachineHandler.addPipe(x, y, dir);
                    }
                }

                // Nodes
                String nodePath = path + "machines/nde.dsw";
                FileHandle nodeF = Gdx.files.local(nodePath);
                if(nodeF.exists()) {
                    String nodes = Utils.loadFileAsString(nodePath);
                    String[] nodeTokens = nodes.split("\\s+");

                    int x = 0, y = 0;
                    for (int s = 0; s < nodeTokens.length; s += 2) {
                        x = Utils.parseInt(nodeTokens[s]);
                        y = Utils.parseInt(nodeTokens[s + 1]);
                        MachineHandler.addNode(x, y);
                    }
                }

                // Assemblers
                String assemblerPath = path + "machines/asblr.dsw";
                FileHandle assemblerF = Gdx.files.local(assemblerPath);
                if(assemblerF.exists()) {
                    String assemblers = Utils.loadFileAsString(assemblerPath);
                    String[] assemblerTokens = assemblers.split("\\s+");

                    int x = 0, y = 0, itemID = 0;
                    for(int s = 0; s < assemblerTokens.length; s += 3) {
                        x = Utils.parseInt(assemblerTokens[s]);
                        y = Utils.parseInt(assemblerTokens[s + 1]);
                        itemID = Utils.parseInt(assemblerTokens[s + 2]);

                        if(itemID == -1) MachineHandler.addAssembler(x, y);
                        else {
                            Item item = Item.getItemByID(itemID);
                            MachineHandler.addAssembler(x, y, item);
                        }
                    }
                }

            // Load in all crates
            String relpath = dirpath + "crates/";
            FileHandle cratesDir = Gdx.files.local(relpath);
            if(cratesDir.exists()) {
                int crateNum = -1;
                for(FileHandle crateFH : cratesDir.list()) {
                    crateNum++;
                    String crt = Utils.loadFileAsString(crateFH.path());
                    String[] crtTokens = crt.split("\\s+");
                    for(int s = 0; s < crtTokens.length; s++) {
                        if(s > 1) {
                            String[] further = crtTokens[s].split("-");
                            int itemID = Utils.parseInt(further[0]);
                            int count = Utils.parseInt(further[1]);
                            for(int i = 0; i < count; i++)
                                entityHandler.getPlayer().crates.get(crateNum).addItem(Item.getItemByID(itemID));
                        } else if (s == 1) {
                            entityHandler.getPlayer().addCrate(Utils.parseInt(crtTokens[0]), Utils.parseInt(crtTokens[1]));
                            entityHandler.getPlayer().crates.get(entityHandler.getPlayer().crates.size() - 1).setActive(false);
                        }
                    }
                }
            }
        }
    }

    public void saveWorld() {
        // Save front tiles
        String ftPath = dirpath + "ft.dsw";
        FileHandle fh = Gdx.files.local(ftPath);
        fh.delete();
        FileHandle ftf = Gdx.files.local(ftPath);
        if(!writeFT(ftf, w, h)) System.exit(-1);

        // Save front tile break level
        String ftblPath = dirpath + "ftbl.dsw";
        FileHandle fhbl = Gdx.files.local(ftblPath);
        fhbl.delete();
        FileHandle ftblf = Gdx.files.local(ftblPath);
        if(!writeFTBL(ftblf, w, h)) System.exit(-1);

        // Save back tiles
        String btPath = dirpath + "bt.dsw";
        FileHandle bt = Gdx.files.local(btPath);
        bt.delete();
        FileHandle btf = Gdx.files.local(btPath);
        if(!writeBT(btf, w, h)) System.exit(-1);

        // Save back tiles break level
        String btblPath = dirpath + "btbl.dsw";
        FileHandle btbl = Gdx.files.local(btblPath);
        btbl.delete();
        FileHandle btblf = Gdx.files.local(btblPath);
        if(!writeBTBL(btblf, w, h)) System.exit(-1);

        // Save front high tiles
        String fhtPath = dirpath + "fht.dsw";
        FileHandle fhtf = Gdx.files.local(fhtPath);
        if(fhtf.exists()) {
            fhtf.delete();
            FileHandle fhtf2 = Gdx.files.local(fhtPath);
            if(!writeFHT(fhtf2, w)) System.exit(-1);
        } else {
            if(!writeFHT(fhtf, w)) System.exit(-1);
        }

        // Save back high tiles
        String bhtPath = dirpath + "bht.dsw";
        FileHandle bhtf = Gdx.files.local(bhtPath);
        if(bhtf.exists()) {
            bhtf.delete();
            FileHandle bhtf2 = Gdx.files.local(bhtPath);
            if(!writeBHT(bhtf2, w)) System.exit(-1);
        } else {
            if(!writeBHT(bhtf, w)) System.exit(-1);
        }

        // Save the trees XD
        String trPath = dirpath + "tr.dsw";
        FileHandle tr = Gdx.files.local(trPath);
        tr.delete();
        FileHandle trf = Gdx.files.local(trPath);
        if(!writeTR(trf, w)) System.exit(-1);

        // Save inventory
        String invPath = dirpath + "inv.dsw";
        FileHandle invf = Gdx.files.local(invPath);
        if(invf.exists()) {
            invf.delete();
            FileHandle invf2 = Gdx.files.local(invPath);
            if(!writeINV(invf2)) System.exit(-1);
        } else if(!writeINV(invf)) System.exit(-1);

        // Save crates
        ArrayList<Crate> crates = entityHandler.getPlayer().crates;
        for(int i = 0; i < crates.size(); i++) {
            String cratePath = dirpath + "crates/c" + i + ".dsw";
            FileHandle crateF = Gdx.files.local(cratePath);
            if(crateF.exists()) crateF.delete();
            if(!writeCRATE(crateF, crates.get(i))) System.exit(-1);
        }

        // Save machines
            // Save drills
            ArrayList<MachineType.MechanicalDrill> drills = MachineHandler.drills;
            if(drills.size() != 0) {
                String drillPath = dirpath + "machines/drl.dsw";
                FileHandle drillF = Gdx.files.local(drillPath);
                drillF.delete();
                FileHandle drillFF = Gdx.files.local(drillPath);
                if (!writeDRILL(drillFF, drills)) System.exit(-1);
            }

            // Save pipes
            ArrayList<MachineType.Pipe> pipes = MachineHandler.pipes;
            if(pipes.size() != 0) {
                String pipePath = dirpath + "machines/pip.dsw";
                FileHandle pipeF = Gdx.files.local(pipePath);
                pipeF.delete();
                FileHandle pipeFF = Gdx.files.local(pipePath);
                if (!writePIPE(pipeFF, pipes)) System.exit(-1);
            }

            // Save nodes
            ArrayList<MachineType.Node> nodes = MachineHandler.nodes;
            if(nodes.size() != 0) {
                String nodePath = dirpath + "machines/nde.dsw";
                FileHandle nodeF = Gdx.files.local(nodePath);
                nodeF.delete();
                FileHandle nodeFF = Gdx.files.local(nodePath);
                if (!writeNODE(nodeFF, nodes)) System.exit(-1);
            }

            // Save assemblers
            ArrayList<MachineType.Assembler> assemblers = MachineHandler.assemblers;
            if(assemblers.size() != 0) {
                String assemblerPath = dirpath + "machines/asblr.dsw";
                FileHandle assemblerF = Gdx.files.local(assemblerPath);
                assemblerF.delete();
                FileHandle assemblerFF = Gdx.files.local(assemblerPath);
                if (!writeASSEMBLER(assemblerFF, assemblers)) System.exit(-1);
            }

    }

    private static boolean writeFT(FileHandle fh, int width, int height) {
        try {
            Writer w = fh.writer(true);
            w.write(width + " " + height + "\n");

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int num = tiles[x][y];
                    w.write(num + " ");
                }
                w.write("\n");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeFTBL(FileHandle ftblf, int width, int height) {
        try {
            Writer w = ftblf.writer(true);
            w.write(width + " " + height + "\n");

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int num = tileBreakLevel[x][y];
                    w.write(num + " ");
                }
                w.write("\n");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeBT(FileHandle ftf, int width, int height) {
        try {
            Writer w = ftf.writer(true);
            w.write(width + " " + height + "\n");

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int num = backTiles[x][y];
                    w.write(num + " ");
                }
                w.write("\n");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeBTBL(FileHandle ftblf, int width, int height) {
        try {
            Writer w = ftblf.writer(true);
            w.write(width + " " + height + "\n");

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int num = backTileBreakLevel[x][y];
                    w.write(num + " ");
                }
                w.write("\n");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeFHT(FileHandle ftblf, int width) {
        try {
            Writer w = ftblf.writer(true);

            for (int x = 0; x < width; x++) {
                int num = origHighTiles[x];
                w.write(num + " ");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeBHT(FileHandle ftblf, int width) {
        try {
            Writer w = ftblf.writer(true);

            for (int x = 0; x < width; x++) {
                int num = origHighBackTiles[x];
                w.write(num + " ");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeTR(FileHandle ftblf, int width) {
        try {
            Writer w = ftblf.writer(true);

            for (int num : trees) w.write(num + "-" + origHighTiles[num] + " ");

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeINV(FileHandle ftblf) {
        try {
            Writer w = ftblf.writer(true);
            w.write((int)(entityHandler.getPlayer().getX() / Tile.TILE_SIZE) + " " +
                    (int)(entityHandler.getPlayer().getY() / Tile.TILE_SIZE));
            w.write("\n");

            for(int y = 0; y < 400 / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < 200 / InventorySlot.SLOT_WIDTH; x++) {
                    Item i = null;
                    int count = 0;
                    try {
                        if (Inventory.slots[x][y].getItem() != null) {
                            i = Inventory.slots[x][y].getItem();
                            count = Inventory.slots[x][y].itemCount;
                            w.write(i.getItemID() + "-" + count + " ");
                        } else {
                            w.write("0-0 ");
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                }
                w.write("\n");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeCRATE(FileHandle ftblf, Crate crate) {
        try {
            Writer w = ftblf.writer(true);
            w.write(crate.getX() + " " + crate.getY()); // Tile coords
            w.write("\n");

            for(int y = 0; y < 400 / InventorySlot.SLOT_HEIGHT; y++) {
                for (int x = 0; x < 400 / InventorySlot.SLOT_WIDTH; x++) {
                    Item i = null;
                    int count = 0;
                    try {
                        if (crate.storage[x][y].getItem() != null) {
                            i = crate.storage[x][y].getItem();
                            count = crate.storage[x][y].itemCount;
                            w.write(i.getItemID() + "-" + count + " ");
                        } else {
                            w.write("0-0 ");
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                }
                w.write("\n");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeDRILL(FileHandle ftblf, ArrayList<MachineType.MechanicalDrill> drills) {
        try {
            Writer w = ftblf.writer(true);
            for(MachineType.MechanicalDrill drill : drills) {
                int rt; int mr;

                switch(drill.resourceType) {
                    case COPPER:
                    default: rt = 0; break;
                    case SILVER: rt = 1; break;
                    case IRON: rt = 2; break;
                    case DIAMOND: rt = 3; break;
                }

                mr = drill.miningResource.getID();

                w.write(drill.x + " " + drill.y + " " + rt + " " + mr + "\n");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writePIPE(FileHandle ftblf, ArrayList<MachineType.Pipe> pipes) {
        try {

            Writer w = ftblf.writer(true);
            for(MachineType.Pipe pipe : pipes) {
                int orientation;
                switch(pipe.direction) {
                    case RIGHT:
                    default: orientation = 0; break;
                    case DOWN: orientation = 1; break;
                    case LEFT: orientation = 2; break;
                    case UP: orientation = 3; break;
                }
                w.write(pipe.x + " " + pipe.y + " " + orientation + "\n");
            }

            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static boolean writeNODE(FileHandle ftblf, ArrayList<MachineType.Node> nodes) {
        try {
            Writer w = ftblf.writer(true);
            for(MachineType.Node node : nodes) w.write(node.x + " " + node.y + "\n");
            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean writeASSEMBLER(FileHandle ftblf, ArrayList<MachineType.Assembler> assemblers) {
        try {
            Writer w = ftblf.writer(true);
            for(MachineType.Assembler assembler : assemblers) {
                Item item = null;
                if(assembler.targetRecipe != null) item = assembler.targetRecipe.item;
                if(item != null) {
                    w.write(assembler.x + " " + assembler.y + " " + item.getItemID() + "\n");
                } else w.write(assembler.x + " " + assembler.y + " " + -1 + "\n");
            }
            w.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void bake(int[][] lm) { lightMap = lm; }
    public GameBuffer getGameBuffer() { return gb; }
    public static ItemHandler getItemHandler() { return itemHandler; }
    public MachineHandler getMachineHandler() { return machineHandler; }
    public static EntityHandler getEntityHandler() { return entityHandler; }
    public int[][] getOrigLightMap() { return this.origLightMap; }
    public LightManager getLightManager() { return lightManager; }
    public AmbientCycle getAmbientCycle() { return ambientCycle; }

}
