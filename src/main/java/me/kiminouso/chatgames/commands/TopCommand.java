package me.kiminouso.chatgames.commands;

import me.kiminouso.chatgames.ChatGames;
import me.kiminouso.chatgames.Storage;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TopCommand extends TippieCommand {
    public TopCommand() {
        super.subLevel = 1;
        super.name = "topwins";
        super.description = "See the top three chat gamers";
        super.permission = "chatgames.top-wins";
    }

    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This is a player only command.");
            return;
        }

        ChatGames.getPlugin(ChatGames.class).getStorage().getTopGamers().thenAccept(gamers -> {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    ChatGames.getPlugin(ChatGames.class).getConfig().getString("messages.top.header")));
            for (Storage.Gamer gamer : gamers) {
                String message = ChatGames.getPlugin(ChatGames.class).getConfig().getString("messages.top.entry");
                message = message.replace("{0}", Bukkit.getOfflinePlayer(gamer.uuid()).getName());
                message = message.replace("{1}", String.valueOf(gamer.wins()));

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        });
    }
}
