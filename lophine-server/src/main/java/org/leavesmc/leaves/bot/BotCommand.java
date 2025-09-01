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

package org.leavesmc.leaves.bot;

import com.mojang.logging.LogUtils;
import fun.bm.lophine.config.modules.function.FakeplayerConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.bot.agent.AbstractBotConfig;
import org.leavesmc.leaves.bot.agent.Actions;
import org.leavesmc.leaves.bot.agent.Configs;
import org.leavesmc.leaves.bot.agent.actions.ServerBotAction;
import org.leavesmc.leaves.command.CommandArgumentResult;
import org.leavesmc.leaves.entity.bot.Bot;
import org.leavesmc.leaves.event.bot.BotActionStopEvent;
import org.leavesmc.leaves.event.bot.BotConfigModifyEvent;
import org.leavesmc.leaves.event.bot.BotCreateEvent;
import org.leavesmc.leaves.event.bot.BotRemoveEvent;
import org.leavesmc.leaves.plugin.MinecraftInternalPlugin;
import org.slf4j.Logger;

import java.util.*;

import static net.kyori.adventure.text.Component.text;

public class BotCommand extends Command {
    private static final Logger LOGGER = LogUtils.getLogger();

    public BotCommand() {
        super("bot");
        this.setPermission("lophine.bot");
        this.setDescription("FakePlayer Command");
        this.setUsage("/bot <create|remove|list|action|config|save|load> [args...]");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        if (!FakeplayerConfig.enable) {
            sender.sendMessage(Component.text("Fakeplayer feature is disabled!").color(TextColor.color(255, 0, 0)));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /bot <create|remove|list|action|config|save|load> [args...]").color(TextColor.color(255, 255, 0)));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "create" -> {
                return handleCreate(sender, args);
            }
            case "remove" -> {
                return handleRemove(sender, args);
            }
            case "list" -> {
                return handleList(sender, args);
            }
            case "action" -> {
                return handleAction(sender, args);
            }
            case "config" -> {
                return handleConfig(sender, args);
            }
            case "save" -> {
                return handleSave(sender, args);
            }
            case "load" -> {
                return handleLoad(sender, args);
            }
            default -> {
                sender.sendMessage(Component.text("Unknown subcommand: " + subCommand).color(TextColor.color(255, 0, 0)));
                sender.sendMessage(Component.text("Available commands: create, remove, list, action, config, save, load").color(TextColor.color(255, 255, 0)));
            }
        }

        return true;
    }

    private boolean handleCreate(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(text("Use /bot create <name> [skin_name] to create a fakeplayer", NamedTextColor.RED));
            return true;
        }

        String botName = args[1];
        String fullName = BotUtil.getFullName(botName);
        if (this.canCreate(sender, fullName)) {
            BotCreateState.Builder builder = BotCreateState.builder(botName, Bukkit.getWorlds().getFirst().getSpawnLocation())
                    .createReason(BotCreateEvent.CreateReason.COMMAND)
                    .creator(sender);

            if (args.length >= 3) {
                builder.skinName(args[2]);
            }

            if (sender instanceof Player player) {
                builder.location(player.getLocation());
            } else if (sender instanceof ConsoleCommandSender) {
                if (args.length >= 7) {
                    try {
                        World world = Bukkit.getWorld(args[3]);
                        double x = Double.parseDouble(args[4]);
                        double y = Double.parseDouble(args[5]);
                        double z = Double.parseDouble(args[6]);
                        if (world != null) {
                            builder.location(new Location(world, x, y, z));
                        }
                    } catch (Exception e) {
                        LOGGER.warn("Can't build location", e);
                    }
                }
            }

            builder.spawnWithSkin(null);
        }
        return true;
    }

    private boolean handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2 || args.length > 5) {
            sender.sendMessage(text("Usage: /bot remove <name> [hour] [minute] [second]", NamedTextColor.RED));
            return true;
        }

        String botName = args[1];
        BotList botList = BotList.INSTANCE;
        ServerBot bot = botList.getBotByName(BotUtil.getFullName(botName));

        if (bot == null) {
            sender.sendMessage(text("This fakeplayer is not in server", NamedTextColor.RED));
            return true;
        }

        if (args.length == 3 && args[2].equals("cancel")) {
            if (bot.removeTaskId == -1) {
                sender.sendMessage(text("This fakeplayer is not scheduled to be removed", NamedTextColor.RED));
                return true;
            }
            Bukkit.getScheduler().cancelTask(bot.removeTaskId);
            bot.removeTaskId = -1;
            sender.sendMessage(text("Remove cancel"));
            return true;
        }

        if (args.length > 2) {
            long time = 0;
            int h;
            long s = 0;
            long m = 0;

            try {
                h = Integer.parseInt(args[2]);
                if (h < 0) {
                    throw new NumberFormatException();
                }
                time += ((long) h) * 3600 * 20;
                if (args.length > 3) {
                    m = Long.parseLong(args[3]);
                    if (m > 59 || m < 0) {
                        throw new NumberFormatException();
                    }
                    time += m * 60 * 20;
                }
                if (args.length > 4) {
                    s = Long.parseLong(args[4]);
                    if (s > 59 || s < 0) {
                        throw new NumberFormatException();
                    }
                    time += s * 20;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(text("Usage: /bot remove <name> [hour] [minute] [second]", NamedTextColor.RED));
                return true;
            }

            boolean isReschedule = bot.removeTaskId != -1;

            if (isReschedule) {
                Bukkit.getScheduler().cancelTask(bot.removeTaskId);
            }
            bot.removeTaskId = Bukkit.getScheduler().runTaskLater(MinecraftInternalPlugin.INSTANCE, () -> {
                bot.removeTaskId = -1;
                botList.removeBot(bot, BotRemoveEvent.RemoveReason.COMMAND, sender, false);
            }, time).getTaskId();

            sender.sendMessage("This fakeplayer will be removed in " + h + "h " + m + "m " + s + "s" + (isReschedule ? " (rescheduled)" : ""));
            return true;
        }

        botList.removeBot(bot, BotRemoveEvent.RemoveReason.COMMAND, sender, false);
        sender.sendMessage(text("Removed fakeplayer: " + botName, NamedTextColor.GREEN));
        return true;
    }

    private boolean handleList(CommandSender sender, String[] args) {
        BotList botList = BotList.INSTANCE;

        if (args.length < 2) {
            Map<World, List<String>> botMap = new HashMap<>();
            for (World world : Bukkit.getWorlds()) {
                botMap.put(world, new ArrayList<>());
            }

            for (ServerBot bot : botList.bots) {
                Bot bukkitBot = bot.getBukkitEntity();
                botMap.get(bukkitBot.getWorld()).add(bukkitBot.getName());
            }

            sender.sendMessage("Total number: (" + botList.bots.size() + "/" + FakeplayerConfig.limit + ")");
            for (World world : botMap.keySet()) {
                sender.sendMessage(world.getName() + "(" + botMap.get(world).size() + "): " + formatPlayerNameList(botMap.get(world)));
            }
        } else {
            World world = Bukkit.getWorld(args[1]);

            if (world == null) {
                sender.sendMessage(text("Unknown world", NamedTextColor.RED));
                return true;
            }

            List<String> snowBotList = new ArrayList<>();
            for (ServerBot bot : botList.bots) {
                Bot bukkitBot = bot.getBukkitEntity();
                if (bukkitBot.getWorld() == world) {
                    snowBotList.add(bukkitBot.getName());
                }
            }

            sender.sendMessage(world.getName() + "(" + snowBotList.size() + "): " + formatPlayerNameList(snowBotList));
        }
        return true;
    }

    private boolean handleAction(CommandSender sender, String[] args) {
        if (!FakeplayerConfig.canUseAction) {
            sender.sendMessage(text("Bot action feature is disabled!", NamedTextColor.RED));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(text("Use /bot action <name> <action> to make fakeplayer do action", NamedTextColor.RED));
            return true;
        }

        ServerBot bot = BotList.INSTANCE.getBotByName(args[1]);
        if (bot == null) {
            sender.sendMessage(text("This fakeplayer is not in server", NamedTextColor.RED));
            return true;
        }

        switch (args[2].toLowerCase()) {
            case "list" -> {
                sender.sendMessage(bot.getScoreboardName() + "'s action list:");
                for (int i = 0; i < bot.getBotActions().size(); i++) {
                    sender.sendMessage(i + " " + bot.getBotActions().get(i).getName());
                }
            }
            case "start" -> executeActionStart(bot, sender, args);
            case "stop" -> executeActionStop(bot, sender, args);
            default -> sender.sendMessage(text("Unknown action command. Use: list, start, stop", NamedTextColor.RED));
        }
        return true;
    }

    private void executeActionStart(ServerBot bot, CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(text("Invalid action", NamedTextColor.RED));
            return;
        }
        ServerBotAction<?> action = Actions.getForName(args[3]);
        if (action == null) {
            sender.sendMessage(text("Invalid action", NamedTextColor.RED));
            return;
        }

        CraftPlayer player;
        if (sender instanceof CraftPlayer) {
            player = (CraftPlayer) sender;
        } else {
            player = bot.getBukkitEntity();
        }

        String[] realArgs = Arrays.copyOfRange(args, 4, args.length);
        ServerBotAction<?> newAction;
        try {
            newAction = action.create();
            newAction.loadCommand(player.getHandle(), action.getArgument().parse(0, realArgs));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(text("Action create error, please check your arguments, " + e.getMessage(), NamedTextColor.RED));
            return;
        }

        if (bot.addBotAction(newAction, sender)) {
            sender.sendMessage("Action " + action.getName() + " has been issued to " + bot.getName().getString());
        }
    }

    private void executeActionStop(ServerBot bot, CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(text("Invalid index", NamedTextColor.RED));
            return;
        }

        String index = args[3];
        if (index.equals("all")) {
            Set<ServerBotAction<?>> forRemoval = new HashSet<>();
            for (int i = 0; i < bot.getBotActions().size(); i++) {
                ServerBotAction<?> action = bot.getBotActions().get(i);
                BotActionStopEvent event = new BotActionStopEvent(
                        bot.getBukkitEntity(), action.getName(), action.getUUID(), BotActionStopEvent.Reason.COMMAND, sender
                );
                event.callEvent();
                if (!event.isCancelled()) {
                    forRemoval.add(action);
                    action.stop(bot, BotActionStopEvent.Reason.COMMAND);
                }
            }
            bot.getBotActions().removeAll(forRemoval);
            sender.sendMessage(bot.getScoreboardName() + "'s action list cleared.");
            return;
        }
        try {
            int i = Integer.parseInt(index);
            if (i < 0 || i >= bot.getBotActions().size()) {
                sender.sendMessage(text("Invalid index", NamedTextColor.RED));
                return;
            }

            ServerBotAction<?> action = bot.getBotActions().get(i);
            BotActionStopEvent event = new BotActionStopEvent(
                    bot.getBukkitEntity(), action.getName(), action.getUUID(), BotActionStopEvent.Reason.COMMAND, sender
            );
            event.callEvent();
            if (!event.isCancelled()) {
                action.stop(bot, BotActionStopEvent.Reason.COMMAND);
                bot.getBotActions().remove(i);
                sender.sendMessage(bot.getScoreboardName() + "'s " + action.getName() + " stopped.");
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(text("Invalid index", NamedTextColor.RED));
        }
    }

    private boolean handleConfig(CommandSender sender, String[] args) {
        if (!FakeplayerConfig.canModifyConfig) {
            sender.sendMessage(text("Bot config feature is disabled!", NamedTextColor.RED));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(text("Use /bot config <name> <config> to modify fakeplayer's config", NamedTextColor.RED));
            return true;
        }

        ServerBot bot = BotList.INSTANCE.getBotByName(args[1]);
        if (bot == null) {
            sender.sendMessage(text("This fakeplayer is not in server", NamedTextColor.RED));
            return true;
        }

        if (!Configs.getConfigNames().contains(args[2])) {
            sender.sendMessage(text("This config is not accept", NamedTextColor.RED));
            return true;
        }

        AbstractBotConfig<?> config = bot.getConfig(Objects.requireNonNull(Configs.getConfig(args[2])));
        if (args.length < 4) {
            config.getMessage().forEach(sender::sendMessage);
        } else {
            String[] realArgs = Arrays.copyOfRange(args, 3, args.length);

            BotConfigModifyEvent event = new BotConfigModifyEvent(bot.getBukkitEntity(), config.getName(), realArgs, sender);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            CommandArgumentResult result = config.getArgument().parse(0, realArgs);

            try {
                config.setFromCommand(result);
                config.getChangeMessage().forEach(sender::sendMessage);
            } catch (IllegalArgumentException e) {
                sender.sendMessage(text(e.getMessage(), NamedTextColor.RED));
            }
        }
        return true;
    }

    private boolean handleSave(CommandSender sender, String[] args) {
        if (!FakeplayerConfig.canManualSaveAndLoad) {
            sender.sendMessage(text("Bot save/load feature is disabled!", NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(text("Use /bot save <name> to save a fakeplayer", NamedTextColor.RED));
            return true;
        }

        BotList botList = BotList.INSTANCE;
        ServerBot bot = botList.getBotByName(args[1]);

        if (bot == null) {
            sender.sendMessage(text("This fakeplayer is not in server", NamedTextColor.RED));
            return true;
        }

        if (botList.removeBot(bot, BotRemoveEvent.RemoveReason.COMMAND, sender, true)) {
            sender.sendMessage(bot.getScoreboardName() + " saved to " + bot.createState.realName());
        }
        return true;
    }

    private boolean handleLoad(CommandSender sender, String[] args) {
        if (!FakeplayerConfig.canManualSaveAndLoad) {
            sender.sendMessage(text("Bot save/load feature is disabled!", NamedTextColor.RED));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(text("Use /bot load <name> to load a fakeplayer", NamedTextColor.RED));
            return true;
        }

        String realName = args[1];
        BotList botList = BotList.INSTANCE;
        if (!botList.getSavedBotList().contains(realName)) {
            sender.sendMessage(text("This fakeplayer is not saved", NamedTextColor.RED));
            return true;
        }

        if (botList.loadNewBot(realName) == null) {
            sender.sendMessage(text("Can't load bot, please check", NamedTextColor.RED));
        } else {
            sender.sendMessage(text("Successfully loaded fakeplayer: " + realName, NamedTextColor.GREEN));
        }
        return true;
    }

    @NotNull
    private static String formatPlayerNameList(@NotNull List<String> list) {
        if (list.isEmpty()) {
            return "";
        }
        String string = list.toString();
        return string.substring(1, string.length() - 1);
    }

    private boolean canCreate(CommandSender sender, @NotNull String name) {
        BotList botList = BotList.INSTANCE;
        if (!name.matches("^[a-zA-Z0-9_]{4,16}$")) {
            sender.sendMessage(text("This name is illegal", NamedTextColor.RED));
            return false;
        }

        if (Bukkit.getPlayerExact(name) != null || botList.getBotByName(name) != null) {
            sender.sendMessage(text("This player is in server", NamedTextColor.RED));
            return false;
        }

        if (FakeplayerConfig.unableNames.contains(name)) {
            sender.sendMessage(text("This name is not allowed", NamedTextColor.RED));
            return false;
        }

        if (botList.bots.size() >= FakeplayerConfig.limit) {
            sender.sendMessage(text("Fakeplayer limit is full", NamedTextColor.RED));
            return false;
        }

        return true;
    }
}