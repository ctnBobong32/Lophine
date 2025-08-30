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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.waypoints.ServerWaypointManager;
import org.jetbrains.annotations.NotNull;
import fun.bm.lophine.config.modules.function.FakeplayerConfig;
import org.leavesmc.leaves.bot.agent.AbstractBotConfig;
import org.leavesmc.leaves.command.CommandArgument;
import org.leavesmc.leaves.command.CommandArgumentType;

import java.util.List;

public class LocatorBarConfig extends AbstractBotConfig<Boolean> {

    public static final String NAME = "enable_locator_bar";

    private boolean value;

    public LocatorBarConfig() {
        super(NAME, CommandArgument.of(CommandArgumentType.BOOLEAN).setSuggestion(0, List.of("true", "false")));
        this.value = FakeplayerConfig.enableLocatorBar;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) throws IllegalArgumentException {
        this.value = value;
        ServerWaypointManager manager = this.bot.level().getWaypointManager();
        if (value) {
            manager.trackWaypoint(this.bot);
        } else {
            manager.untrackWaypoint(this.bot);
        }
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        super.save(nbt);
        nbt.putBoolean(NAME, this.getValue());
        return nbt;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        this.setValue(nbt.getBooleanOr(NAME, FakeplayerConfig.enableLocatorBar));
    }
}