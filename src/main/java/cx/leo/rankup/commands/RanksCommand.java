package cx.leo.rankup.commands;

import cx.leo.rankup.RankupManager;
import cx.leo.rankup.RankupPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RanksCommand implements CommandExecutor {

    private final RankupPlugin plugin;

    public RanksCommand(RankupPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        RankupManager rankupManager = plugin.getRankupManager();
        rankupManager.openRankGui((Player) sender);

        return false;
    }
}
