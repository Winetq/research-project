package database.repository;

import database.DatabaseConnection;
import database.model.CustomerAccountRef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
}
