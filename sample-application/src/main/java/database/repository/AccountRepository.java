package database.repository;

import database.DatabaseConnection;
import database.model.Account;

import java.sql.*;
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


    public Account getAccountById(Long id) {
        String SQL = "SELECT * FROM account WHERE id=" + id;
        Account account;

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery();) {

            account = new Account(rs);
            return account;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void addAccount(int balance, String creationDate) {
        String SQL = "INSERT INTO account (id, balance, creation_date) " +
            "VALUES("+ System.currentTimeMillis() + ", " + balance + ", '" + Timestamp.valueOf(creationDate) + "')";


        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);){
            pstmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
