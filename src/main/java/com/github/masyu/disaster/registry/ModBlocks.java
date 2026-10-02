package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.Disaster;
import com.github.masyu.disaster.block.EntanglingRootBlock;
import com.github.masyu.disaster.block.IndestructibleBlock;
import com.github.masyu.disaster.block.NatureGuardianAltarBlock;
import com.github.masyu.disaster.block.teleportblock.TeleportBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static class Blocks {

        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Disaster.MODID);

        // 不可壊ブロック
        public static final RegistryObject<Block> INDESTRUCTIBLE_BLOCK = BLOCKS.register("indestructible_block", IndestructibleBlock::new);

        // 根
        public static final RegistryObject<Block> ENTANGLING_ROOT = BLOCKS.register("entangling_root",
                () -> new EntanglingRootBlock(BlockBehaviour.Properties.of()
                        .noCollission()
                        .instabreak()
                        .noOcclusion()
                        .sound(SoundType.WOOD)
                        .noLootTable() // ドロップ不要なら
                )
        );

        public static final RegistryObject<Block> ALTAR_OF_NATURE = BLOCKS.register("altar_of_nature",
                () -> new NatureGuardianAltarBlock(BlockBehaviour.Properties.of()
                        .strength(50.0F, 1200.0F) // 通常は壊せない硬さにしておく(プレイヤーが先に壊すのを防ぐ)
                        .sound(SoundType.WOOD)
                )
        );

        public static final RegistryObject<TeleportBlock> TELEPORTER = BLOCKS.register("teleporter",
                () -> new TeleportBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)
                        .strength(-1,3600000).explosionResistance(1000000000000000000000000000F).noOcclusion()));

    }

    // BlockItems
    public static class BlockItems{

        public static final DeferredRegister<Item> BLOCKS_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Disaster.MODID);

        public static final RegistryObject<Item> INDESTRUCTIBLE_BLOCK = BLOCKS_ITEMS.register("indestructible_block", () -> new BlockItem(Blocks.INDESTRUCTIBLE_BLOCK.get(), new Item.Properties().rarity(Rarity.RARE).stacksTo(64)));

        public static final RegistryObject<Item> TELEPORTER = BLOCKS_ITEMS.register("teleporter", () -> new BlockItem(Blocks.TELEPORTER.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(64)));

        public static final RegistryObject<Item> ALTAR_OF_NATURE = BLOCKS_ITEMS.register("altar_of_nature", () -> new BlockItem(Blocks.ALTAR_OF_NATURE.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(64)));

    }
}
