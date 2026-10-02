package com.github.masyu.disaster.item.records;

import com.github.masyu.disaster.registry.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;

public class MagnificentNature extends RecordItem {

    public MagnificentNature() {
        super(
                15,
                ModSounds.NATURE_GUARDIAN_BOSS,
                new Properties()
                        .stacksTo(1)
                        .rarity(Rarity.EPIC)
                        .fireResistant(),
                184 * 20
        );
    }
}
