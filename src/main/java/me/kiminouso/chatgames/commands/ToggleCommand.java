package me.kiminouso.chatgames.commands;

import me.kiminouso.chatgames.ChatGames;
import me.tippie.tippieutils.commands.TippieCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends TippieCommand {
    public ToggleCommand() {
        super.subLevel = 1;
        super.name = "toggle";
        super.description = "Toggles chat games";
        super.permission = "chatgames.set-delay";
    }

    @Override
    public void executes(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NoSuchMethodException {
        ChatGames.getPlugin(ChatGames.class).toggleChatGames(sender);
    }
}
