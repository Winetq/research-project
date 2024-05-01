package web.database.repository;

import web.database.DatabaseConnection;
import web.database.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public List<Customer> getAllCustomers() {
        String SQL = "SELECT * FROM customer";
        List<Customer> customerList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customerList.add(new Customer(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customerList;
    }

    public Customer getCustomerById(Long id) {
        String SQL = "SELECT * FROM Customer WHERE id=" + id;
        Customer customer = null;

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            customer = new Customer(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customer;
    }

    public List<Customer> getCustomersByFirstName(String firstName) {
        String SQL = "SELECT * FROM Customer WHERE firstName=" + firstName;
        List<Customer> customerList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customerList.add(new Customer(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customerList;
    }

    public List<Customer> getCustomersByLastName(String lastName) {
        String SQL = "SELECT * FROM Customer WHERE lastName=" + lastName;
        List<Customer> customerList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customerList.add(new Customer(rs));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customerList;
    }

    public void addCustomer(String firstName, String lastName) {
        String SQL = "INSERT INTO customer (first_name, last_name) " +
                "VALUES(" + firstName + ", " + lastName + ")";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement pstmt = connection.prepareStatement(SQL)) {
            pstmt.executeQuery();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
