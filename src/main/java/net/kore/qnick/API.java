package net.kore.qnick;

import net.kore.qnick.utils.Nickname;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * <h2>API class</h2>
 * <p>Allows external plugins to access certain methods from the QNick plugin</p>
 *
 * @author Nova
 */
@SuppressWarnings({"unused"})
public class API implements QNickAPI {
    /**
     * <h2>Gets a player from a nickname component</h2>
     * <b>Will return null if no player is found</b>
     * <p>To get a nickname from a player look at {@link #getNickFromPlayer(Player)}</p>
     *
     * @return {@link Player} or <code>null</code>
     * @since 0.1.0
     */
    @Override
    public Player getPlayerFromNick(Component nick) {
        if (QNick.getSQL() != null) {
            return Bukkit.getPlayer(QNick.getSQL().getUUID(MiniMessage.miniMessage().serialize(nick)));
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Nickname.has(player)) {
                continue;
            }
            if (!Objects.equals(Nickname.get(player), nick)) {
                continue;
            }
            return player;
        }
        return null;
    }

    @Override
    public UUID getUUIDFromNick(Component nick) {
        if (QNick.getSQL() != null) {
            return QNick.getSQL().getUUID(MiniMessage.miniMessage().serialize(nick));
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Nickname.has(player)) {
                continue;
            }
            if (!Objects.equals(Nickname.get(player), nick)) {
                continue;
            }
            return player.getUniqueId();
        }
        return null;
    }

    @Override
    public UUID getUUIDFromNick(String nick) {
        if (QNick.getSQL() != null) {
            return QNick.getSQL().getUUID(nick);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!Nickname.has(player)) {
                continue;
            }
            if (!Objects.equals(Nickname.get(player), MiniMessage.miniMessage().deserialize(nick))) {
                continue;
            }
            return player.getUniqueId();
        }
        return null;
    }


    /**
     * <h2>Gets a nickname from a Player</h2>
     * <b>Will return null if the player has no nickname</b>
     * <p>To get a player from a Nickname use {@link #getPlayerFromNick(Component)}</p>
     *
     * @return {@link Component} or <code>null</code>
     * @since 0.1.0
     */
    @Override
    public Component getNickFromPlayer(Player player) {
        return Nickname.get(player);
    }


    /**
     * <h2>Set a player's nickname using a component</h2>
     * <p>To never overwrite a player's nickname check {@link #setNick(Player, Component, boolean)}</p>
     *
     * @return true (if it didn't fail) / false (if something went wrong)
     * @since 0.1.0
     */
    @Override
    public boolean setNick(Player player, Component nick) {
        try {
            Nickname.set(player, nick);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * <h2>Set a player's nickname using a component</h2>
     * <p>To never check if a player has a nickname use {@link #setNick(Player, Component)}</p>
     *
     * @return true (if it didn't fail) / false (if something went wrong)
     * @since 0.1.0
     */
    @Override
    public boolean setNick(Player player, Component nick, boolean replace) {
        if (!replace && Nickname.has(player)) {
            return false;
        }
        return setNick(player, nick);
    }


    /**
     * <h2>Check if a player has a nickname</h2>
     *
     * @return true (has a nickname) / false (doesn't have a nickname)
     * @since 0.1.0
     */
    @Override
    public boolean hasNick(Player player) {
        return Nickname.has(player);
    }


    /**
     * <h2>Get a map containing the player and nickname component</h2>
     *
     * @return {@link Map}&lt;{@link Player}, {@link Component}&gt;
     * @since 0.1.0
     */
    @Override
    public Map<Player, Component> getNickMap() {
        Map<Player, Component> nicked = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Nickname.has(player)) {
                nicked.put(player, Nickname.get(player));
            }
        }
        return nicked;
    }
}
