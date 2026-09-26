package com.github.masyu.disaster;

import com.github.masyu.disaster.registry.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

@Mod("disaster")
public class Disaster {

    public static final String MODID = "disaster";

    public Disaster() {
        GeckoLib.initialize();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.ENTITIES.register(bus);
        ModSounds.SOUND_EVENTS.register(bus);
        ModItems.ITEMS.register(bus);
        ModBlocks.Blocks.BLOCKS.register(bus);
        ModBlocks.BlockItems.BLOCKS_ITEMS.register(bus);
        ModTabs.MOD_TABS.register(bus);

    }

}
