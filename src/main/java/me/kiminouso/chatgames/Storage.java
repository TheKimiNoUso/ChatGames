package me.kiminouso.chatgames;

import me.tippie.tippieutils.storage.SQLStorage;
import me.tippie.tippieutils.storage.annotations.SqlQuery;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class Storage extends SQLStorage {
    private final ChatGames plugin;

    public Storage(ChatGames plugin){
        super(plugin, org.h2.Driver.load(), SQLType.H2, new File(plugin.getDataFolder(), "statistics"));

        this.plugin = plugin;

        try {
            runResourceScript("db.sql");
        } catch (SQLException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not connect to H2 Database for ChatGames statistics...", e);
        }
    }

    @SqlQuery("SELECT * FROM STATS ORDER BY WINS DESC LIMIT 3")
    public CompletableFuture<List<Gamer>> getTopGamers() {
        return prepareStatement((stmt) -> {
            try {
                ResultSet rs = stmt.executeQuery();
                List<Gamer> result = new ArrayList<>();
                while (rs.next()) {
                    Gamer entry = new Gamer(
                            rs.getObject("USER", UUID.class),
                            rs.getInt("WINS")
                    );
                    result.add(entry);
                }
                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SqlQuery("SELECT * FROM STATS WHERE USER = ?")
    public CompletableFuture<Integer> getUser(UUID uuid) {
        return prepareStatement((stmt) -> {
            try {
                stmt.setObject(1, uuid);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("WINS");
                } else {
                    return 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @SqlQuery("UPDATE STATS SET WINS = WINS+1 WHERE USER = ?")
    public CompletableFuture<Void> addWin(UUID uuid) {
        return prepareStatement((stmt) -> {
            try {
                if (!findUser(uuid).join()) {
                    registerNewUser(uuid).join();
                } else {
                    stmt.setObject(1, uuid);
                    stmt.execute();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @SqlQuery("INSERT INTO STATS (USER, WINS) VALUES (?,1)")
    public CompletableFuture<Void> registerNewUser(UUID uuid) {
        return prepareStatement((stmt) -> {
            try {
                stmt.setObject(1, uuid);
                stmt.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @SqlQuery("SELECT * FROM STATS WHERE USER = ?")
    public CompletableFuture<Boolean> findUser(UUID uuid) {
        return prepareStatement((stmt) -> {
            try {
                stmt.setObject(1, uuid);

                ResultSet rs = stmt.executeQuery();
                return rs.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public record Gamer(UUID uuid, int wins) {

    }
}
