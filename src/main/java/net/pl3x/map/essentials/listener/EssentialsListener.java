package net.pl3x.map.essentials.listener;

import net.ess3.api.events.VanishStatusChangeEvent;
import net.pl3x.map.essentials.configuration.Config;
import net.pl3x.map.essentials.hook.EssentialsHook;
import net.pl3x.map.essentials.hook.Pl3xMapHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EssentialsListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVanishStatusChange(VanishStatusChangeEvent event) {
        if (Config.HIDE_VANISHED) {
            Player player = event.getAffected().getBase();
            boolean vanished = event.getValue();
            Pl3xMapHook.api().playerManager().hidden(player.getUniqueId(), vanished);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Config.HIDE_VANISHED) {
            Player player = event.getPlayer();
            if (EssentialsHook.isVanished(player)) {
                Pl3xMapHook.api().playerManager().hide(player.getUniqueId());
            }
        }

    }
}
