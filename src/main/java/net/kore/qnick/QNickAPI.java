package net.kore.qnick;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@SuppressWarnings({"unused"})
public interface QNickAPI {
    Player getPlayerFromNick(Component nick);
    UUID getUUIDFromNick(Component nick);
    UUID getUUIDFromNick(String nick);

    Component getNickFromPlayer(Player player);

    boolean setNick(Player player, Component nick);
    boolean setNick(Player player, Component nick, boolean replace);

    boolean hasNick(Player player);

    Map<Player, Component> getNickMap();
}
