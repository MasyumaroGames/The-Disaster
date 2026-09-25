package com.github.masyu.disaster.music;

import com.github.masyu.disaster.entity.NatureGuardian;
import com.github.masyu.disaster.registry.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class NatureGuardianBossMusic extends AbstractTickableSoundInstance {

    private final NatureGuardian boss;

    public NatureGuardianBossMusic(NatureGuardian boss) {
        super(
                ModSounds.NATURE_GUARDIAN_BOSS.get(),
                SoundSource.MUSIC,
                SoundInstance.createUnseededRandom()
        );

        this.boss = boss;

        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
    }

    @Override
    public void tick() {

        if (!boss.isAlive()) {
            this.stop();
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) {
            stop();
            return;
        }

        if (boss.distanceTo(mc.player) > 80) {
            stop();
        }
    }

}
