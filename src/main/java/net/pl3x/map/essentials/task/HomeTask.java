package net.pl3x.map.essentials.task;

import com.earth2me.essentials.UserMap;
import net.ess3.api.IUser;
import net.pl3x.map.api.Key;
import net.pl3x.map.api.MapWorld;
import net.pl3x.map.api.Point;
import net.pl3x.map.api.SimpleLayerProvider;
import net.pl3x.map.api.marker.Icon;
import net.pl3x.map.api.marker.Marker;
import net.pl3x.map.api.marker.MarkerOptions;
import net.pl3x.map.essentials.configuration.WorldConfig;
import net.pl3x.map.essentials.hook.EssentialsHook;
import net.pl3x.map.essentials.hook.Pl3xMapHook;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeTask extends BukkitRunnable {

    private final MapWorld world;
    private final WorldConfig worldConfig;
    private final SimpleLayerProvider provider;

    private boolean stop = false;

    public HomeTask(MapWorld world, WorldConfig worldConfig, SimpleLayerProvider provider) {
        this.world = world;
        this.provider = provider;
        this.worldConfig = worldConfig;
    }

    public void run() {
        if (stop) {
            cancel();
        }

        UserMap users = EssentialsHook.ess().getUserMap();
        if (users == null) {
            return;
        }

        users.getAllUniqueUsers().forEach(UUID -> {
            IUser user = EssentialsHook.ess().getUser(UUID);
            user.getHomes().forEach(home -> {
                try {
                    Location loc = user.getHome(home);
                    if (loc.getWorld().getUID().equals(world.uuid())) {
                        this.handle(user.getName(), loc, home);
                    }
                } catch (Exception ignore) {}
            });

        });

    }

    private void handle(String playerName, Location loc, String homeName) {
        Icon icon = Marker.icon(Point.fromLocation(loc), Pl3xMapHook.homeIconKey, worldConfig.ICON_SIZE);

        icon.anchor(Point.of(worldConfig.ICON_ANCHOR_X, worldConfig.ICON_ANCHOR_Z));

        icon.markerOptions(MarkerOptions.builder()
                .hoverTooltip(worldConfig.HOMES_TOOLTIP
                        .replace("{player}", playerName)
                )
        );

        String markerid = "essentials_" + world.name() + playerName + homeName;
        this.provider.addMarker(Key.of(markerid), icon);
    }

    public void disable() {
        cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
