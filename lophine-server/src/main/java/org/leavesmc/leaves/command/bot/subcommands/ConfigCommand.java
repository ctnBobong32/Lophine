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

package org.leavesmc.leaves.command.bot.subcommands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fun.bm.lophine.config.modules.function.FakeplayerConfig;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.bot.ServerBot;
import org.leavesmc.leaves.bot.agent.Configs;
import org.leavesmc.leaves.bot.agent.configs.AbstractBotConfig;
import org.leavesmc.leaves.command.CommandContext;
import org.leavesmc.leaves.command.CustomArgumentNode;
import org.leavesmc.leaves.command.LiteralNode;
import org.leavesmc.leaves.command.bot.BotSubcommand;

import java.util.Collection;
import java.util.function.Supplier;

import static io.papermc.paper.adventure.PaperAdventure.asAdventure;
import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.JoinConfiguration.spaces;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class ConfigCommand extends BotSubcommand {

    public ConfigCommand() {
        super("config");
        children(BotArgument::new);
    }

    @Override
    public boolean requires(@NotNull CommandSourceStack source) {
        return FakeplayerConfig.canModifyConfig && super.requires(source);
    }

    private static class BotArgument extends CustomArgumentNode<ServerBot, String> {

        private BotArgument() {
            super("bot", new org.leavesmc.leaves.command.bot.BotArgument());
            Configs.getConfigs().stream()
                    .map(this::configNodeCreator)
                    .forEach(this::children);
        }

        @Contract(pure = true)
        private @NotNull Supplier<LiteralNode> configNodeCreator(AbstractBotConfig<?, ?, ?> config) {
            return () -> new ConfigNode<>(config);
        }

        public static @NotNull ServerBot getBot(@NotNull CommandContext context) throws CommandSyntaxException {
            return context.getCustomArgument(BotArgument.class);
        }

        @Override
        protected boolean execute(CommandContext context) throws CommandSyntaxException {
            ServerBot bot = BotArgument.getBot(context);
            CommandSender sender = context.getSender();
            Collection<AbstractBotConfig<?, ?, ?>> botConfigs = bot.getAllConfigs();
            sender.sendMessage(join(spaces(),
                    text("Bot", GRAY),
                    asAdventure(bot.getDisplayName()).append(text("'s", GRAY)),
                    text("configs:", GRAY)
            ));
            for (AbstractBotConfig<?, ?, ?> botConfig : botConfigs) {
                sender.sendMessage(join(spaces(),
                        botConfig.getNameComponent(),
                        text("=", GRAY),
                        text(String.valueOf(botConfig.getValue()), AQUA)
                ));
            }
            return true;
        }
    }

    private static class ConfigNode<Value> extends LiteralNode {
        private final AbstractBotConfig<Value, ?, ?> config;

        private ConfigNode(@NotNull AbstractBotConfig<Value, ?, ?> config) {
            super(config.getName());
            this.config = config;
        }

        @Override
        protected ArgumentBuilder<CommandSourceStack, ?> compileBase() {
            RequiredArgumentBuilder<CommandSourceStack, ?> argument = config.getArgument()
                    .compile()
                    .executes(mojangCtx -> {
                        CommandContext ctx = new CommandContext(mojangCtx);
                        return executeSet(ctx) ? 1 : 0;
                    });
            return super.compileBase()
                    .then(argument);
        }

        @Override
        protected boolean execute(@NotNull CommandContext context) throws CommandSyntaxException {
            ServerBot bot = BotArgument.getBot(context);
            AbstractBotConfig<Value, ?, ?> botConfig = bot.getConfig(config);
            context.getSender().sendMessage(join(spaces(),
                    text("Bot", GRAY),
                    asAdventure(bot.getDisplayName()).append(text("'s", GRAY)),
                    text("config", GRAY),
                    botConfig.getNameComponent(),
                    text("is", GRAY),
                    text(String.valueOf(bot.getConfig(config).getValue()), AQUA)
            ));
            return true;
        }

        private boolean executeSet(CommandContext context) throws CommandSyntaxException {
            ServerBot bot = BotArgument.getBot(context);
            AbstractBotConfig<Value, ?, ?> botConfig = bot.getConfig(config);
            try {
                botConfig.setValue(botConfig.loadFromCommand(context));
            } catch (ClassCastException e) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
            }
            context.getSender().sendMessage(join(spaces(),
                    text("Bot", GRAY),
                    asAdventure(bot.getDisplayName()).append(text("'s", GRAY)),
                    text("config", GRAY),
                    botConfig.getNameComponent(),
                    text("changed to", GRAY),
                    text(String.valueOf(botConfig.getValue()), AQUA)
            ));
            return true;
        }
    }
}
