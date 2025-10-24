/*
 * This file is part of Leaves (https://github.com/LeavesMC/Leaves)
 *
 * Leaves is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Leaves is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Leaves. If not, see <https://www.gnu.org/licenses/>.
 */

package org.leavesmc.leaves.protocol.jade.accessor;

import ca.spottedleaf.moonrise.common.util.TickThread;
import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * 用于获取方块目标和上下文信息的类
 * Class to get information of block target and context.
 */
public class BlockAccessorImpl extends AccessorImpl<BlockHitResult> implements BlockAccessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockAccessorImpl.class);
    
    private final BlockState blockState;
    @Nullable
    private final Supplier<BlockEntity> blockEntity;

    private BlockAccessorImpl(Builder builder) {
        super(builder.level, builder.player, Suppliers.ofInstance(builder.hit));
        blockState = builder.blockState;
        blockEntity = builder.blockEntity;
    }

    @Override
    public Block getBlock() {
        return getBlockState().getBlock();
    }

    @Override
    public BlockState getBlockState() {
        return blockState;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return blockEntity == null ? null : blockEntity.get();
    }

    @Override
    public BlockPos getPosition() {
        return getHitResult().getBlockPos();
    }

    @Nullable
    @Override
    public Object getTarget() {
        return getBlockEntity();
    }

    public static class Builder implements BlockAccessor.Builder {
        private ServerLevel level;
        private Player player;
        private BlockHitResult hit;
        private BlockState blockState = Blocks.AIR.defaultBlockState();
        private Supplier<BlockEntity> blockEntity;

        @Override
        public Builder level(ServerLevel level) {
            this.level = level;
            return this;
        }

        @Override
        public Builder player(Player player) {
            this.player = player;
            return this;
        }

        @Override
        public Builder hit(BlockHitResult hit) {
            this.hit = hit;
            return this;
        }

        @Override
        public Builder blockState(BlockState blockState) {
            this.blockState = blockState;
            return this;
        }

        @Override
        public Builder blockEntity(Supplier<BlockEntity> blockEntity) {
            this.blockEntity = blockEntity;
            return this;
        }

        @Override
        public Builder from(BlockAccessor accessor) {
            level = accessor.getLevel();
            player = accessor.getPlayer();
            hit = accessor.getHitResult();
            blockEntity = accessor::getBlockEntity;
            blockState = accessor.getBlockState();
            return this;
        }

        @Override
        public BlockAccessor build() {
            return new BlockAccessorImpl(this);
        }
    }

    public record SyncData(boolean showDetails, BlockHitResult hit, BlockState blockState, ItemStack fakeBlock) {
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                SyncData::showDetails,
                StreamCodec.of(FriendlyByteBuf::writeBlockHitResult, FriendlyByteBuf::readBlockHitResult),
                SyncData::hit,
                ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY),
                SyncData::blockState,
                ItemStack.OPTIONAL_STREAM_CODEC,
                SyncData::fakeBlock,
                SyncData::new
        );

        public BlockAccessor unpack(ServerPlayer player) {
            Supplier<BlockEntity> blockEntity = null;
            if (blockState.hasBlockEntity()) {
                blockEntity = Suppliers.memoize(() -> {
                    // 线程安全检查 - 修复崩溃问题的关键
                    ServerLevel level = player.level();
                    BlockPos pos = hit.getBlockPos();
                    
                    // 检查是否在正确的线程中
                    if (!TickThread.isTickThreadFor(level, pos)) {
                        LOGGER.debug("[Jade] 线程安全检查失败: 试图在区域线程中获取方块实体, 位置: {}", pos);
                        return null;
                    }
                    
                    // 检查区块是否已加载
                    if (!level.isLoaded(pos)) {
                        LOGGER.debug("[Jade] 区块未加载: {}", pos);
                        return null;
                    }
                    
                    try {
                        return level.getBlockEntity(pos);
                    } catch (Exception e) {
                        LOGGER.warn("[Jade] 获取方块实体时出错, 位置 {}: {}", pos, e.getMessage());
                        return null;
                    }
                });
            }
            return new Builder()
                    .level(player.level())
                    .player(player)
                    .hit(hit)
                    .blockState(blockState)
                    .blockEntity(blockEntity)
                    .build();
        }
    }
}