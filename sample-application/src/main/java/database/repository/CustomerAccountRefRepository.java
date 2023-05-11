package database.repository;

import database.DatabaseConnection;
import database.model.Action;
import database.model.CustomerAccountRef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerAccountRefRepository {

    private DatabaseConnection connection = new DatabaseConnection();

    public List<CustomerAccountRef> getAllCustomerAccountRefs() {
        String SQL = "SELECT * FROM Customer_Account_ref";
        List<CustomerAccountRef> customerAccountRefList = new ArrayList<>();

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customerAccountRefList.add(new CustomerAccountRef(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customerAccountRefList;
    }

    public List<CustomerAccountRef> getCustomerAccountRefsByCustomerId(Long customerId) {
        String SQL = "SELECT * FROM Customer_Account_ref WHERE customerId=" + customerId;
        List<CustomerAccountRef> customerAccountRefListList = new ArrayList<>();

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery();) {

            while (rs.next()) {
                customerAccountRefListList.add(new CustomerAccountRef(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return customerAccountRefListList;
    }

    public List<CustomerAccountRef> getCustomerAccountRefsByAccountId(Long accountId) {
        String SQL = "SELECT * FROM Customer_Account_ref WHERE accountId=" + accountId;
        List<CustomerAccountRef> customerAccountRefListList = new ArrayList<>();

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery();) {

            while (rs.next()) {
                customerAccountRefListList.add(new CustomerAccountRef(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return customerAccountRefListList;
    }

    public void addCustomerAccountRef(Long customerId, Long accountId) {
        String SQL = "INSERT INTO Customer_Account_ref (customer_id, account_id) " +
                "VALUES(" + customerId + ", " + accountId + ")";

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);){
            pstmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
