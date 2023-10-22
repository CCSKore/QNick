package net.kore.qnick;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kore.qnick.utils.Nickname;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class PAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getAuthor() {
        return "Kore";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "qnick";
    }

    @Override
    public @NotNull String getVersion() {
        return Objects.requireNonNull(QNick.getPlugin().getPluginMeta()).getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        Player player = offlinePlayer.getPlayer();
        if (player == null && QNick.getSQL() == null) {
            return null;
        }

        if (params.equalsIgnoreCase("nickpersist")) {
            if (QNick.getSQL() != null) {
                if (Nickname.has(offlinePlayer.getUniqueId())) {
                    return PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(Nickname.get(offlinePlayer.getUniqueId())));
                } else {
                    return offlinePlayer.getName();
                }
            }
            if (Nickname.has(player)) {
                return PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(Nickname.get(player)));
            } else {
                return player.getName();
            }
        }

        if (params.equalsIgnoreCase("formatnick")) {
            if (QNick.getSQL() != null) {
                if (Nickname.has(offlinePlayer.getUniqueId())) {
                    return PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(Nickname.get(offlinePlayer.getUniqueId()))) + " (" + offlinePlayer.getName() + ")";
                } else {
                    return offlinePlayer.getName();
                }
            }
            if (Nickname.get(player) != null) {
                return Nickname.get(player) + " (" + player.getName() + ")";
            } else {
                return player.getName();
            }
        }

        return null;
    }
}