package database.repository;

import database.DatabaseConnection;
import database.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    private DatabaseConnection connection = new DatabaseConnection();

    public List<Customer> getAllCustomers() {
        String SQL = "SELECT * FROM customer";
        List<Customer> customerList = new ArrayList<>();

        try (Connection conn = connection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customerList.add(new Customer(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customerList;
    }
}
