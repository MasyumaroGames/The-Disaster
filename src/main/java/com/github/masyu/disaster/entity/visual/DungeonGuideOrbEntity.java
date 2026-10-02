package com.github.masyu.disaster.entity.visual;

import com.github.masyu.disaster.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DungeonGuideOrbEntity extends Entity implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK =
            SynchedEntityData.defineId(DungeonGuideOrbEntity.class, EntityDataSerializers.ITEM_STACK);

    private double tx, ty, tz;
    private int life;

    public DungeonGuideOrbEntity(EntityType<? extends DungeonGuideOrbEntity> type, Level level) {
        super(type, level);
    }

    public DungeonGuideOrbEntity(Level level, double x, double y, double z) {
        this(ModEntities.DUNGEON_GUIDE_ORB.get(), level);
        this.setPos(x, y, z);
    }

    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public void setItem(ItemStack stack) {
        this.getEntityData().set(DATA_ITEM_STACK, net.minecraft.Util.make(stack.copy(), s -> s.setCount(1)));
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    public void signalTo(BlockPos target) {
        double tx0 = target.getX();
        int ty0 = target.getY();
        double tz0 = target.getZ();
        double dx = tx0 - this.getX();
        double dz = tz0 - this.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);

        if (dist > 12.0D) {
            this.tx = this.getX() + dx / dist * 12.0D;
            this.tz = this.getZ() + dz / dist * 12.0D;
            this.ty = this.getY() + 8.0D;
        } else {
            this.tx = tx0;
            this.ty = ty0;
            this.tz = tz0;
        }
        this.life = 0;
    }

    @Override
    public void tick() {
        if (this.getItem().isEmpty()) {
            this.discard();
            return;
        }

        super.tick();

        Vec3 delta = this.getDeltaMovement();
        double nx, ny, nz;

        double dxToTarget = this.tx - this.getX();
        double dzToTarget = this.tz - this.getZ();
        double dyToTarget = this.ty - this.getY();
        double distToTarget = Math.sqrt(dxToTarget * dxToTarget + dyToTarget * dyToTarget + dzToTarget * dzToTarget);

        boolean arrived = distToTarget < 1.5D;

        if (arrived) {
            // 到達したら完全に静止させる(速度も回転も一切更新しない)
            nx = this.tx;
            ny = this.ty;
            nz = this.tz;
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            nx = this.getX() + delta.x;
            ny = this.getY() + delta.y;
            nz = this.getZ() + delta.z;
            double horizontalDist = delta.horizontalDistance();

            this.setXRot(lerpRotation(this.xRotO, (float) (Mth.atan2(delta.y, horizontalDist) * (180F / Math.PI))));
            if (horizontalDist > 0.01D) {
                this.setYRot(lerpRotation(this.yRotO, (float) (Mth.atan2(delta.x, delta.z) * (180F / Math.PI))));
            }

            if (!this.level().isClientSide) {
                double dx = this.tx - nx;
                double dz = this.tz - nz;
                float dist = (float) Math.sqrt(dx * dx + dz * dz);
                float angle = (float) Mth.atan2(dz, dx);

                double speed = Mth.lerp(0.0025D, horizontalDist, (double) dist);
                double vy = delta.y;
                if (dist < 1.0F) {
                    speed *= 0.8D;
                    vy *= 0.8D;
                }

                int dir = this.getY() < this.ty ? 1 : -1;
                delta = new Vec3(Math.cos(angle) * speed, vy + ((double) dir - vy) * 0.015D, Math.sin(angle) * speed);
                this.setDeltaMovement(delta);
            }
        }

        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE,
                        nx - delta.x * 0.25D, ny - delta.y * 0.25D, nz - delta.z * 0.25D,
                        delta.x, delta.y, delta.z);
            }
        } else {
            this.level().addParticle(ParticleTypes.PORTAL, nx, ny, nz,
                    (random.nextDouble() - 0.5) * 0.02, (random.nextDouble() - 0.5) * 0.02, (random.nextDouble() - 0.5) * 0.02);
        }

        if (!this.level().isClientSide) {
            this.setPos(nx, ny, nz);
            ++this.life;
            if (this.life > 80) {
                this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
                if (this.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.END_ROD, tx, ty, tz, 20, 0.3, 0.3, 0.3, 0.02);
                }
                this.discard();
            }
        } else {
            this.setPosRaw(nx, ny, nz);
        }
    }

    public static float lerpRotation(float from, float to) {
        while (to - from < -180.0F) from -= 360.0F;
        while (to - from >= 180.0F) from += 360.0F;
        return Mth.lerp(0.2F, from, to);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        ItemStack stack = getItem();
        if (!stack.isEmpty()) {
            tag.put("Item", stack.save(new CompoundTag()));
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setItem(ItemStack.of(tag.getCompound("Item")));
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}