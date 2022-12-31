package me.kiminouso.chatgames.listeners;

import me.kiminouso.chatgames.ChatGame;
import me.kiminouso.chatgames.ChatGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerChat(AsyncPlayerChatEvent event) {
        if (ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.isEmpty())
            return;

        ChatGame.Game currentGame = ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.get(0);

        if (!event.getMessage().contains(currentGame.answer()))
            return;

        String message = ChatGames.getPlugin(ChatGames.class).getConfig().getString("messages.win").replace("{0}", currentGame.answer());
        message = message.replace("{player}", event.getPlayer().getName());

        if (message.contains("{time}")) {
            Duration duration = Duration.between(ChatGames.getPlugin(ChatGames.class).getChatGame().lastSent, Instant.now());
            message = message.replace("{time}", String.valueOf(duration.getSeconds()));
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("chatgames.bypass"))
                return;

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

        if (ChatGames.getPlugin(ChatGames.class).getConfig().getBoolean("settings.enable-rewards")) {
            String command = currentGame.command();
            String finalCommand = command.replace("{player}", event.getPlayer().getName());

            Bukkit.getScheduler().runTask(ChatGames.getPlugin(ChatGames.class), () -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), finalCommand));
        }

        ChatGames.getPlugin(ChatGames.class).getStorage().addWin(event.getPlayer().getUniqueId());

        ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.clear();

        ChatGames.getPlugin(ChatGames.class).getStorage().getUser(event.getPlayer().getUniqueId()).thenAccept(integer -> {
            ChatGames.getPlugin(ChatGames.class).getChatGame().wins.put(event.getPlayer().getUniqueId(), integer);
        });
    }
}
