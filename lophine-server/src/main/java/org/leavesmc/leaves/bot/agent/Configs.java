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

package org.leavesmc.leaves.bot.agent;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.leavesmc.leaves.bot.agent.configs.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused"})
public class Configs {
    private static final Map<Class<?>, AbstractBotConfig<?, ?, ?>> configs = new HashMap<>();

    public static final SkipSleepConfig SKIP_SLEEP = register(new SkipSleepConfig());
    public static final AlwaysSendDataConfig ALWAYS_SEND_DATA = register(new AlwaysSendDataConfig());
    public static final SpawnPhantomConfig SPAWN_PHANTOM = register(new SpawnPhantomConfig());
    public static final SimulationDistanceConfig SIMULATION_DISTANCE = register(new SimulationDistanceConfig());
    public static final TickTypeConfig TICK_TYPE = register(new TickTypeConfig());
    public static final LocatorBarConfig ENABLE_LOCATOR_BAR = register(new LocatorBarConfig());

    @Nullable
    public static AbstractBotConfig<?, ?, ?> getConfig(String name) {
        return configs.values().stream()
                .filter(config -> config.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @NotNull
    @Contract(pure = true)
    public static Collection<AbstractBotConfig<?, ?, ?>> getConfigs() {
        return configs.values();
    }

    @SuppressWarnings("unchecked")
    private static <Value, Type, E extends AbstractBotConfig<Value, Type, E>> @NotNull E register(AbstractBotConfig<Value, Type, E> instance) {
        configs.put(instance.getClass(), instance);
        return (E) instance;
    }
}
