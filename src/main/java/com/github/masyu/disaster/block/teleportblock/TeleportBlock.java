// TeleportBlock.java
package com.github.masyu.disaster.block.teleportblock;

import com.github.masyu.disaster.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public final class TeleportBlock extends BaseEntityBlock {
    public static final EnumProperty<TeleportEndpoint> ENDPOINT =
            EnumProperty.create("endpoint", TeleportEndpoint.class);

    // ダンジョン内でTREE⇔BOSSが取りうる最大距離より少し広めに設定した探索半径
    private static final int SEARCH_RADIUS = 96;

    public TeleportBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(ENDPOINT, TeleportEndpoint.TREE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENDPOINT);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeleportBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    /** エンダーマンのテレポートを参考にした、ブロック周囲のポータルパーティクル演出。 */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 3; i++) {
            double x;
            double z;
            switch (random.nextInt(4)) {
                case 0 -> { x = pos.getX() - 0.08; z = pos.getZ() + random.nextDouble(); }
                case 1 -> { x = pos.getX() + 1.08; z = pos.getZ() + random.nextDouble(); }
                case 2 -> { x = pos.getX() + random.nextDouble(); z = pos.getZ() - 0.08; }
                default -> { x = pos.getX() + random.nextDouble(); z = pos.getZ() + 1.08; }
            }
            double y = pos.getY() + random.nextDouble();
            level.addParticle(ParticleTypes.PORTAL, x, y, z,
                    (random.nextDouble() - 0.5) * 0.04,
                    (random.nextDouble() - 0.5) * 0.04,
                    (random.nextDouble() - 0.5) * 0.04);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(player instanceof ServerPlayer serverPlayer) || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.PASS;
        }
        if (!(serverLevel.getBlockEntity(pos) instanceof TeleportBlockEntity blockEntity)) {
            return InteractionResult.PASS;
        }

        BlockPos destination = blockEntity.getCachedDestination();

        // キャッシュが無い、または既に壊されているなら周辺を探索する
        if (destination == null || !isValidTeleporter(serverLevel, destination, state)) {
            destination = findPartner(serverLevel, pos, state);
            if (destination != null) {
                blockEntity.setCachedDestination(destination);
                // 相方側にも、こちらの座標を先にキャッシュしておく(次回以降はお互い即座に使える)
                if (serverLevel.getBlockEntity(destination) instanceof TeleportBlockEntity partnerBE) {
                    partnerBE.setCachedDestination(pos);
                }
            }
        }

        if (destination == null) {
            serverPlayer.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("転送先が見つかりません。"), true);
            return InteractionResult.CONSUME;
        }

        BlockPos safePos = findSafePlayerPosition(serverLevel, serverPlayer, destination);
        if (safePos == null) {
            serverPlayer.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("転送先に安全な場所がありません。"), true);
            return InteractionResult.CONSUME;
        }

        serverPlayer.teleportTo(safePos.getX() + 0.5, safePos.getY(), safePos.getZ() + 0.5);
        return InteractionResult.CONSUME;
    }

    private static boolean isValidTeleporter(ServerLevel level, BlockPos pos, BlockState selfState) {
        if (!level.hasChunkAt(pos)) return false;
        BlockState other = level.getBlockState(pos);
        return other.is(ModBlocks.Blocks.TELEPORTER.get())
                && other.getValue(ENDPOINT) != selfState.getValue(ENDPOINT);
    }

    /** 自分を中心に一定範囲をスキャンし、逆側のエンドポイントを持つテレポーターを探す。 */
    @Nullable
    private static BlockPos findPartner(ServerLevel level, BlockPos self, BlockState selfState) {
        TeleportEndpoint wanted = selfState.getValue(ENDPOINT) == TeleportEndpoint.TREE
                ? TeleportEndpoint.BOSS : TeleportEndpoint.TREE;

        for (BlockPos pos : BlockPos.betweenClosed(
                self.offset(-SEARCH_RADIUS, -SEARCH_RADIUS, -SEARCH_RADIUS),
                self.offset(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS))) {
            if (pos.equals(self)) continue;
            if (!level.hasChunkAt(pos)) continue;
            BlockState state = level.getBlockState(pos);
            if (state.is(ModBlocks.Blocks.TELEPORTER.get()) && state.getValue(ENDPOINT) == wanted) {
                return pos.immutable();
            }
        }
        return null;
    }

    private static BlockPos findSafePlayerPosition(ServerLevel level, ServerPlayer player, BlockPos endpoint) {
        for (int dy = 1; dy <= 4; dy++) {
            BlockPos feet = endpoint.above(dy);
            if (!level.getWorldBorder().isWithinBounds(feet)) continue;
            var movedBox = player.getBoundingBox().move(
                    feet.getX() + 0.5 - player.getX(), feet.getY() - player.getY(),
                    feet.getZ() + 0.5 - player.getZ());
            if (level.noCollision(player, movedBox)) return feet;
        }
        return null;
    }
}