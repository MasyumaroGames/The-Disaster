package com.github.masyu.disaster.client;

import com.github.masyu.disaster.entity.NatureGuardian;
import com.github.masyu.disaster.music.NatureGuardianBossMusic;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class BossMusicHandler {

    private static NatureGuardianBossMusic currentMusic;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {

        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null || mc.player == null)
            return;

        NatureGuardian foundBoss = null;

        for (Entity entity : mc.level.entitiesForRendering()) {

            if (entity instanceof NatureGuardian boss
                    && boss.isAlive()
                    && boss.distanceTo(mc.player) < 80) {

                foundBoss = boss;
                break;
            }
        }

        if (foundBoss != null) {

            if (currentMusic == null
                    || !mc.getSoundManager().isActive(currentMusic)) {

                currentMusic = new NatureGuardianBossMusic(foundBoss);

                mc.getSoundManager().play(currentMusic);
            }
        }
        else {

            if (currentMusic != null) {

                currentMusic.isStopped();

                currentMusic = null;
            }
        }
    }
}