package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.Disaster;
import com.github.masyu.disaster.entity.boss.NatureGuardian;

import com.github.masyu.disaster.entity.normal_mobs.Dryad;
import com.github.masyu.disaster.entity.visual.DungeonGuideOrbEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Disaster.MODID);

    /**
     * Mob
     */

    // DRYAD
    public static final RegistryObject<EntityType<Dryad>> DRYAD =
            ENTITIES.register("dryad",
                    () -> EntityType.Builder.of(Dryad::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .build("dryad"));

    /**
     * Boss
     */


    // Nature Guadian
    public static final RegistryObject<EntityType<NatureGuardian>> NATURE_GUARDIAN =
            ENTITIES.register("nature_guardian",
                    () -> EntityType.Builder.of(
                                    NatureGuardian::new,
                                    MobCategory.MONSTER)
                            .sized(2.0F, 4.0F)
                            .build("nature_guardian"));

    /**
     * Visual Entity
     */

    // Eye
    public static final RegistryObject<EntityType<DungeonGuideOrbEntity>> DUNGEON_GUIDE_ORB =
            ENTITIES.register("dungeon_guide_orb",
                    () -> EntityType.Builder.<DungeonGuideOrbEntity>of(DungeonGuideOrbEntity::new, MobCategory.MISC)
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(64)
                            .updateInterval(1) // 10 → 1 に変更(滑らかな追従のため)
                            .build("dungeon_guide_orb"));


}