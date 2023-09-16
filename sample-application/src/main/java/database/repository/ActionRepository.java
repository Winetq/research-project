package database.repository;

import database.DatabaseConnection;
import database.model.Action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ActionRepository {

    private final Connection connection = DatabaseConnection.connect();

    public List<Action> getAllActions() {
        String SQL = "SELECT * FROM action";
        List<Action> actionList = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(SQL);
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

        try (PreparedStatement pstmt = connection.prepareStatement(SQL);
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

        try (PreparedStatement pstmt = connection.prepareStatement(SQL);
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

        try (PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                actionList.add(new Action(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return actionList;
    }

    public void addAction(String title, int amount, String type, Long accountId, String status, String date) {
        String SQL = "INSERT INTO Action (title, amount, type, account_id, status, date) " +
                "VALUES('" + title + "', " + amount + ", '" + type + "', " + accountId + ", '" + status+ "', '" + Timestamp.valueOf(date) + "')";

        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
