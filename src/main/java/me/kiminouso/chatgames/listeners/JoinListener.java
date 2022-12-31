package me.kiminouso.chatgames.listeners;

import me.kiminouso.chatgames.ChatGames;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        ChatGames.getPlugin(ChatGames.class).getStorage().getUser(event.getPlayer().getUniqueId()).thenAccept(integer -> {
            ChatGames.getPlugin(ChatGames.class).getChatGame().wins.put(event.getPlayer().getUniqueId(), integer);
        });
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        ChatGames.getPlugin(ChatGames.class).getChatGame().wins.remove(event.getPlayer().getUniqueId());
    }
}
