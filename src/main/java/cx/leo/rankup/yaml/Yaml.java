package cx.leo.rankup.yaml;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Yaml {

    private final JavaPlugin plugin;
    private final String name;
    private YamlConfiguration yamlConfiguration;

    public Yaml(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.reload();
    }

    public YamlConfiguration getConfig() {
        return yamlConfiguration;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return name + ".yml";
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), getRealName());

        if (!file.exists()) {
            boolean success = file.getParentFile().mkdirs();
            plugin.saveResource(getRealName(), false);
        }

        this.yamlConfiguration = new YamlConfiguration();

        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        File file = new File(plugin.getDataFolder(), getRealName());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
