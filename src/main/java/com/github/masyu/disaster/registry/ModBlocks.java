package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.Disaster;
import com.github.masyu.disaster.block.IndestructibleBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static class Blocks {

        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Disaster.MODID);

        public static final RegistryObject<Block> INDESTRUCTIBLE_BLOCK = BLOCKS.register("indestructible_block", IndestructibleBlock::new);
    }

    //BLockState
    public static class BlockItems{

        public static final DeferredRegister<Item> BLOCKS_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Disaster.MODID);

        public static final RegistryObject<Item> INDESTRUCTIBLE_BLOCK = BLOCKS_ITEMS.register("indestructible_block", () -> new BlockItem(Blocks.INDESTRUCTIBLE_BLOCK.get(), new Item.Properties().rarity(Rarity.RARE).stacksTo(64)));

    }
}
