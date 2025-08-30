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

package org.leavesmc.leaves.bot.agent.configs;

import fun.bm.lophine.config.modules.function.FakeplayerConfig;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.bot.agent.AbstractBotConfig;
import org.leavesmc.leaves.command.CommandArgument;
import org.leavesmc.leaves.command.CommandArgumentType;

import java.util.List;

public class SimulationDistanceConfig extends AbstractBotConfig<Integer> {

    public static final String NAME = "simulation_distance";

    public SimulationDistanceConfig() {
        super(NAME, CommandArgument.of(CommandArgumentType.INTEGER).setSuggestion(0, Pair.of(List.of("2", "10"), "<INT 2 - 32>")));
    }

    @Override
    public Integer getValue() {
        return this.bot.getBukkitEntity().getSimulationDistance();
    }

    @Override
    public void setValue(Integer value) {
        if (value < 2 || value > 32) {
            throw new IllegalArgumentException("simulation_distance must be a number between 2 and 32, got: " + value);
        }
        this.bot.getBukkitEntity().setSimulationDistance(value);
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag nbt) {
        super.save(nbt);
        nbt.putInt(NAME, this.getValue());
        return nbt;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        this.setValue(nbt.getIntOr(NAME, FakeplayerConfig.getSimulationDistance(this.bot)));
    }
}
