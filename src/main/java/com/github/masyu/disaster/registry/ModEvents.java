package com.github.masyu.disaster.registry;

import com.github.masyu.disaster.entity.boss.NatureGuardian;
import com.github.masyu.disaster.entity.normal_mobs.Dryad;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "disaster", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.NATURE_GUARDIAN.get(),
                NatureGuardian.createAttributes().build()
        );

        event.put(
                ModEntities.DRYAD.get(),
                Dryad.createAttributes().build()
        );
    }
}