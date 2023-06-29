package cx.leo.rankup;

import cx.leo.rankup.commands.RanksCommand;
import cx.leo.rankup.commands.RankupAdminCommand;
import cx.leo.rankup.commands.RankupCommand;
import cx.leo.rankup.listeners.PlayerJoinListener;
import cx.leo.rankup.sqlite.SQLiteManager;
import cx.leo.rankup.yaml.Yaml;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class RankupPlugin extends JavaPlugin {

    private Economy econ;
    private Yaml config;
    private RankupManager rankupManager;

    private SQLiteManager sqLiteManager;

    @Override
    public void onEnable() {
        this.checkVault();

        this.config = new Yaml(this, "config");
        this.rankupManager = new RankupManager(this);
        this.sqLiteManager = new SQLiteManager(this);

        this.getCommand("ranks").setExecutor(new RanksCommand(this));
        this.getCommand("rankup").setExecutor(new RankupCommand(this));
        this.getCommand("rankupadmin").setExecutor(new RankupAdminCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    public Economy getEcon() {
        return econ;
    }

    public RankupManager getRankupManager() {
        return rankupManager;
    }

    public SQLiteManager getSqLiteManager() {
        return sqLiteManager;
    }

    public void checkVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("Vault not found, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().warning("Vault Economy plugin not found, disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        econ = rsp.getProvider();
    }

    public void reload() {
        this.reloadConfig();
        this.rankupManager.reload();
    }

    @Override
    @NonNull
    public FileConfiguration getConfig() {
        return config.getConfig();
    }

    @Override
    public void reloadConfig() {
        config.reload();
    }

    @Override
    public void saveConfig() {
        config.save();
    }
}
