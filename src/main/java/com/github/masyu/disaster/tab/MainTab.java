package com.github.masyu.disaster.tab;

import com.github.masyu.disaster.registry.ModBlocks;
import com.github.masyu.disaster.registry.ModItems;
import net.minecraft.world.item.Item;

public class MainTab {

    public static final Item[] items = {
            ModBlocks.BlockItems.INDESTRUCTIBLE_BLOCK.get(),
            ModBlocks.BlockItems.TELEPORTER.get(),
            ModBlocks.BlockItems.ALTAR_OF_NATURE.get(),
            ModItems.NATURE_GUARDIAN_SPAWN_EGG.get(),
            ModItems.DRYAD_SPAWN_EGG.get()
    };
}