package me.kiminouso.chatgames;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class ChatGame {
    private final List<Game> games = new ArrayList<>();
    public List<Game> currentGame = new ArrayList<>();
    public Instant lastSent;
    public HashMap<UUID, Integer> wins = new HashMap<>();

    public void load() {
        if (ChatGames.getPlugin(ChatGames.class).getChatGameTask().isActive())
            ChatGames.getPlugin(ChatGames.class).getChatGameTask().end();

        ChatGames.getPlugin(ChatGames.class).getChatGameTask().setCooldown(
                ChatGames.getPlugin(ChatGames.class).getConfig().getInt("settings.timer")
        );

        ChatGames.getPlugin(ChatGames.class).reloadConfig();
        games.clear();

        if (ChatGames.getPlugin(ChatGames.class).getConfig().getBoolean("games.scramble.enable")) {
            ChatGames.getPlugin(ChatGames.class).getConfig().getStringList("games.scramble.words").forEach(word -> {
                String scrambled = scrambleWord(word);
                games.add(new Game("SCRAMBLE", scrambled, word,
                        ChatGames.getPlugin(ChatGames.class).getConfig().getString("games.scramble.reward", "?")));
            });
        }

        if (ChatGames.getPlugin(ChatGames.class).getConfig().getBoolean("games.reverse.enable")) {
            ChatGames.getPlugin(ChatGames.class).getConfig().getStringList("games.reverse.words").forEach(word -> {
                String reversed = reverseWord(word);
                games.add(new Game("REVERSE", reversed, word,
                        ChatGames.getPlugin(ChatGames.class).getConfig().getString("games.reverse.reward", "?")));
            });
        }

        if (ChatGames.getPlugin(ChatGames.class).getConfig().getBoolean("games.math.enable")) {
            Map<String, String> math = new HashMap<>();
            int subtraction = ChatGames.getPlugin(ChatGames.class).getConfig().getInt("games.math.limits.subtraction");
            int addition = ChatGames.getPlugin(ChatGames.class).getConfig().getInt("games.math.limits.addition");
            int division = ChatGames.getPlugin(ChatGames.class).getConfig().getInt("games.math.limits.division");
            int multiplication = ChatGames.getPlugin(ChatGames.class).getConfig().getInt("games.math.limits.multiplication");

            if (subtraction >= 1) {
                for (int i = 0; i < subtraction; i++) {
                    int a = getRandomInt(100);
                    int b = getRandomInt(100);
                    String question = a + " - " + b;
                    int answer = a - b;
                    math.put(question, String.valueOf(answer));
                }
            }

            if (division >= 1) {
                for (int i = 0; i < division; i++) {
                    int a = getRandomInt(100);
                    int b = getRandomInt(50);
                    String question = a + " / " + b;
                    int answer = a / b;
                    math.put(question, String.valueOf(answer));
                }
            }

            if (multiplication >= 1) {
                for (int i = 0; i < multiplication; i++) {
                    int a = getRandomInt(100);
                    int b = getRandomInt(13);
                    String question = a + " * " + b;
                    int answer = a * b;
                    math.put(question, String.valueOf(answer));
                }
            }

            if (addition >= 1) {
                for (int i = 0; i < addition; i++) {
                    int a = getRandomInt(1000);
                    int b = getRandomInt(1000);
                    String question = a + " + " + b;
                    int answer = a + b;
                    math.put(question, String.valueOf(answer));
                }
            }

            math.forEach((key, value) -> games.add(new Game("MATH", key, value,
                    ChatGames.getPlugin(ChatGames.class).getConfig().getString("games.math.reward", "?"))));
        }

        Collections.shuffle(games);

        if (ChatGames.getPlugin(ChatGames.class).getConfig().getBoolean("settings.auto-start", true)) {
            if (!ChatGames.getPlugin(ChatGames.class).getChatGameTask().isActive())
                ChatGames.getPlugin(ChatGames.class).getChatGameTask().start();
        }
    }

    private String scrambleWord(String word) {
        StringBuilder builder = new StringBuilder();
        List<Character> characters = new ArrayList<>();

        for (char c : word.toCharArray())
            characters.add(c);

        Collections.shuffle(characters);
        characters.forEach(builder::append);

        if (builder.toString().equals(word))
            scrambleWord(word);

        return builder.toString();
    }

    private String reverseWord(String word) {
        StringBuilder builder = new StringBuilder();
        List<Character> characters = new ArrayList<>();

        for (char c : word.toCharArray())
            characters.add(c);

        Collections.reverse(characters);
        characters.forEach(builder::append);

        return builder.toString();
    }

    private int getRandomInt(int max) {
        return ThreadLocalRandom.current().nextInt(1, max + 1);
    }

    public record Game(String type, String question, String answer, String command) {
    }
}
