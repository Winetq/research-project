package database.repository;

import database.DatabaseConnection;
import database.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    private DatabaseConnection connection = new DatabaseConnection();

    public List<Account> getAllAccounts() {
        String SQL = "SELECT * FROM account";
        List<Account> accountList = new ArrayList<>();

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery();) {

            while (rs.next()) {
                accountList.add(new Account(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accountList;
    }
}
