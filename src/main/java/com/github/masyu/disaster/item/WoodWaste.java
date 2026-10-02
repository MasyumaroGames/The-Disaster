package com.github.masyu.disaster.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class WoodWaste extends Item {
    public WoodWaste() {
        super(new Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(64)
        );
    }
}
