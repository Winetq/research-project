package database.repository;

import database.DatabaseConnection;
import database.model.Action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
}
