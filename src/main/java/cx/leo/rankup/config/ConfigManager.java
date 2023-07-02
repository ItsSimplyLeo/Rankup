package cx.leo.rankup.config;

import cx.leo.rankup.yaml.Yaml;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final Yaml config, menus;

    public ConfigManager(final JavaPlugin plugin) {
        this.config = new Yaml(plugin, "config");
        this.menus = new Yaml(plugin, "menus");
    }

    public void reload() {
        this.config.reload();
        this.menus.reload();
    }

    public Yaml getConfig() {
        return config;
    }

    public Yaml getMenus() {
        return menus;
    }
}
