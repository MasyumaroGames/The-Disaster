package com.github.masyu.disaster.entity;

import com.github.masyu.disaster.entity.ai.NatureGuadian.GroundSlamGoal;
import com.github.masyu.disaster.entity.ai.NatureGuadian.RootEntangleGoal;
import com.github.masyu.disaster.registry.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NatureGuardian extends Monster implements GeoEntity {
    public NatureGuardian(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 400.0D)   // 体力
                .add(Attributes.MOVEMENT_SPEED, 0.25D) // 移動速度
                .add(Attributes.ATTACK_DAMAGE, 20.5D)  // 攻撃力
                .add(Attributes.FOLLOW_RANGE, 50.0D);  // 索敵距離
    }

    private static final RawAnimation IDLE =
            RawAnimation.begin().thenLoop("breathing");

    private static final RawAnimation WALK =
            RawAnimation.begin().thenLoop("walk");

    private static final RawAnimation ATTACK =
            RawAnimation.begin().thenPlay("Lower");

    private final ServerBossEvent bossEvent =
            new ServerBossEvent(
                    Component.translatable("boss.disaster.nature_guardian"),
                    BossEvent.BossBarColor.GREEN,
                    BossEvent.BossBarOverlay.PROGRESS);

    private boolean attacking = false;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(
                new AnimationController<>(this,
                        "MoveController",
                        5,
                        state -> {
                            if (state.isMoving())
                                return state.setAndContinue(WALK);

                            return state.setAndContinue(IDLE);
                        }));

        controllers.add(
                new AnimationController<>(this,
                        "AttackController",
                        state -> PlayState.STOP)
                        .triggerableAnim("attack", ATTACK));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1,
                new RootEntangleGoal(this));

        this.goalSelector.addGoal(1,
                new GroundSlamGoal(this));

        this.goalSelector.addGoal(2,
                new MeleeAttackGoal(this, 1.0D, false));

        this.goalSelector.addGoal(3,
                new WaterAvoidingRandomStrollGoal(this, 0.8D));

        this.goalSelector.addGoal(4,
                new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.goalSelector.addGoal(4,
                new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1,
                new HurtByTargetGoal(this));

        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(
                        this,
                        Player.class,
                        true));
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);

        bossEvent.addPlayer(player);

        System.out.println("PLAYER START SEEING BOSS");
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);

        bossEvent.removePlayer(player);
    }

    @Override
    public boolean doHurtTarget(Entity target) {

        boolean result = super.doHurtTarget(target);

        System.out.println("NATURE GUARDIAN ATTACK");

        if (!level().isClientSide()) {
            triggerAnim("AttackController", "attack");
        }

        return result;
    }

    private int attackTicks = 0;

    @Override
    public void tick() {
        super.tick();

        bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        if (attacking) {
            attackTicks++;

            if (attackTicks > 50) {
                attacking = false;
                attackTicks = 0;
            }
        }

        if (attacking) {
            System.out.println("AttackTicks: " + attackTicks);
        }
    }
    @Override
    public void die(DamageSource damageSource) {

        if (!this.level().isClientSide()) {

            this.level().playSound(
                    null,
                    this.blockPosition(),
                    ModSounds.NATURE_GUARDIAN_DEATH.get(),
                    this.getSoundSource(),
                    1.0F,
                    1.0F
            );

            this.level().explode(
                    this,
                    getX(),
                    getY(),
                    getZ(),
                    0.0F,
                    Level.ExplosionInteraction.NONE
            );
        }

        super.die(damageSource);
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.NATURE_GUARDIAN_HURT.get();
    }

    // --- クールダウン管理用フィールド ---
    private int groundSlamCooldown = 0;
    private int rootEntangleCooldown = 0;

    // ... 既存のコンストラクタやフィールドはそのまま ...

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (groundSlamCooldown > 0) {
            groundSlamCooldown--;
        }
        if (rootEntangleCooldown > 0) {
            rootEntangleCooldown--;
        }
    }

    // --- GroundSlam用 ---
    public boolean isGroundSlamReady() {
        return groundSlamCooldown <= 0;
    }

    public void setGroundSlamCooldown(int ticks) {
        this.groundSlamCooldown = ticks;
    }

    // --- RootEntangle用 ---
    public boolean isRootEntangleReady() {
        return rootEntangleCooldown <= 0;
    }

    public void setRootEntangleCooldown(int ticks) {
        this.rootEntangleCooldown = ticks;
    }

}
