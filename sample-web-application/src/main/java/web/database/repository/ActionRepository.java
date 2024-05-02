package web.database.repository;

import web.database.DatabaseConnection;
import web.database.model.Action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ActionRepository {

    public List<Action> getAllActions() {
        String SQL = "SELECT * FROM action";
        List<Action> actionList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                actionList.add(new Action(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return actionList;
    }

    public Action getActionById(Long id) {
        String SQL = "SELECT * FROM Action WHERE id=" + id;
        Action action = null;

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            action = new Action(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return action;
    }

    public List<Action> getActionsByTitle(String title) {
        String SQL = "SELECT * FROM Action WHERE title=" + title;
        List<Action> actionList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                actionList.add(new Action(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return actionList;
    }

    public List<Action> getActionsByType(String type) {
        String SQL = "SELECT * FROM Action WHERE type=" + type;
        List<Action> actionList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                actionList.add(new Action(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return actionList;
    }

    public void addAction(String title, int amount, String type, Long accountId, String status, String date) throws SQLException {
        String SQL = "INSERT INTO Action (title, amount, type, account_id, status, date) " +
                "VALUES('" + title + "', " + amount + ", '" + type + "', " + accountId
                + ", '" + status+ "', '" + Timestamp.valueOf(date) + "')";
        String updateAccountSQL = "UPDATE Account SET balance=balance+? WHERE id=?";

        Connection connection = DatabaseConnection.connect();

        connection.setAutoCommit(false);

        boolean isCurrencyTransfer = type.equals("Przelew walutowy");

        if (isCurrencyTransfer) {
            int currencyTransferCommission = 2;
            String commissionTitle = "Prowizja za przelew walutowy: " + title;
            String commissionSQL = "INSERT INTO Action (title, amount, type, account_id, status, date) " +
                    "VALUES('" + commissionTitle + "', " + currencyTransferCommission + ", '" + type + "', "
                    + accountId + ", '" + status+ "', '" + Timestamp.valueOf(date) + "')";

            try (PreparedStatement pstmtCommission = connection.prepareStatement(commissionSQL)) {
                pstmtCommission.execute();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        try (PreparedStatement pstmt = connection.prepareStatement(SQL);
             PreparedStatement pstmtAccount = connection.prepareStatement(updateAccountSQL)) {
            pstmt.execute();
            // Thread.sleep(30000);
            pstmtAccount.setInt(1, amount);
            pstmtAccount.setLong(2, accountId);
            pstmtAccount.execute();
            if (Math.random() < 0.75) {
                connection.commit();
            } else {
                connection.rollback();
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connection.close();
        }
    }

}
