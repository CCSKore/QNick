package net.kore.qnick.utils;

import net.kore.qnick.QNick;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@SuppressWarnings({"unused"})
public class Nickname {
    private static void setDisplayName(@NotNull Player player, @NotNull Component nick) {
        player.displayName(nick);
    }

    private static NamespacedKey getKey() {
        return QNick.getNickKey();
    }


    public static Component get(@NotNull Player player) {
        if (QNick.getSQL() != null) {
            String nick = QNick.getSQL().getNick(player.getUniqueId());
            if (nick == null) {
                return null;
            }
            return MiniMessage.miniMessage().deserialize(nick);
        }
        if (player.getPersistentDataContainer().has(getKey(), PersistentDataType.STRING)) {
            return MiniMessage.miniMessage().deserialize(Objects.requireNonNull(player.getPersistentDataContainer().get(getKey(), PersistentDataType.STRING)));
        }
        return null;
    }

    public static Component get(@NotNull UUID uuid) {
        if (QNick.getSQL() != null) {
            String nick = QNick.getSQL().getNick(uuid);
            if (nick == null) {
                return null;
            }
            return MiniMessage.miniMessage().deserialize(nick);
        }
        return null;
    }

    public static void set(@NotNull Player player, @NotNull Component nick) {
        if (QNick.getSQL() != null) {
            boolean result = QNick.getSQL().setNick(player.getUniqueId(), MiniMessage.miniMessage().serialize(nick));
            if (!result) {
                QNick.getLog().warning("Unable to set nickname, SQL failed.");
            } else {
                setDisplayName(player, nick);
            }
            return;
        }
        player.getPersistentDataContainer().set(getKey(), PersistentDataType.STRING, MiniMessage.miniMessage().serialize(nick));
        setDisplayName(player, nick);
    }

    public static void set(@NotNull UUID uuid, @NotNull Component nick) {
        if (QNick.getSQL() != null) {
            boolean result = QNick.getSQL().setNick(uuid, MiniMessage.miniMessage().serialize(nick));
            if (!result) {
                QNick.getLog().warning("Unable to set nickname, SQL failed.");
            }
        }
    }

    public static void remove(@NotNull Player player) {
        if (QNick.getSQL() != null) {
            boolean result = QNick.getSQL().removeNick(player.getUniqueId());
            if (!result) {
                QNick.getLog().warning("Unable to remove nickname, SQL failed.");
            } else {
                player.displayName(Component.text(player.getName()));
            }
            return;
        }
        player.getPersistentDataContainer().remove(getKey());
        player.displayName(Component.text(player.getName()));
    }

    public static void remove(@NotNull UUID uuid) {
        if (QNick.getSQL() != null) {
            boolean result = QNick.getSQL().removeNick(uuid);
            if (!result) {
                QNick.getLog().warning("Unable to remove nickname, SQL failed.");
            }
        }
    }

    public static boolean has(@NotNull Player player) {
        if (QNick.getSQL() != null) {
            return QNick.getSQL().hasNick(player.getUniqueId());
        }
        return player.getPersistentDataContainer().has(getKey(), PersistentDataType.STRING);
    }

    public static boolean has(@NotNull UUID uuid) {
        if (QNick.getSQL() != null) {
            return QNick.getSQL().hasNick(uuid);
        }
        return false;
    }

    public static Component getList() {
        Component base = Component.text("Nicknames:").color(NamedTextColor.DARK_AQUA);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (has(player)) {
                base = base.append(Component.newline());
                base = base.append(Component.newline());
                base = base.append(Component.text("----------------").color(NamedTextColor.WHITE));
                base = base.append(Component.newline());
                base = base.append(Component.text("Username: " + player.getName()).color(NamedTextColor.WHITE));
                base = base.append(Component.newline());
                base = base.append(Component.text("----------------").color(NamedTextColor.WHITE));
                base = base.append(Component.newline());
                base = base.append(Component.text("Nickname: ").color(NamedTextColor.WHITE).append(Objects.requireNonNull(get(player))));
                base = base.append(Component.newline());
                base = base.append(Component.text("----------------").color(NamedTextColor.WHITE));
            }
        }
        if (Objects.equals(base, Component.text("Nicknames:"))) {
            base = Component.text("No one has a nickname.").color(NamedTextColor.DARK_AQUA);
        }
        return base;
    }
}
