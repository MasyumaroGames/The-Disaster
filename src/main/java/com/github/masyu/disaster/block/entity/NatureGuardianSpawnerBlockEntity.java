package com.github.masyu.disaster.block.entity;

import com.github.masyu.disaster.entity.boss.NatureGuardian;
import com.github.masyu.disaster.registry.ModBlockEntities;
import com.github.masyu.disaster.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class NatureGuardianSpawnerBlockEntity extends BlockEntity {

    private static final double TRIGGER_RANGE = 6.0; // 反応する距離(ブロック)
    private static final int CHECK_INTERVAL = 10;    // 10tick(0.5秒)ごとにチェック
    private int tickCounter = 0;
    private boolean triggered = false;

    public NatureGuardianSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NATURE_GUARDIAN_SPAWNER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, NatureGuardianSpawnerBlockEntity be) {
        if (be.triggered || level.isClientSide) {
            return;
        }

        be.tickCounter++;
        if (be.tickCounter < CHECK_INTERVAL) {
            return;
        }
        be.tickCounter = 0;

        AABB range = new AABB(pos).inflate(TRIGGER_RANGE);
        List<Player> players = level.getEntitiesOfClass(Player.class, range,
                p -> !p.isSpectator() && !p.isCreative());

        if (!players.isEmpty()) {
            be.trigger(level, pos);
        }
    }

    private void trigger(Level level, BlockPos pos) {
        this.triggered = true;

        NatureGuardian guardian = ModEntities.NATURE_GUARDIAN.get().create(level);
        if (guardian != null) {
            guardian.moveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0F, 0.0F);
            guardian.finalizeSpawn(
                    (net.minecraft.server.level.ServerLevel) level,
                    level.getCurrentDifficultyAt(pos),
                    MobSpawnType.TRIGGERED,
                    null,
                    null
            );
            level.addFreshEntity(guardian);
        }

        // ブロックを破壊(アイテムはドロップさせない場合はfalse)
        level.destroyBlock(pos, false);
    }
}