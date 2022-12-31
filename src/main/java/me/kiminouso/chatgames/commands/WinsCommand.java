package me.kiminouso.chatgames.commands;

import me.kiminouso.chatgames.ChatGame;
import me.kiminouso.chatgames.ChatGames;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WinsCommand extends TippieCommand {
    public WinsCommand() {
        super.subLevel = 1;
        super.name = "wins";
        super.description = "See how many Chat Game wins you have";
        super.permission = "chatgames.wins";
    }

    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This is a player only command.");
            return;
        }

        ChatGames.getPlugin(ChatGames.class).getStorage().getUser(player.getUniqueId()).thenAccept(integer -> {
            String message = ChatGames.getPlugin(ChatGames.class).getConfig().getString("messages.wins");
            String finalMessage = message.replace("{0}", String.valueOf(integer));

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', finalMessage));
        });
    }
}
