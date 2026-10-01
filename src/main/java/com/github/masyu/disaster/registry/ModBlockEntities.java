package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.Disaster;
import com.github.masyu.disaster.block.entity.NatureGuardianSpawnerBlockEntity;
import com.github.masyu.disaster.block.teleportblock.TeleportBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Disaster.MODID);

    public static final RegistryObject<BlockEntityType<TeleportBlockEntity>> TELEPORTER_BE =
            BLOCK_ENTITIES.register("teleporter", () -> BlockEntityType.Builder
                    .of(TeleportBlockEntity::new, ModBlocks.Blocks.TELEPORTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<NatureGuardianSpawnerBlockEntity>> NATURE_GUARDIAN_SPAWNER =
            BLOCK_ENTITIES.register("nature_guardian_spawner",
                    () -> BlockEntityType.Builder.of(
                            NatureGuardianSpawnerBlockEntity::new,
                            ModBlocks.Blocks.ALTAR_OF_NATURE.get()
                    ).build(null));

}
