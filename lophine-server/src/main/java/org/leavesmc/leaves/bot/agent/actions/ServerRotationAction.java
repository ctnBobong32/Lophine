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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.bot.ServerBot;
import org.leavesmc.leaves.command.CommandArgument;
import org.leavesmc.leaves.command.CommandArgumentResult;
import org.leavesmc.leaves.command.CommandArgumentType;
import org.leavesmc.leaves.entity.bot.actions.CraftRotationAction;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class ServerRotationAction extends ServerBotAction<ServerRotationAction> {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public ServerRotationAction() {
        super("rotation", CommandArgument.of(CommandArgumentType.FLOAT, CommandArgumentType.FLOAT), ServerRotationAction::new);
        this.setSuggestion(0, (sender, arg) -> sender instanceof Player player ? Pair.of(List.of(DF.format(player.getYaw())), "[yaw]") : Pair.of(List.of("0"), "<yaw>"));
        this.setSuggestion(1, (sender, arg) -> sender instanceof Player player ? Pair.of(List.of(DF.format(player.getPitch())), "[pitch]") : Pair.of(List.of("0"), "<pitch>"));
    }

    private float yaw = 0.0f;
    private float pitch = 0.0f;

    @Override
    public void loadCommand(ServerPlayer player, @NotNull CommandArgumentResult result) {
        try {
            this.yaw = result.readFloat(Objects.requireNonNull(player).getYRot());
            this.pitch = result.readFloat(player.getXRot());
        } catch (Exception e) {
            throw new IllegalArgumentException("No valid rotation specified", e);
        }
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    @Override
    @NotNull
    public CompoundTag save(@NotNull CompoundTag nbt) {
        super.save(nbt);
        nbt.putFloat("yaw", this.yaw);
        nbt.putFloat("pitch", this.pitch);
        return nbt;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.setYaw(nbt.getFloat("yaw").orElseThrow());
        this.setPitch(nbt.getFloat("pitch").orElseThrow());
    }

    @Override
    public boolean doTick(@NotNull ServerBot bot) {
        bot.setRot(yaw, pitch);
        return true;
    }

    @Override
    public Object asCraft() {
        return new CraftRotationAction(this);
    }
}
