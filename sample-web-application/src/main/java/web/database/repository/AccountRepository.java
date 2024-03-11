package web.database.repository;

import web.database.DatabaseConnection;
import web.database.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Long> maxId = accountList.stream().map(Account::getId).max(Long::compare);
        Long newId = maxId.map(id -> id + 1).orElse(1L);
        String SQL = "INSERT INTO account (id, balance, creation_date) VALUES(?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.setLong(1, newId);
            pstmt.setInt(2, balance);
            pstmt.setTimestamp(3, Timestamp.valueOf(creationDate));
            pstmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAccount(Long id) {
        String SQL = "DELETE FROM Account WHERE id=" + id;

        try (PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
