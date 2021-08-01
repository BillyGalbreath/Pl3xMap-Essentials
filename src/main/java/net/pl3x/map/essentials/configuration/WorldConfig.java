package net.pl3x.map.essentials.configuration;

import net.pl3x.map.api.MapWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class WorldConfig {
    private static final Map<UUID, WorldConfig> configs = new HashMap<>();

    public static void reload() {
        configs.clear();
    }

    public static WorldConfig get(MapWorld world) {
        WorldConfig config = configs.get(world.uuid());
        if (config == null) {
            config = new WorldConfig(world);
            configs.put(world.uuid(), config);
        }
        return config;
    }

    private final String worldName;

    public WorldConfig(MapWorld world) {
        this.worldName = world.name();
        init();
    }

    public void init() {
        Config.readConfig(WorldConfig.class, this);
    }

    private void set(String path, Object val) {
        Config.CONFIG.addDefault("world-settings.default." + path, val);
        Config.CONFIG.set("world-settings.default." + path, val);
        if (Config.CONFIG.get("world-settings." + worldName + "." + path) != null) {
            Config.CONFIG.addDefault("world-settings." + worldName + "." + path, val);
            Config.CONFIG.set("world-settings." + worldName + "." + path, val);
        }
    }

    private boolean getBoolean(String path, boolean def) {
        Config.CONFIG.addDefault("world-settings.default." + path, def);
        return Config.CONFIG.getBoolean("world-settings." + worldName + "." + path, Config.CONFIG.getBoolean("world-settings.default." + path));
    }

    private int getInt(String path, int def) {
        Config.CONFIG.addDefault("world-settings.default." + path, def);
        return Config.CONFIG.getInt("world-settings." + worldName + "." + path, Config.CONFIG.getInt("world-settings.default." + path));
    }

    private String getString(String path, String def) {
        Config.CONFIG.addDefault("world-settings.default." + path, def);
        return Config.CONFIG.getString("world-settings." + worldName + "." + path, Config.CONFIG.getString("world-settings.default." + path));
    }

    public boolean ENABLED = true;

    private void worldSettings() {
        ENABLED = getBoolean("enabled", ENABLED);
    }

    public String WARPS_LABEL = "Warps";
    public boolean WARPS_SHOW_CONTROLS = true;
    public boolean WARPS_CONTROLS_HIDDEN = false;
    public int WARPS_PRIORITY = 999;
    public int WARPS_ZINDEX = 999;
    public String WARPS_TOOLTIP = "{warp}";

    public String HOMES_LABEL = "Homes";
    public boolean HOMES_SHOW_CONTROLS = true;
    public boolean HOMES_CONTROLS_HIDDEN = false;
    public int HOMES_PRIORITY = 995;
    public int HOMES_ZINDEX = 999;
    public String HOMES_TOOLTIP = "{player}'s home";

    private void layerSettings() {
        WARPS_LABEL = getString("warps.label", WARPS_LABEL);
        WARPS_SHOW_CONTROLS = getBoolean("warps.show-controls", WARPS_SHOW_CONTROLS);
        WARPS_CONTROLS_HIDDEN = getBoolean("warps.hide-by-default", WARPS_CONTROLS_HIDDEN);
        WARPS_PRIORITY = getInt("warps.priority", WARPS_PRIORITY);
        WARPS_ZINDEX = getInt("warps.z-index", WARPS_ZINDEX);
        WARPS_TOOLTIP = getString("warps.tooltip", WARPS_TOOLTIP);

        HOMES_LABEL = getString("homes.label", HOMES_LABEL);
        HOMES_SHOW_CONTROLS = getBoolean("homes.show-controls", HOMES_SHOW_CONTROLS);
        HOMES_CONTROLS_HIDDEN = getBoolean("homes.hide-by-default", HOMES_CONTROLS_HIDDEN);
        HOMES_PRIORITY = getInt("homes.priority", HOMES_PRIORITY);
        HOMES_ZINDEX = getInt("homes.z-index", HOMES_ZINDEX);
        HOMES_TOOLTIP = getString("homes.tooltip", HOMES_TOOLTIP);
    }

    public int ICON_SIZE = 16;
    public int ICON_ANCHOR_X = 8;
    public int ICON_ANCHOR_Z = 16;

    private void iconSettings() {
        ICON_SIZE = getInt("icon.size", ICON_SIZE);
        ICON_ANCHOR_X = getInt("icon.anchor.x", ICON_ANCHOR_X);
        ICON_ANCHOR_Z = getInt("icon.anchor.z", ICON_ANCHOR_Z);
    }
}
