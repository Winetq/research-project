package database.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Getter
@Setter
@ToString
public class Account {
    private Long id;
    private int balance;
    private Date creationDate;

    public Account(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.balance = rs.getInt("balance");
        this.creationDate = rs.getDate("creation_date");
    }
}
