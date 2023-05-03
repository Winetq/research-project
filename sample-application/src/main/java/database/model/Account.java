package database.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Account {

    private Long id;
    private int balance;
    private Date creationDate;

    public Account(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.balance = rs.getInt("balance");
        this.creationDate = rs.getDate("creation_date");
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", creationDate=" + creationDate +
                '}';
    }
}
