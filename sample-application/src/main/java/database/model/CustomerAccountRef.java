package database.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@ToString
public class CustomerAccountRef {
    private Long id;
    private Long customerId;
    private Long accountId;

    public CustomerAccountRef(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.customerId = rs.getLong("customer_id");
        this.accountId = rs.getLong("account_id");
    }
}
