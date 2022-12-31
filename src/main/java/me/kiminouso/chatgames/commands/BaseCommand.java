package me.kiminouso.chatgames.commands;

import me.tippie.tippieutils.commands.TippieCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

public class BaseCommand extends TippieCommand {
    public BaseCommand() {
        super.name = "chatgames";
        super.getSubCommands().add(new WinsCommand());
        super.getSubCommands().add(new TopCommand());
        super.getSubCommands().add(new ReloadCommand());
        super.getSubCommands().add(new SetDelayCommand());
        super.getSubCommands().add(new ToggleCommand());
    }

    @Override
    protected void sendHelpMessage(CommandSender sender, String label, String prefix) {
        sender.sendMessage("\n§8[§eChatGames§8]§e Announcement Commands");

        getSubCommands().forEach(cmd -> {
            if (!sender.hasPermission(cmd.getPermission())) return;

            TextComponent helpMessage =
                    new TextComponent("§7 - §e/" + label + " " + cmd.getName() + ":§f " + cmd.getDescription());
            helpMessage.setHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7Click to open command.")));
            helpMessage.setClickEvent(
                    new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " " + cmd.getName() + " "));
            sender.spigot().sendMessage(helpMessage);
        });
    }
}
