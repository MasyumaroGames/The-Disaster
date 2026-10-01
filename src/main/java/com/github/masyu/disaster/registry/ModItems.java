package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.Disaster;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Disaster.MODID);


    /**
     * Spawn Egg
     */

    // Dryad
    public static final RegistryObject<Item> DRYAD_SPAWN_EGG =
            ITEMS.register(
                    "dryad_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.DRYAD,
                            0xB7F5C,
                            0xb3957d,
                            new Item.Properties()
                    )
            );

    // Nature Guardian
    public static final RegistryObject<Item> NATURE_GUARDIAN_SPAWN_EGG =
            ITEMS.register(
                    "nature_guardian_spawn_egg",
                    () -> new ForgeSpawnEggItem(
                            ModEntities.NATURE_GUARDIAN,
                            0x3A5F2D,
                            0x7CB342,
                            new Item.Properties()
                    )
            );
}
