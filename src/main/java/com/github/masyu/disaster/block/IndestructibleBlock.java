package com.github.masyu.disaster.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class IndestructibleBlock extends Block {
    public IndestructibleBlock() {
        super(Properties.of()
                .strength(-1,3600000)
                .explosionResistance(1000000000000000000000000000F)
                .sound(SoundType.STONE)
        );
    }
}
