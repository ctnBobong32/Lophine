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

package org.leavesmc.leaves.protocol.jade.provider.block;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.protocol.jade.JadeProtocol;
import org.leavesmc.leaves.protocol.jade.accessor.BlockAccessor;
import org.leavesmc.leaves.protocol.jade.provider.IServerDataProvider;

public enum RedstoneProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation MC_REDSTONE = JadeProtocol.mc_id("redstone");

    @Override
    public void appendServerData(CompoundTag data, @NotNull BlockAccessor accessor) {
        BlockEntity blockEntity = accessor.getBlockEntity();
        if (blockEntity instanceof ComparatorBlockEntity comparator) {
            data.putInt("Signal", comparator.getOutputSignal());
        } else if (blockEntity instanceof CalibratedSculkSensorBlockEntity) {
            Direction direction = accessor.getBlockState().getValue(CalibratedSculkSensorBlock.FACING).getOpposite();
            int signal = accessor.getLevel().getSignal(accessor.getPosition().relative(direction), direction);
            data.putInt("Signal", signal);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return MC_REDSTONE;
    }
}
