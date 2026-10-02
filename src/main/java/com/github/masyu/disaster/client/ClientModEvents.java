package com.github.masyu.disaster.client;

import com.github.masyu.disaster.entity.renderer.DungeonGuideOrbRenderer;
import com.github.masyu.disaster.registry.ModBlocks;
import com.github.masyu.disaster.registry.ModEntities;
import com.github.masyu.disaster.entity.renderer.NatureGuardianRenderer;
import com.github.masyu.disaster.entity.renderer.DryadRenderer; // ← 追加
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = "disaster", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.NATURE_GUARDIAN.get(), NatureGuardianRenderer::new);
        EntityRenderers.register(ModEntities.DRYAD.get(), DryadRenderer::new);
        EntityRenderers.register(ModEntities.DUNGEON_GUIDE_ORB.get(), DungeonGuideOrbRenderer::new);

        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.Blocks.ENTANGLING_ROOT.get(), RenderType.cutout());
        });
    }
}