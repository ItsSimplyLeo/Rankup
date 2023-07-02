package cx.leo.rankup;

import cx.leo.rankup.rank.Rank;
import cx.leo.rankup.utils.ComponentUtils;
import cx.leo.rankup.utils.FormatUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

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

    public boolean isCompleted(Rank current, Rank check) {
        return ranks.indexOf(current) >= ranks.indexOf(check);
    }

    public void setRank(Player player, Rank rank) {
        setRank(player.getUniqueId(), rank);
    }

    public void setRank(UUID uuid, Rank rank) {
        playerRanks.put(uuid, rank);
        saveRank(uuid, rank);
    }

    private void saveRank(UUID uuid, Rank rank) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getSqLiteManager().setRank(uuid, rank));
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

    public void openRankGui(Player player) {
        final FileConfiguration menus = plugin.getConfigManager().getMenus().getConfig();

        var gui = Gui.gui().title(ComponentUtils.parse(menus.getString("ranks.title", "Ranks"))).rows(5).disableAllInteractions().create();

        var item = ItemBuilder.from(Material.GRAY_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem();
        gui.getFiller().fillBorder(item);

        for (Rank rank : ranks) {
            gui.addItem(createRankItem(rank, getPlayerRank(player)));
        }

        gui.open(player);
    }

    private GuiItem createRankItem(Rank target, Rank current) {
        final FileConfiguration menus = plugin.getConfigManager().getMenus().getConfig();
        String sec = "ranks.rank-item.";

        boolean unlocked = isCompleted(target, current);

        if (unlocked) sec = sec + "unlocked.";
        else sec = sec + "locked.";

        double bulkCost = 0d;

        for (Rank rank : ranks) {
            if (rank == target) {
                bulkCost += rank.getPrice();
                break;
            }

            if (!isCompleted(current, rank)) bulkCost += rank.getPrice();
        }

        var rankPlaceholder = Placeholder.parsed("rank", target.getId());
        var pricePlaceholder = Placeholder.parsed("cost", FormatUtils.currency(target.getPrice()));
        var priceBulkPlaceholder = Placeholder.parsed("bulk_cost", FormatUtils.currency(bulkCost));
        
        var placeholders = Arrays.asList(rankPlaceholder, pricePlaceholder, priceBulkPlaceholder);

        ItemBuilder builder = ItemBuilder
                .from(Material.valueOf(menus.getString(sec + "material", "BARRIER")))
                .name(ComponentUtils.parse(menus.getString(sec + "name"), TagResolver.resolver(placeholders)))
                .lore(ComponentUtils.parseToList(menus.getStringList(sec + "lore"), TagResolver.resolver(placeholders)))
                ;

        return builder.asGuiItem();
    }

    public void refreshPlayerRanks() {
        playerRanks.clear();
        Bukkit.getOnlinePlayers().forEach(this::loadRank);
    }

    public Rank getPlayerRank(Player player) {
        return playerRanks.get(player.getUniqueId());
    }

    public Rank getPlayerRank(UUID uuid) {
        return playerRanks.get(uuid);
    }
}
