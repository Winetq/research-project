package web.database.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Account {
    private Long id;
    private Integer balance;
    private Date creationDate;

    public Account(ResultSet rs) throws SQLException {
        this.id = rs.getLong("id");
        this.balance = rs.getInt("balance");
        this.creationDate = rs.getDate("creation_date");
    }
}
