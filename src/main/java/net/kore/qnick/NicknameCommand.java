package net.kore.qnick;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.TextArgument;
import net.kore.qnick.utils.Nickname;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class NicknameCommand {
    protected static CommandTree getCommand() {
        return new CommandTree("nickname")
                .withAliases("nick")
                .executes((target, args) -> {
                    target.sendMessage(Component.text("Usage: /nick <set/get/list/clear>").color(NamedTextColor.GOLD));
                })
                .then(new LiteralArgument("set")
                        .withPermission(CommandPermission.fromString("qnick.command.nick.set"))
                        .then(
                                new TextArgument("name")
                                        .withPermission(CommandPermission.fromString("qnick.command.nick.set"))
                                        .executes((target, args) -> {
                                            //
                                            // Set own nickname
                                            //
                                            if (!(target instanceof Player)) {
                                                target.sendMessage("Must execute from player!");
                                                return;
                                            }
                                            Nickname.set((Player) target, Component.text((String) Objects.requireNonNull(args.get("name"))));
                                            target.sendMessage(Component.text("Your nickname has been set to " + args.get("name")).color(NamedTextColor.GOLD));
                                        })
                                        .then(
                                                new PlayerArgument("player")
                                                        .withPermission(CommandPermission.fromString("qnick.command.nick.setother"))
                                                        .executes((target, args) -> {
                                                            //
                                                            // Set other nickname
                                                            //
                                                            Player nicknameable = (Player) args.get("player");
                                                            target.sendMessage(Component.text(
                                                                    "Set " +
                                                                            Objects.requireNonNull(nicknameable).getName() +
                                                                            "'s nickname to " +
                                                                            args.get("name")
                                                            ).color(NamedTextColor.GOLD));
                                                            nicknameable.sendMessage(Component.text("Your nickname has been set to " + args.get("name")).color(NamedTextColor.GOLD));
                                                            Nickname.set(nicknameable, Component.text((String) Objects.requireNonNull(args.get("name"))));
                                                        })
                                        )
                        )
                )
                .then(new LiteralArgument("get")
                        .withPermission(CommandPermission.fromString("qnick.command.nick.get"))
                        .executes((target, args) -> {
                            //
                            // Get own nickname
                            //
                            if (!(target instanceof Player)) {
                                target.sendMessage("Must execute from player!");
                                return;
                            }
                            if (Nickname.get((Player) target) != null) {
                                target.sendMessage(Component.text("Your nickname is: ").color(NamedTextColor.GOLD).append(Objects.requireNonNull(Nickname.get((Player) target)).color(NamedTextColor.WHITE)));
                            } else {
                                target.sendMessage(Component.text("You have no nickname!").color(NamedTextColor.DARK_RED));
                            }
                        })
                        .then(
                                new PlayerArgument("player")
                                        .withPermission(CommandPermission.fromString("qnick.command.nick.getother"))
                                        .executes((target, args) -> {
                                            //
                                            // Get other nickname
                                            //
                                            Player nicknameable = (Player) args.get("player");
                                            if (Nickname.has(Objects.requireNonNull(nicknameable))) {
                                                target.sendMessage(
                                                        Objects.requireNonNull(
                                                                Nickname.get(
                                                                        Objects.requireNonNull(
                                                                                nicknameable
                                                                        )
                                                                )
                                                        )
                                                );
                                            } else {
                                                target.sendMessage(Component.text("Player has no nickname!").color(NamedTextColor.DARK_RED));
                                            }
                                        })
                        )
                )
                .then(new LiteralArgument("list")
                        .withPermission(CommandPermission.fromString("qnick.command.nick.list"))
                        .executes((target, args) -> {
                            //
                            // List all nicknames
                            //
                            target.sendMessage(Nickname.getList().color(NamedTextColor.GOLD));
                        })
                )
                .then(new LiteralArgument("clear")
                        .withPermission(CommandPermission.fromString("qnick.command.nick.clear"))
                        .executes((target, args) -> {
                            //
                            // Clear own nickname
                            //
                            if (!(target instanceof Player)) {
                                target.sendMessage("Must execute from player!");
                                return;
                            }
                            target.sendMessage(Component.text("Cleared your nickname").color(NamedTextColor.GOLD));
                            Nickname.remove((Player) target);
                        })
                        .then(
                                new PlayerArgument("player")
                                        .withPermission(CommandPermission.fromString("qnick.command.nick.clearother"))
                                        .executes((target, args) -> {
                                            //
                                            // Clear other nickname
                                            //
                                            Player nicknameable = (Player) args.get("player");
                                            Objects.requireNonNull(nicknameable).sendMessage(Component.text("Your nickname has been cleared").color(NamedTextColor.GOLD));
                                            target.sendMessage(Component.text("Cleared " + nicknameable.getName() + "'s nickname").color(NamedTextColor.GOLD));
                                            Nickname.remove(nicknameable);
                                        })
                        )
                );
    }
}
