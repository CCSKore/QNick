package net.kore.qnick;

import net.kore.qnick.utils.Nickname;
import net.kore.qnick.utils.SQL;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

@SuppressWarnings({"unused"})
public class QNick extends JavaPlugin implements Listener {
    private static QNick plugin;
    private static QNickAPI api = null;
    private static SQL sql = null;
    private static NamespacedKey nickKey;

    protected static QNick getPlugin() {
        return plugin;
    }
    public static Logger getLog() {
        return QNick.getPlugin().getLogger();
    }
    public static SQL getSQL() {
        return sql;
    }
    public static NamespacedKey getNickKey() {
        return nickKey;
    }
    public static QNickAPI getAPI() {
        return api;
    }

    @Override
    public void onEnable() {
        plugin = this;
        nickKey = new NamespacedKey(this, "nick");
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PAPI().register();
        }
        if (getConfig().getBoolean("apienabled", true)) {
            api = new API();
        }
        if (getConfig().getBoolean("command", true)) {
            NicknameCommand.getCommand().register();
        }
        if (getConfig().getBoolean("changenameonjoin", true)) {
            Bukkit.getPluginManager().registerEvents(this, this);
        }

        if (getConfig().getBoolean("sql", false)) {
            String dburl = "jdbc:mysql://" + getConfig().getString("sqloptions.host") + ":" + getConfig().getString("sqloptions.port") + "/" + getConfig().getString("sqloptions.db");
            sql = new SQL(dburl, getConfig().getString("sqloptions.username"), getConfig().getString("sqloptions.password"));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        if (sql == null) {
            if (Nickname.has(event.getPlayer())) {
                event.getPlayer().displayName(Nickname.get(event.getPlayer()));
            }
        } else {
            if (sql.hasNick(event.getPlayer().getUniqueId(), false)) {
                event.getPlayer().displayName(MiniMessage.miniMessage().deserialize(sql.getNick(event.getPlayer().getUniqueId(), false)));
            }
        }
    }
}
