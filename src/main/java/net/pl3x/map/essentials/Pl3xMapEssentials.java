package net.pl3x.map.essentials;

import net.pl3x.map.essentials.configuration.Config;
import net.pl3x.map.essentials.hook.Pl3xMapHook;
import net.pl3x.map.essentials.listener.EssentialsListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Pl3xMapEssentials extends JavaPlugin {
    private static Pl3xMapEssentials instance;

    public Pl3xMapEssentials() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload(this);

        if (!new File(getDataFolder(), "warp.png").exists()) {
            saveResource("warp.png", false);
        }

        if (!new File(getDataFolder(), "home.png").exists()) {
            saveResource("home.png", false);
        }

        if (!getServer().getPluginManager().isPluginEnabled("Essentials")) {
            Logger.severe("Essentials not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!getServer().getPluginManager().isPluginEnabled("Pl3xMap")) {
            Logger.severe("Pl3xMap not found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!Config.WARP_FEATURE && !Config.HOME_FEATURE && !Config.DEBUG_MODE) {
            Logger.severe("You have neither warps nor homes enabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        Pl3xMapHook.load(this);

        getServer().getPluginManager().registerEvents(new EssentialsListener(), this);
    }

    @Override
    public void onDisable() {
        Pl3xMapHook.disable();
    }

    public static Pl3xMapEssentials getInstance() {
        return instance;
    }
}
