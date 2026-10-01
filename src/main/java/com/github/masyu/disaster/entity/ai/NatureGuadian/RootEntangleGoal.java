package com.github.masyu.disaster.entity.ai.NatureGuadian;

import com.github.masyu.disaster.entity.boss.NatureGuardian;
import com.github.masyu.disaster.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class RootEntangleGoal extends Goal {
    private final NatureGuardian boss;
    private int cooldown;
    private final List<BlockPos> placedRoots = new ArrayList<>();
    private LivingEntity currentTarget;
    private int duration;

    public RootEntangleGoal(NatureGuardian boss) {
        this.boss = boss;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = boss.getTarget();
        return target != null && boss.isRootEntangleReady() && boss.distanceToSqr(target) < 400;
    }

    @Override
    public void start() {
        currentTarget = boss.getTarget();
        if (currentTarget == null) return;

        boss.level().playSound(null, boss.blockPosition(),
                SoundEvents.GRASS_BREAK, SoundSource.HOSTILE, 1.0F, 0.7F);

        BlockPos targetPos = currentTarget.blockPosition();

        // ターゲット中心に十字/円形で根を生やす
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos rootPos = targetPos.offset(dx, 0, dz);
                if (canPlaceRoot(rootPos)) {
                    boss.level().setBlockAndUpdate(rootPos, ModBlocks.Blocks.ENTANGLING_ROOT.get().defaultBlockState());
                    placedRoots.add(rootPos);
                }
            }
        }

        // 拘束効果
        currentTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 255, false, true));
        currentTarget.addEffect(new MobEffectInstance(MobEffects.JUMP, 100, -10, false, true)); // 負のジャンプで実質不可
        duration = 100; // 5秒間拘束
    }

    private boolean canPlaceRoot(BlockPos pos) {
        BlockState below = boss.level().getBlockState(pos.below());
        return boss.level().getBlockState(pos).isAir() && below.isSolid();
    }

    @Override
    public void tick() {
        duration--;
        if (currentTarget != null && boss.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    currentTarget.getX(), currentTarget.getY() + 0.2, currentTarget.getZ(),
                    2, 0.3, 0.1, 0.3, 0.0);
            // 完全に固定したい場合は移動をキャンセル
            currentTarget.setDeltaMovement(0, currentTarget.getDeltaMovement().y, 0);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return duration > 0 && currentTarget != null && currentTarget.isAlive();
    }

    @Override
    public void stop() {
        // 根を撤去
        for (BlockPos pos : placedRoots) {
            if (boss.level().getBlockState(pos).is(ModBlocks.Blocks.ENTANGLING_ROOT.get())) {
                boss.level().removeBlock(pos, false);
            }
        }
        placedRoots.clear();
        boss.setRootEntangleCooldown(160); // ← ここを変更
        currentTarget = null;
    }
}
