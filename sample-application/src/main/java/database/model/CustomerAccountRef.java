package database.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerAccountRef {

    private Long id;
    private Long customerId;
    private Long accountId;

    public CustomerAccountRef(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.customerId = rs.getLong("customer_id");
        this.accountId = rs.getLong("account_id");
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "CustomerAccountRef{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", accountId=" + accountId +
                '}';
    }
}
