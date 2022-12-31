package me.kiminouso.chatgames;

import lombok.Getter;
import me.kiminouso.chatgames.commands.BaseCommand;
import me.kiminouso.chatgames.listeners.ChatListener;
import me.kiminouso.chatgames.listeners.JoinListener;
import me.kiminouso.chatgames.utils.ChatGameTask;
import me.kiminouso.chatgames.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;

public final class ChatGames extends JavaPlugin {
    @Getter
    private final ChatGame chatGame = new ChatGame();
    @Getter
    private final ChatGameTask chatGameTask = new ChatGameTask();
    private final ChatListener chatListener = new ChatListener();
    @Getter
    private final Storage storage = new Storage(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Metrics metrics = new Metrics(this, 17210);

        Bukkit.getPluginCommand("chatgames").setExecutor(new BaseCommand());

        chatGame.load();
        Bukkit.getPluginManager().registerEvents(chatListener, this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new Placeholders(this).register();
    }

    @Override
    public void onDisable() {
        if (chatGameTask.isActive())
            chatGameTask.end();
    }
}
