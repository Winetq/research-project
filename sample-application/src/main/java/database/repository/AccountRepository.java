package database.repository;

import database.DatabaseConnection;
import database.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    private final Connection connection = DatabaseConnection.connect();

    public List<Account> getAllAccounts() {
        String SQL = "SELECT * FROM account";
        List<Account> accountList = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

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
        Account account = null;

        try (PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            account = new Account(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return account;
    }

    public void addAccount(int balance, String creationDate) {
        List<Account> accountList = getAllAccounts();
        long newId = accountList.get(accountList.size() - 1).getId() + 1;
        String SQL = "INSERT INTO account (id, balance, creation_date) " +
            "VALUES("+ newId + ", " + balance + ", '" + Timestamp.valueOf(creationDate) + "')";

        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
