package com.github.masyu.disaster.entity.ai.NatureGuadian;

import com.github.masyu.disaster.entity.boss.NatureGuardian;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroundSlamGoal extends Goal {
    private final NatureGuardian boss;
    private int windupTicks;
    private int shockwaveRadius;
    private static final int WINDUP_DURATION = 20; // 1秒タメ
    private static final int MAX_RADIUS = 8;
    private int cooldown;

    public GroundSlamGoal(NatureGuardian boss) {
        this.boss = boss;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = boss.getTarget();
        return target != null && boss.isGroundSlamReady() && boss.distanceToSqr(target) < 100;
    }

    @Override
    public void start() {
        windupTicks = 0;
        shockwaveRadius = 0;
        boss.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = boss.getTarget();
        if (target != null) {
            boss.getLookControl().setLookAt(target);
        }

        windupTicks++;

        // タメ中のパーティクル(警告表現)
        if (boss.level() instanceof ServerLevel serverLevel) {
            if (windupTicks < WINDUP_DURATION) {
                serverLevel.sendParticles(ParticleTypes.CRIT,
                        boss.getX(), boss.getY() + 0.1, boss.getZ(),
                        5, 0.5, 0.1, 0.5, 0.01);
            } else if (windupTicks == WINDUP_DURATION) {
                // 叩きつけ発生
                boss.level().playSound(null, boss.blockPosition(),
                        SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.5F, 0.8F);
                shockwaveRadius = 1;
            } else if (shockwaveRadius > 0 && shockwaveRadius <= MAX_RADIUS) {
                spawnShockwaveRing(serverLevel, shockwaveRadius);
                applyShockwaveDamage(shockwaveRadius);
                shockwaveRadius++;
            }
        }
    }

    private void spawnShockwaveRing(ServerLevel level, int radius) {
        double cx = boss.getX();
        double cz = boss.getZ();
        double y = boss.getY() + 0.1;
        int points = 16 + radius * 4;
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double x = cx + radius * Math.cos(angle);
            double z = cz + radius * Math.sin(angle);
            level.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.STONE.defaultBlockState()),
                    x, y, z, 1, 0, 0, 0, 0
            );
        }
    }

    private void applyShockwaveDamage(int radius) {
        AABB ring = boss.getBoundingBox().inflate(radius + 0.5);
        List<LivingEntity> hit = boss.level().getEntitiesOfClass(
                LivingEntity.class, ring,
                e -> e != boss && !alreadyHit.contains(e.getId())
        );
        for (LivingEntity entity : hit) {
            double dist = entity.distanceTo(boss);
            if (dist >= radius - 0.5 && dist <= radius + 0.5) {
                entity.hurt(boss.damageSources().mobAttack(boss), 6.0F);
                // ノックバック
                Vec3 push = entity.position().subtract(boss.position()).normalize().scale(0.8);
                entity.setDeltaMovement(push.x, 0.4, push.z);
                // スタン付与
                applyStun(entity, 60); // 3秒スタン
                alreadyHit.add(entity.getId());
            }
        }
    }

    private final Set<Integer> alreadyHit = new HashSet<>();

    private void applyStun(LivingEntity entity, int durationTicks) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, durationTicks, 255, false, false));
        entity.addEffect(new MobEffectInstance(MobEffects.JUMP, durationTicks, 128, false, false)); // ジャンプ不可(負の効果として)
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, durationTicks, 255, false, false));
        entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, durationTicks, 255, false, false));
    }

    @Override
    public boolean canContinueToUse() {
        return windupTicks <= WINDUP_DURATION
                || (shockwaveRadius > 0 && shockwaveRadius <= MAX_RADIUS);
    }

    @Override
    public void stop() {
        boss.setGroundSlamCooldown(100);
        alreadyHit.clear();
    }
}