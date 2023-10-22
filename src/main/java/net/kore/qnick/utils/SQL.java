package net.kore.qnick.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SQL {
    private Connection connection = null;
    private final String DBURL;
    private final String USERNAME;
    private final String PASSWORD;
    private final String TABLE = "qnick";
    private final Map<String, String> nicknames = new HashMap<>();

    public SQL(String dburl, String username, String password) {
        refreshConnection(dburl, username, password);
        DBURL = dburl;
        USERNAME = username;
        PASSWORD = password;
    }

    public String getNick(UUID uuid) {
        return getNick(uuid, true);
    }

    public String getNick(UUID uuid, boolean cache) {
        checkConnection();
        String struuid = uuid.toString();
        String nick;
        if (cache && nicknames.get(struuid) != null) {
            return nicknames.get(struuid);
        }
        if (cache && Objects.equals(nicknames.get(struuid), "")) {
            return null;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String sql = "SELECT nick FROM "+TABLE+" WHERE uuid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, struuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nick = resultSet.getString("nick");
                nicknames.put(struuid, nick);
            } else {
                nick = null;
                nicknames.put(struuid, "");
            }

            resultSet.close();
            preparedStatement.close();
            return nick;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setNick(UUID uuid, String newNick) {
        checkConnection();
        String struuid = uuid.toString();
        nicknames.remove(struuid);
        nicknames.put(struuid, newNick);
        try {
            String checkSql = "SELECT uuid FROM "+TABLE+" WHERE uuid = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setString(1, struuid);

            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                String sql = "UPDATE "+TABLE+" SET nick = ? WHERE uuid = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newNick);
                preparedStatement.setString(2, struuid);

                int rowsAffected = preparedStatement.executeUpdate();

                resultSet.close();
                checkStatement.close();

                return rowsAffected > 0;
            } else {
                // UUID doesn't exist, so insert it
                String insertSql = "INSERT INTO "+TABLE+" (uuid, nick) VALUES (?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setString(1, struuid);
                insertStatement.setString(2, newNick);

                int rowsAffected = insertStatement.executeUpdate();

                resultSet.close();
                checkStatement.close();

                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeNick(UUID uuid) {
        checkConnection();
        String struuid = uuid.toString();
        nicknames.remove(struuid);
        nicknames.put(struuid, "");
        try {
            String deleteSql = "DELETE FROM "+TABLE+" WHERE uuid = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
            deleteStatement.setString(1, struuid);

            int rowsAffected = deleteStatement.executeUpdate();

            deleteStatement.close();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasNick(UUID uuid) {
        return hasNick(uuid, true);
    }

    public boolean hasNick(UUID uuid, boolean cache) {
        checkConnection();
        String struuid = uuid.toString();
        if (cache && nicknames.get(struuid) != null) {
            return true;
        }
        if (cache && Objects.equals(nicknames.get(struuid), "")) {
            return false;
        }
        try {
            String sql = "SELECT uuid FROM "+TABLE+" WHERE uuid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, struuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                return true;
            } else {
                resultSet.close();
                preparedStatement.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void refreshConnection(String dburl, String username, String password) {
        try {
            connection = DriverManager.getConnection(dburl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkConnection() {
        try {
            if (connection.isClosed() || connection == null) {
                refreshConnection(DBURL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
