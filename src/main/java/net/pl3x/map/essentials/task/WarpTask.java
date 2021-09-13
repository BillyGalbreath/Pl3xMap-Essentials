package net.pl3x.map.essentials.task;

import com.earth2me.essentials.Warps;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.ess3.api.InvalidWorldException;
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

public class WarpTask extends BukkitRunnable {
    private final MapWorld world;
    private final SimpleLayerProvider provider;
    private final WorldConfig worldConfig;

    private boolean stop;

    public WarpTask(MapWorld world, WorldConfig worldConfig, SimpleLayerProvider provider) {
        this.world = world;
        this.provider = provider;
        this.worldConfig = worldConfig;
    }

    @Override
    public void run() {
        if (stop) {
            cancel();
        }

        provider.clearMarkers();

        Warps warps = EssentialsHook.getWarps();

        warps.getList().forEach(warpName -> {
            try {
                Location loc = warps.getWarp(warpName);
                if (loc.getWorld().getUID().equals(world.uuid())) {
                    this.handle(warpName, loc);
                }
            } catch (WarpNotFoundException | InvalidWorldException ignore) {
            }
        });
    }

    private void handle(String warpName, Location loc) {
        Icon icon = Marker.icon(Point.fromLocation(loc), Pl3xMapHook.warpIconKey, worldConfig.ICON_SIZE);

        icon.anchor(Point.of(worldConfig.ICON_ANCHOR_X, worldConfig.ICON_ANCHOR_Z));

        icon.markerOptions(MarkerOptions.builder()
                .hoverTooltip(worldConfig.WARPS_TOOLTIP
                        .replace("{warp}", warpName)
                )
        );

        String markerid = "essentials_" + world.name() + "_warp_" + warpName.hashCode();
        this.provider.addMarker(Key.of(markerid), icon);
    }

    public void disable() {
        cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
