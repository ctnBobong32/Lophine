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
import fun.bm.lophine.config.modules.experiment.CommandConfig;
import fun.bm.lophine.config.modules.function.FakeplayerConfig;
import me.earthme.luminol.utils.NullPlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.waypoints.ServerWaypointManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.bot.ServerBot;
import org.leavesmc.leaves.command.CommandContext;

public class LocatorBarConfig extends AbstractBotConfig<Boolean, Boolean, LocatorBarConfig> {
    private boolean value;

    public LocatorBarConfig() {
        super("enable_locator_bar", BoolArgumentType.bool(), LocatorBarConfig::new);
        this.value = FakeplayerConfig.enableLocatorBar && CommandConfig.waypoint;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(@NotNull Boolean value) throws IllegalArgumentException {
        if (bot == null) {
            Bukkit.getGlobalRegionScheduler().runDelayed(new NullPlugin(), (task) -> setValue(value), 20);
        } else {
            setValue(value, this.bot);
        }
    }

    public void setValue(@NotNull Boolean value, ServerBot bot) throws IllegalArgumentException {
        this.value = value;
        ServerWaypointManager manager = bot.level().getWaypointManager();
        if (value) {
            manager.trackWaypoint(bot);
        } else {
            manager.untrackWaypoint(bot);
        }
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
        this.setValue(nbt.getBooleanOr(getName(), FakeplayerConfig.enableLocatorBar && CommandConfig.waypoint));
    }
}