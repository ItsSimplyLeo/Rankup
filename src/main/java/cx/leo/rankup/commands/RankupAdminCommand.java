package cx.leo.rankup.commands;

import cx.leo.rankup.RankupPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RankupAdminCommand implements CommandExecutor {

    private final RankupPlugin plugin;

    public RankupAdminCommand(RankupPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reload();
        sender.sendMessage(ChatColor.RED + "Rankup has been reloaded.");
        return true;
    }
}
