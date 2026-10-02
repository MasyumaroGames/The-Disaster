package com.github.masyu.disaster.item.eye;

import com.github.masyu.disaster.entity.visual.DungeonGuideOrbEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;

import com.mojang.datafixers.util.Pair;

public class GreatTreeEyeItem extends Item {

    private static final int COOLDOWN_TICKS = 60;
    private static final int CHUNK_SEARCH_RADIUS = 500;
    private static final ResourceKey<Structure> GREAT_TREE_KEY = ResourceKey.create(
            Registries.STRUCTURE, new ResourceLocation("disaster", "great_tree_of_nature"));

    public GreatTreeEyeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.consume(stack);
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        BlockPos target = locateGreatTree(serverLevel, player.blockPosition());

        if (target == null) {
            player.displayClientMessage(
                    Component.translatable("message.disaster.great_tree_eye.not_found"), true);
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            return InteractionResultHolder.consume(stack);
        }

        DungeonGuideOrbEntity orb = new DungeonGuideOrbEntity(
                level, player.getX(), player.getY(0.5D), player.getZ());
        orb.setItem(stack);
        orb.signalTo(target);

        level.gameEvent(GameEvent.PROJECTILE_SHOOT, orb.position(), GameEvent.Context.of(player));
        level.addFreshEntity(orb);

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F,
                0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        level.levelEvent(null, 1003, player.blockPosition(), 0);

        player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        player.swing(hand, true);

        return InteractionResultHolder.success(stack);
    }

    private BlockPos locateGreatTree(ServerLevel serverLevel, BlockPos origin) {
        Registry<Structure> structureRegistry =
                serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE);

        Holder<Structure> holder = structureRegistry.getHolder(GREAT_TREE_KEY).orElse(null);
        if (holder == null) {
            return null;
        }

        HolderSet<Structure> holderSet = HolderSet.direct(holder);

        Pair<BlockPos, Holder<Structure>> result = serverLevel.getChunkSource().getGenerator()
                .findNearestMapStructure(serverLevel, holderSet, origin, CHUNK_SEARCH_RADIUS, false);

        return result != null ? result.getFirst() : null;
    }
}