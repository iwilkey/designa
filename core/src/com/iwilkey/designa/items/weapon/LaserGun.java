package com.iwilkey.designa.items.weapon;

import com.iwilkey.designa.items.ItemRecipe;
import com.iwilkey.designa.items.ItemType;

public class LaserGun extends ItemType.CreatableItem.Weapon.Gun {

    public LaserGun(String name, int strength, ItemRecipe ir) {
        super(name, strength, ir);
    }

    @Override
    public void fire() {

    }

    @Override
    public void reload() {

    }

}
