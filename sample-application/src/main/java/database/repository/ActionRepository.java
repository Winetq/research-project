package database.repository;

import database.DatabaseConnection;
import database.model.Account;
import database.model.Action;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActionRepository {

    private DatabaseConnection connection = new DatabaseConnection();

    public List<Action> getAllActions() {
        String SQL = "SELECT * FROM action";
        List<Action> actionList = new ArrayList<>();

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery();) {

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
        Action action;

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery();) {

                action = new Action(rs);
                return action;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<Action> getActionsByTitle(String title) {
        String SQL = "SELECT * FROM Action WHERE title=" + title;
        List<Action> actionList = new ArrayList<>();

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery();) {

            while (rs.next()) {
                actionList.add(new Action(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return actionList;
    }

    public List<Action> getActionsByType(String type) {
        String SQL = "SELECT * FROM Action WHERE type=" + type;
        List<Action> actionList = new ArrayList<>();

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery();) {

            while (rs.next()) {
                actionList.add(new Action(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return actionList;
    }

    public void addAction(String title, int amount, String type, Long accountId, String status, String date) {
        String SQL = "INSERT INTO Action (title, amount, type, account_id, status, date) " +
                "VALUES('" + title + "', " + amount + ", '" + type + "', " + accountId + ", '" + status+ "', '" + Timestamp.valueOf(date) + "')";

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);){
            pstmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
