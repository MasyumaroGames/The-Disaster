package com.github.masyu.disaster.block.teleportblock;

import com.github.masyu.disaster.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public final class TeleportBlockEntity extends BlockEntity {
    @Nullable
    private BlockPos cachedDestination;

    public TeleportBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TELEPORTER_BE.get(), pos, state);
    }

    @Nullable
    public BlockPos getCachedDestination() {
        return cachedDestination;
    }

    public void setCachedDestination(BlockPos pos) {
        this.cachedDestination = pos.immutable();
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (cachedDestination != null) {
            tag.putLong("CachedDestination", cachedDestination.asLong());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("CachedDestination")) {
            cachedDestination = BlockPos.of(tag.getLong("CachedDestination"));
        }
    }
}