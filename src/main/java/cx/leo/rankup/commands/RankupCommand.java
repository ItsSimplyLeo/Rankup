package cx.leo.rankup.commands;

import cx.leo.rankup.RankupPlugin;
import cx.leo.rankup.rank.Rank;
import cx.leo.rankup.rank.RankManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankupCommand implements CommandExecutor {

    private final RankupPlugin plugin;

    public RankupCommand(RankupPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You need to be a player to execute this command."));
            return false;
        }

        RankManager rankManager = plugin.getRankupManager();
        Rank currentRank = rankManager.getPlayerRank(player);

        if (rankManager.hasNextRank(currentRank)) {
            Rank nextRank = rankManager.getNextRank(currentRank);
            if (plugin.getEcon().has(player, nextRank.price())) {
                player.sendMessage(Component.text("You have ranked up to " + nextRank.id() + " for $" + nextRank.price(), NamedTextColor.GREEN));
                rankManager.setRank(player, nextRank);
            } else player.sendMessage(Component.text("You do not have enough money for this rank.", NamedTextColor.RED));
        } else {
            player.sendMessage(Component.text("You have reached the last available rank.", NamedTextColor.RED));
        }

        return true;
    }
}
