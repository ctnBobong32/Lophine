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

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.bot.ServerBot;
import org.leavesmc.leaves.entity.bot.actions.CraftUseItemToAction;

public class ServerUseItemToAction extends ServerUseBotAction<ServerUseItemToAction> {

    public ServerUseItemToAction() {
        super("use_to", ServerUseItemToAction::new);
    }

    @Override
    protected boolean interact(@NotNull ServerBot bot) {
        EntityHitResult hitResult = bot.getEntityHitResult();
        return useItemTo(bot, hitResult, InteractionHand.MAIN_HAND).consumesAction();
    }

    public static InteractionResult useItemTo(ServerBot bot, EntityHitResult hitResult, InteractionHand hand) {
        if (hitResult == null) {
            return InteractionResult.FAIL;
        }

        Entity entity = hitResult.getEntity();
        if (!bot.level().getWorldBorder().isWithinBounds(entity.blockPosition())) {
            return InteractionResult.FAIL;
        }

        Vec3 vec3 = hitResult.getLocation().subtract(entity.getX(), entity.getY(), entity.getZ());
        bot.updateItemInHand(hand);
        InteractionResult interactionResult = entity.interactAt(bot, vec3, hand);
        if (!interactionResult.consumesAction()) {
            interactionResult = bot.interactOn(hitResult.getEntity(), hand);
        }

        if (shouldSwing(interactionResult)) {
            bot.swing(hand);
        }

        return interactionResult;
    }

    @Override
    public Object asCraft() {
        return new CraftUseItemToAction(this);
    }
}
