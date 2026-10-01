package com.github.masyu.disaster.block;

import com.github.masyu.disaster.block.entity.NatureGuardianSpawnerBlockEntity;
import com.github.masyu.disaster.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class NatureGuardianAltarBlock extends BaseEntityBlock {

    // モデルの各パーツに近い範囲を組み合わせた当たり判定
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 3, 15),   // 台座本体
            Block.box(3, 3, 3, 13, 9, 13),   // 中央コア
            Block.box(0, 9, 0, 16, 14, 16),  // 天板
            Block.box(-1, 11, -1, 2, 16, 2), // 柱(北西)
            Block.box(14, 11, -1, 17, 16, 2),// 柱(北東)
            Block.box(-1, 11, 14, 2, 16, 17),// 柱(南西)
            Block.box(14, 11, 14, 17, 16, 17)// 柱(南東)
    );

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public NatureGuardianAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL; // 通常の見た目のブロックとして描画
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NatureGuardianSpawnerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null :
                createTickerHelper(type, ModBlockEntities.NATURE_GUARDIAN_SPAWNER.get(),
                        NatureGuardianSpawnerBlockEntity::serverTick);
    }
}