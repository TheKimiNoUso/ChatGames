package me.kiminouso.chatgames.commands;

import me.kiminouso.chatgames.ChatGames;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class SetDelayCommand extends TippieCommand {
    public SetDelayCommand() {
        super.subLevel = 1;
        super.name = "setdelay";
        super.description = "Change how frequently questions are sent";
        super.permission = "chatgames.set-delay";
    }

    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        int cooldown;

        try {
            cooldown = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInput was not an integer!");
            Bukkit.getLogger().log(Level.WARNING, sender.getName() + " has attempted to change the cooldown but didn't format their integer properly.");
            e.printStackTrace();
            return;
        }

        sender.sendMessage("Reloading ChatGames config.yml & Updating your cooldown...");
        ChatGames.getPlugin(ChatGames.class).getChatGameTask().setCooldown(cooldown);
        Bukkit.getScheduler().runTaskLater(ChatGames.getPlugin(ChatGames.class), () -> ChatGames.getPlugin(ChatGames.class).getChatGame().load(), 10L);
    }
}
