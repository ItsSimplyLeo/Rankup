package cx.leo.rankup.commands;

import cx.leo.rankup.RankupManager;
import cx.leo.rankup.RankupPlugin;
import cx.leo.rankup.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand implements CommandExecutor {

    private final RankupPlugin plugin;

    public RankupCommand(RankupPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You need to be a player to execute this command.");
            return false;
        }

        RankupManager rankupManager = plugin.getRankupManager();
        Rank currentRank = rankupManager.getPlayerRank(player);

        if (rankupManager.hasNextRank(currentRank)) {
            Rank nextRank = rankupManager.getNextRank(currentRank);
            if (plugin.getEcon().has(player, nextRank.getPrice())) {
                player.sendMessage(ChatColor.GREEN + "You have ranked up to " + nextRank.getId() + " for $" + nextRank.getPrice());
                rankupManager.setRank(player, nextRank);
            } else player.sendMessage(ChatColor.RED + "You do not have enough money for this rank.");
        } else {
            player.sendMessage(ChatColor.RED + "You have reached the last available rank.");
        }

        return true;
    }
}
