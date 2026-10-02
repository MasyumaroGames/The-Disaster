package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.Disaster;

import com.github.masyu.disaster.item.WoodWaste;
import com.github.masyu.disaster.item.eye.GreatTreeEyeItem;
import com.github.masyu.disaster.item.records.MagnificentNature;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Disaster.MODID);


    /**
     * Item
     */

    // Wood Waste
    public static final RegistryObject<Item> WOOD_WASTE = ITEMS.register("wood_waste", WoodWaste::new);


    /**
     * Records
     */

    // Nature Guadian
    public static final RegistryObject<Item> MAGNIFICENT_NATURE = ITEMS.register("magnificent_nature", MagnificentNature::new);

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

    /**
     * Eye
     */

    // Nature Guadian
    public static final RegistryObject<Item> GREAT_TREE_EYE = ITEMS.register("great_tree_eye",
            () -> new GreatTreeEyeItem(new Item.Properties().stacksTo(1)));
}
