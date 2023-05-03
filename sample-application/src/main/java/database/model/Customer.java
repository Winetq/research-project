package database.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {

    private Long id;
    private String firstName;
    private String lastName;

    public Customer(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.firstName = rs.getString("first_name");
        this.lastName = rs.getString("last_name");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
