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
public class Action {
    private Long id;
    private String title;
    private Integer amount;
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

    public Action(Long id, String title, Integer amount, String type, Long accountId, String status, Date date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.accountId = accountId;
        this.status = status;
        this.date = date;
    }
}
