package cx.leo.rankup;

import cx.leo.rankup.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RankupManager {

    private final RankupPlugin plugin;
    private final List<Rank> ranks;
    private final HashMap<UUID, Rank> playerRanks;

    public RankupManager(RankupPlugin plugin) {
        this.plugin = plugin;
        this.ranks = new ArrayList<>();
        this.playerRanks = new HashMap<>();

        this.reloadRanks();
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public HashMap<UUID, Rank> getPlayerRanks() {
        return playerRanks;
    }

    public Rank getNextRank(Rank rank) {
        return getNextRank(ranks.indexOf(rank));
    }

    public Rank getNextRank(int index) {
        if (index < 0) return null;
        if (hasNextRank(index)) return ranks.get(index + 1);
        else return null;
    }

    public boolean hasNextRank(Rank rank) {
        return ranks.size() > ranks.indexOf(rank);
    }

    public boolean hasNextRank(int index) {
        return ranks.size() > index;
    }

    public void setRank(Player player, Rank rank) {
        setRank(player.getUniqueId(), rank);
    }

    public void setRank(UUID uuid, Rank rank) {
        playerRanks.put(uuid, rank);
        setRankAsync(uuid, rank);
    }

    private void setRankAsync(UUID uuid, Rank rank) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            plugin.getSqLiteManager().setRank(uuid, rank);
        });
    }

    public void loadRank(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String rank = plugin.getSqLiteManager().getRank(player);
            Rank newRank;
            if (rank == null) {
                newRank = ranks.get(0);
                plugin.getSqLiteManager().insertRank(player.getUniqueId(), newRank);
            } else newRank = ranks.stream().filter(r -> r.getId().equalsIgnoreCase(rank)).findFirst().orElse(null);

            playerRanks.put(player.getUniqueId(), newRank);
        });
    }

    public void reload() {
        this.reloadRanks();
    }

    public void reloadRanks() {
        this.ranks.clear();
        FileConfiguration configuration = plugin.getConfig();
        ConfigurationSection section = configuration.getConfigurationSection("ranks");
        if (section == null) {
            plugin.getLogger().warning("Could not reload ranks, configuration section is null. Stopping plugin.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        section.getKeys(false).forEach(
                rankId -> {
                    Rank newRank = new Rank(rankId, configuration.getDouble("ranks." + rankId + ".price"));
                    ranks.add(newRank);
                }
        );

        this.plugin.getLogger().info(ranks.size() + " ranks have been loaded.");
        this.refreshPlayerRanks();
    }

    public void refreshPlayerRanks() {
        playerRanks.clear();
        Bukkit.getOnlinePlayers().forEach(player -> {
            //todo this...
            player.sendMessage("update...");
        });
    }

    public Rank getPlayerRank(Player player) {
        return playerRanks.get(player.getUniqueId());
    }

    public Rank getPlayerRank(UUID uuid) {
        return playerRanks.get(uuid);
    }
}
