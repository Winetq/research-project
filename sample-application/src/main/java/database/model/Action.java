package database.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Action {
    private Long id;
    private String title;
    private int amount;
    private String type;
    private Long accountId;
    private String status;
    private Date date;

    public Action(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.title = rs.getString("title");
        this.amount = rs.getInt("amount");
        this.type = rs.getString("type");
        this.accountId = rs.getLong("account_id");
        this.status = rs.getString("status");
        this.date = rs.getDate("date");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", accountId=" + accountId +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
