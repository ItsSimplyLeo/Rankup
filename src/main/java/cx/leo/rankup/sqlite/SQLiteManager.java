package cx.leo.rankup.sqlite;

import cx.leo.rankup.RankupPlugin;
import cx.leo.rankup.rank.Rank;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class SQLiteManager {

    private final static String CREATE_PLAYER_RANK_TABLE =
            "CREATE TABLE IF NOT EXISTS player_ranks (" +
                    "`uuid` varchar(32) NOT NULL," +
                    "`rank` varchar(32)," +
                    "PRIMARY KEY (`uuid`)" +
                    ");";

    private final RankupPlugin plugin;

    private Connection connection;

    public SQLiteManager(RankupPlugin plugin) {
        this.plugin = plugin;
        this.load();
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_PLAYER_RANK_TABLE);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), "players.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error whilst creating players.db");
            }
        }
        try {

            if(connection != null && !connection.isClosed()) return connection;

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite Exception, please report.", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite JBDC library was not found. Please install it.");
        }
        return null;
    }

    public String getRank(Player player) {
        return getRank(player.getUniqueId());
    }

    public String getRank(UUID uuid) {
        Connection connection = getSQLConnection();
        PreparedStatement statement = null;
        ResultSet result;

        try {
            statement = connection.prepareStatement("SELECT * FROM player_ranks WHERE uuid = '" + uuid.toString() + "';");
            result = statement.executeQuery();

            while (result.next()) {
                if (result.getString("uuid").equals(uuid.toString())) {
                    return result.getString("rank");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error whilst closing statement & connection...");
            }
        }

        return null;
    }

    public void setRank(UUID uuid, Rank rank) {
        Connection connection = getSQLConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("REPLACE INTO player_ranks (uuid,rank) VALUES(?,?);");
            statement.setString(1, uuid.toString());
            statement.setString(2, rank.id());
            statement.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Error whilst getting player...");
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error whilst closing statement & connection...");
            }
        }
    }

    public void insertRank(UUID uuid, Rank rank) {
        Connection connection = getSQLConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("INSERT INTO player_ranks (uuid,rank) VALUES(?,?);");
            statement.setString(1, uuid.toString());
            statement.setString(2, rank.id());
            statement.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().severe("Error whilst inserting player...");
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error whilst closing statement & connection...");
            }
        }
    }
}
