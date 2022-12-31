package me.kiminouso.chatgames;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Placeholders extends PlaceholderExpansion {

    private final ChatGames plugin;

    public Placeholders(ChatGames plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "chatgames";
    }

    @Override
    public @NotNull String getAuthor() {
        return "KimiNoUso";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return this.onRequest(player, params);
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.equalsIgnoreCase("wins")) {
            if (ChatGames.getPlugin(ChatGames.class).getChatGame().wins.containsKey(player.getUniqueId())) {
                return String.valueOf(ChatGames.getPlugin(ChatGames.class).getChatGame().wins.get(player.getUniqueId()));
            } else {
                return "Unknown";
            }
        } else if (params.equalsIgnoreCase("question")) {
            if (!ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.isEmpty()) {
                return ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.get(0).question();
            } else {
                return "No game running...";
            }
        } else if (params.equalsIgnoreCase("answer")) {
            if (!ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.isEmpty()) {
                return ChatGames.getPlugin(ChatGames.class).getChatGame().currentGame.get(0).answer();
            } else {
                return "No game running...";
            }
        } else {
            return null;
        }
    }
}
