package web.database.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;

    public Customer(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.firstName = rs.getString("first_name");
        this.lastName = rs.getString("last_name");
    }
}
