package fun.bm.lophine.utils;

import fun.bm.lophine.config.modules.misc.ContainerExpansionConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class ShulkerBoxesUtil {
    // Better ShulkerBoxes used
    public static HashMap<Player, ShulkerBoxBlockEntity> shulkerMap = new HashMap<>();
    public static HashMap<ShulkerBoxBlockEntity, Player> playerMap = new HashMap<>();

    // Stackable ShulkerBoxes part
    public static boolean shouldCheck() {
        return ContainerExpansionConfig.shulkerCount > 1 && ContainerExpansionConfig.shulkerCount <= 64;
    }

    public static boolean checkShulkerBox(ItemStack itemStack) {
        return shouldCheck() && checkIsShulkerBox(itemStack);
    }

    public static boolean checkIsShulkerBox(ItemStack itemStack) {
        return itemStack.getItem() instanceof BlockItem b && b.getBlock() instanceof ShulkerBoxBlock;
    }

    public static int getItemMaxCount(ItemStack itemStack) {
        if (checkShulkerBox(itemStack)) {
            return Math.clamp(ContainerExpansionConfig.shulkerCount, 1, 64);
        }
        return itemStack.getMaxStackSize();
    }

    public static int getShulkerBoxesMaxCountUnsafe() {
        return Math.clamp(ContainerExpansionConfig.shulkerCount, 1, 64);
    }

    public static boolean emptyShulkerBoxCheck(@NotNull ItemStack stack) {
        return stack.getComponents().getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).stream().findAny().isEmpty();
    }

    public static boolean isStackable(ItemStack itemStack) {
        return getItemMaxCount(itemStack) > 1 && (!itemStack.isDamageableItem() || !itemStack.isDamaged());
    }

    public static int getItemStackMaxCountReal(ItemStack stack) {
        CompoundTag nbt = Optional.ofNullable(stack.get(DataComponents.CUSTOM_DATA)).orElse(CustomData.EMPTY).copyTag();
        return nbt.getInt("Lophine.RealStackSize").orElse(stack.getMaxStackSize());
    }

    public static ItemStack encodeMaxStackSize(ItemStack itemStack) {
        int realMaxStackSize = getItemStackMaxCountReal(itemStack);
        int modifiedMaxStackSize = getItemMaxCount(itemStack);
        if (itemStack.getMaxStackSize() != modifiedMaxStackSize) {
            itemStack.set(DataComponents.MAX_STACK_SIZE, modifiedMaxStackSize);
            CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            nbt.putInt("Lophine.RealStackSize", realMaxStackSize);
            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        }
        return itemStack;
    }

    public static ItemStack decodeMaxStackSize(ItemStack itemStack) {
        int realMaxStackSize = getItemStackMaxCountReal(itemStack);
        if (itemStack.getMaxStackSize() != realMaxStackSize) {
            itemStack.set(DataComponents.MAX_STACK_SIZE, realMaxStackSize);
            CompoundTag nbt = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            nbt.remove("Lophine.RealStackSize");
            if (nbt.isEmpty()) {
                itemStack.remove(DataComponents.CUSTOM_DATA);
            } else {
                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
            }
        }
        return itemStack;
    }

    // Better ShulkerBox part
    public static boolean checkIfCanOpen(ItemStack itemStack) {
        return checkIsShulkerBox(itemStack) && itemStack.getCount() == 1;
    }

    public static boolean checkIfValid(Player player) {
        return shulkerMap.get(player) != null;
    }

    public static void openShulkerBox(Player player, ItemStack item, InteractionHand hand) {
        ItemStack itemInHand = item.copy();
        ShulkerBoxBlockEntity shulkerBoxEntity = new ShulkerBoxBlockEntity(
                player.blockPosition(),
                ((BlockItem) itemInHand.getItem()).getBlock().defaultBlockState()
        );
        ItemContainerContents container = itemInHand.getOrDefault(
                DataComponents.CONTAINER,
                ItemContainerContents.EMPTY
        );

        NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
        for (int i = 0; i < container.items.size(); i++) {
            items.set(i, container.items.get(i));
        }

        shulkerBoxEntity.setItems(items);

        shulkerBoxEntity.haveRealBlock = false;
        shulkerBoxEntity.shulkerHand = hand;
        shulkerBoxEntity.finalItem = itemInHand;
        shulkerBoxEntity.setLevel(player.level());

        shulkerMap.put(player, shulkerBoxEntity);
        playerMap.put(shulkerBoxEntity, player);

        if (player.openMenu(shulkerBoxEntity).isPresent()) {
            player.awardStat(Stats.OPEN_SHULKER_BOX);
        }
    }

    public static void shulkerBoxEntityCallBack(ShulkerBoxBlockEntity shulkerBoxEntity) {
        Player player = playerMap.get(shulkerBoxEntity);
        if (player != null) {
            InteractionHand hand = shulkerBoxEntity.shulkerHand;
            ItemStack currentItem = player.getItemInHand(hand);
            ItemStack copy = currentItem.copy();

            copy.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(shulkerBoxEntity.getItems()));

            player.setItemInHand(hand, copy);

            shulkerBoxEntity.finalItem = copy;
        }
    }

    public static void inventoryCallBack(boolean isMainHand, Player player) {
        ShulkerBoxBlockEntity entity = shulkerMap.get(player);
        if (entity != null && isMainHand == Objects.equals(entity.shulkerHand, InteractionHand.MAIN_HAND)) {
            closeScreen(entity, player);
        }
    }

    public static void closeScreen(ShulkerBoxBlockEntity entity, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ItemStack stack1 = entity.finalItem;
            ItemStack stack2 = player.containerMenu.getCarried();
            ItemStack stack = stack2.isEmpty() ? stack1 : stack2;
            if (!stack.isEmpty()) {
                if (!player.isAlive() || serverPlayer.hasDisconnected()) {
                    player.drop(stack, false);
                } else {
                    player.getInventory().placeItemBackInInventory(stack);
                }
            }
        }
        player.closeContainer();
    }

    public static void clearMap(Player p) {
        ShulkerBoxBlockEntity e = shulkerMap.get(p);
        if (e != null) playerMap.remove(e);
        shulkerMap.remove(p);
    }

    public static void clearMap(ShulkerBoxBlockEntity e) {
        Player p = playerMap.get(e);
        if (p != null) shulkerMap.remove(p);
        playerMap.remove(e);
    }
}