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

package org.leavesmc.leaves.protocol.servux.logger;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import io.papermc.paper.threadedregions.RegionizedWorldData;
import io.papermc.paper.threadedregions.TickRegionScheduler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.leavesmc.leaves.protocol.servux.ServuxHudDataProtocol;
import org.leavesmc.leaves.protocol.servux.logger.data.MobCapData;
import org.leavesmc.leaves.protocol.servux.logger.data.TickData;

import java.util.ArrayList;
import java.util.function.Function;

public abstract class DataLogger<T extends Tag> {

    private final Type type;

    public DataLogger(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public abstract T getResult(MinecraftServer server, ServuxHudDataProtocol protocol, ServerPlayer player);

    public enum Type implements StringRepresentable {
        TPS("tps", Tps::new, Tps.CODEC),
        MOB_CAPS("mob_caps", MobCaps::new, MobCaps.CODEC);

        public static final EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        public static final ImmutableList<Type> VALUES = ImmutableList.copyOf(values());

        private final String name;
        private final Function<Type, DataLogger<?>> factory;
        private final Codec<?> codec;

        Type(String name, Function<Type, DataLogger<?>> factory, Codec<?> codec) {
            this.name = name;
            this.factory = factory;
            this.codec = codec;
        }

        public static @Nullable Type fromStringStatic(String name) {
            for (Type type : VALUES) {
                if (type.name.equalsIgnoreCase(name)) {
                    return type;
                }
            }

            return null;
        }

        public Function<Type, DataLogger<?>> getFactory() {
            return factory;
        }

        public @Nullable DataLogger<?> init() {
            return this.factory.apply(this);
        }

        public Codec<?> codec() {
            return this.codec;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public static class Tps extends DataLogger<CompoundTag> {

        public static final Codec<CompoundTag> CODEC = CompoundTag.CODEC;

        public Tps(Type type) {
            super(type);
        }

        @Override
        public CompoundTag getResult(MinecraftServer server, ServuxHudDataProtocol protocol, ServerPlayer player) {
            this.build(server, protocol, player);
            return null;
        }

        private TickData build(MinecraftServer server, ServuxHudDataProtocol protocol, ServerPlayer player) {
            io.papermc.paper.threadedregions.RegionizedServer.getInstance().taskQueue.queueTickTaskQueue(
                    player.level(),
                    ca.spottedleaf.moonrise.common.util.CoordinateUtils.getChunkX(player.position()),
                    ca.spottedleaf.moonrise.common.util.CoordinateUtils.getChunkZ(player.position()),
                    () -> {
                        ServerTickRateManager tickManager = server.tickRateManager();
                        boolean frozen = tickManager.isFrozen();
                        boolean sprinting = tickManager.isSprinting();
                        io.papermc.paper.threadedregions.TickData.TickReportData tickData = TickRegionScheduler.getCurrentRegion().getData().getRegionSchedulingHandle().getTickReport5s(System.nanoTime());
                        final double tps = tickData.tpsData().segmentAll().average();
                        final double mspt = tickData.timePerTickData().segmentAll().average() / 1.0E6;

                        TickData tk = new TickData(
                                mspt, tps,
                                tickManager.getRemainingSprintTicks(),
                                frozen, sprinting,
                                tickManager.isSteppingForward()
                        );
                        Tag ret = TickData.CODEC.encodeStart(server.registryAccess().createSerializationContext(NbtOps.INSTANCE), tk).getOrThrow();
                        protocol.applyData(Type.TPS, player, ret);
                    }
            );

            return null;
        }
    }

    public static class MobCaps extends DataLogger<CompoundTag> {

        public static final Codec<CompoundTag> CODEC = CompoundTag.CODEC;

        public MobCaps(Type type) {
            super(type);
        }

        @Override
        public CompoundTag getResult(MinecraftServer server, ServuxHudDataProtocol protocol, ServerPlayer player) {
            CompoundTag nbt = new CompoundTag();
            io.papermc.paper.threadedregions.RegionizedServer.getInstance().taskQueue.queueTickTaskQueue(
                    player.level(),
                    ca.spottedleaf.moonrise.common.util.CoordinateUtils.getChunkX(player.position()),
                    ca.spottedleaf.moonrise.common.util.CoordinateUtils.getChunkZ(player.position()),
                    () -> {
                        final RegionizedWorldData data = player.level().getCurrentWorldData();
                        if (data == null) return;

                        final NaturalSpawner.SpawnState info = data.lastSpawnState;
                        if (info == null) return;

                        int chunks = info.getSpawnableChunkCount();
                        Object2IntMap<MobCategory> counts = info.getMobCategoryCounts();
                        MobCapData mobCapData = new MobCapData(new ArrayList<>(), player.level().getGameTime());
                        for (MobCategory category : MobCategory.values()) {
                            mobCapData.data().add(new MobCapData.Cap(counts.getOrDefault(category, 0), NaturalSpawner.globalLimitForCategory(player.level(), category, chunks)));
                        }

                        try {
                            nbt.put(player.level().dimension().location().toString(), MobCapData.CODEC.encodeStart(player.level().registryAccess().createSerializationContext(NbtOps.INSTANCE), mobCapData).getPartialOrThrow());
                            protocol.applyData(Type.MOB_CAPS, player, nbt);
                        } catch (Exception ignored) {
                        }
                    });
            return null;
        }
    }
}