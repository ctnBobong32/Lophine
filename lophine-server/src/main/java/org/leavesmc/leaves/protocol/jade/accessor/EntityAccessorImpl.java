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

import com.google.common.base.Suppliers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.protocol.jade.util.CommonUtil;

import java.util.function.Supplier;

public class EntityAccessorImpl extends AccessorImpl<EntityHitResult> implements EntityAccessor {

    private final Supplier<Entity> entity;

    public EntityAccessorImpl(Builder builder) {
        super(builder.level, builder.player, builder.hit);
        entity = builder.entity;
    }

    @Override
    public Entity getEntity() {
        return CommonUtil.wrapPartEntityParent(getRawEntity());
    }

    @Override
    public Entity getRawEntity() {
        return entity.get();
    }

    @NotNull
    @Override
    public Object getTarget() {
        return getEntity();
    }

    public static class Builder implements EntityAccessor.Builder {
        private ServerLevel level;
        private Player player;
        private Supplier<EntityHitResult> hit;
        private Supplier<Entity> entity;

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
        public Builder hit(Supplier<EntityHitResult> hit) {
            this.hit = hit;
            return this;
        }

        @Override
        public Builder entity(Supplier<Entity> entity) {
            this.entity = entity;
            return this;
        }

        @Override
        public Builder from(EntityAccessor accessor) {
            level = accessor.getLevel();
            player = accessor.getPlayer();
            hit = accessor::getHitResult;
            entity = accessor::getEntity;
            return this;
        }

        @Override
        public EntityAccessor build() {
            return new EntityAccessorImpl(this);
        }
    }

    public record SyncData(boolean showDetails, int id, int partIndex, Vec3 hitVec) {
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                SyncData::showDetails,
                ByteBufCodecs.VAR_INT,
                SyncData::id,
                ByteBufCodecs.VAR_INT,
                SyncData::partIndex,
                ByteBufCodecs.VECTOR3F.map(Vec3::new, Vec3::toVector3f),
                SyncData::hitVec,
                SyncData::new
        );

        public EntityAccessor unpack(ServerPlayer player) {
            Supplier<Entity> entity = Suppliers.memoize(() -> CommonUtil.getPartEntity(player.level().getEntity(id), partIndex));
            return new EntityAccessorImpl.Builder()
                    .level(player.level())
                    .player(player)
                    .entity(entity)
                    .hit(Suppliers.memoize(() -> new EntityHitResult(entity.get(), hitVec)))
                    .build();
        }
    }
}