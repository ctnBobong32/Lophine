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

import com.mojang.brigadier.arguments.BoolArgumentType;
import fun.bm.lophine.config.modules.function.FakeplayerConfig;
import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.command.CommandContext;

import java.util.List;

public class SpawnPhantomConfig extends AbstractBotConfig<Boolean, Boolean, SpawnPhantomConfig> {
    private boolean value;

    public SpawnPhantomConfig() {
        super("spawn_phantom", BoolArgumentType.bool(), SpawnPhantomConfig::new);
        this.value = FakeplayerConfig.canSpawnPhantom;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) throws IllegalArgumentException {
        this.value = value;
    }

    @Override
    public List<Pair<String, String>> getExtraData() {
        return List.of(Pair.of("not_sleeping_ticks", String.valueOf(bot.notSleepTicks)));
    }

    @Override
    public Boolean loadFromCommand(@NotNull CommandContext context) {
        return context.getBoolean(getName());
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        super.save(nbt);
        nbt.putBoolean(getName(), this.getValue());
        return nbt;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        this.setValue(nbt.getBooleanOr(getName(), FakeplayerConfig.canSpawnPhantom));
    }
}
