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

package org.leavesmc.leaves.bot.agent.actions;

import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.command.CommandArgument;
import org.leavesmc.leaves.command.CommandArgumentResult;
import org.leavesmc.leaves.command.CommandArgumentType;

import java.util.List;
import java.util.function.Supplier;

public abstract class ServerTimerBotAction<E extends ServerTimerBotAction<E>> extends ServerBotAction<E> {

    public ServerTimerBotAction(String name, Supplier<E> creator) {
        this(name, CommandArgument.of(CommandArgumentType.INTEGER, CommandArgumentType.INTEGER, CommandArgumentType.INTEGER), creator);
    }

    public ServerTimerBotAction(String name, CommandArgument argument, Supplier<E> creator) {
        super(name, argument, creator);
        this.setSuggestion(0, Pair.of(List.of("0"), "[TickDelay]"));
        this.setSuggestion(1, Pair.of(List.of("20"), "[TickInterval]"));
        this.setSuggestion(2, Pair.of(List.of("1", "-1"), "[DoNumber]"));
    }

    @Override
    public void loadCommand(ServerPlayer player, @NotNull CommandArgumentResult result) {
        this.setStartDelayTick(result.readInt(0));
        this.setDoIntervalTick(result.readInt(20));
        this.setDoNumber(result.readInt(1));
    }
}
