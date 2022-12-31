package me.kiminouso.chatgames.utils;

import lombok.Getter;
import lombok.Setter;
import me.kiminouso.chatgames.ChatGame;
import me.kiminouso.chatgames.ChatGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;

@Getter
@Setter
public class ChatGameTask {
    private int cooldown;

    private final Runnable task = () -> {
        ChatGame.Game currentGame = ChatGames.getPlugin(ChatGames.class).getChatGame().getGames().get(0);
        ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.clear();
        ChatGames.getPlugin(ChatGames.class).getChatGame().getGames().remove(currentGame);
        ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.add(currentGame);
        switch (currentGame.type()) {
            case "MATH" -> broadcastMessage("math", currentGame.question());
            case "SCRAMBLE" -> broadcastMessage("unscramble", currentGame.question());
            case "REVERSE" -> broadcastMessage("reverse", currentGame.question());
        }
        ChatGames.getPlugin(ChatGames.class).getChatGame().setLastSent(Instant.now());
    };

    private void broadcastMessage(String configValue, String question) {
        if (ChatGames.getPlugin(ChatGames.class).getConfig().getBoolean("settings.player-requirement")) {
            if (Bukkit.getOnlinePlayers().size() < ChatGames.getPlugin(ChatGames.class).getConfig().getInt("settings.threshold"))
                return;
        }

        String msg = ChatGames.getPlugin(ChatGames.class).getConfig().getString("messages." + configValue).replace("{0}", question);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!player.hasPermission("chatgames.see"))
                return;

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        });
    }

    private BukkitTask activeTask = null;

    public void start() {
        if (activeTask != null) activeTask.cancel();

        activeTask = Bukkit.getScheduler()
                .runTaskTimer(ChatGames.getPlugin(ChatGames.class), task, 1L, cooldown * 1200L);
    }

    public void end() {
        if (activeTask != null) {
            activeTask.cancel();
            activeTask = null;
        }
    }

    public boolean isActive() {
        return activeTask != null;
    }
}
