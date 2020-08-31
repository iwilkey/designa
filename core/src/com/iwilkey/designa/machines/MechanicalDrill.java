package com.iwilkey.designa.machines;

import com.iwilkey.designa.tiles.Tile;

public class MechanicalDrill {

    public static String[] resourceTypeList = {
            "copper", "silver", "iron", "diamond"
    };

    public Tile miningResource;
    public String resourceType;

    public MechanicalDrill(Tile miningResource, int resourceType) {
        this.resourceType = resourceTypeList[resourceType];
        this.miningResource = miningResource;
    }

    public void tick() {

    }

}
