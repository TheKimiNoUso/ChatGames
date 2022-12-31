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

public class ReloadCommand extends TippieCommand {
    public ReloadCommand() {
        super.subLevel = 1;
        super.name = "reload";
        super.description = "Reload the configuration";
        super.permission = "chatgames.reload";
    }

    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        sender.sendMessage("Reloading ChatGames config.yml...");
        ChatGames.getPlugin(ChatGames.class).getChatGame().load();
    }
}
