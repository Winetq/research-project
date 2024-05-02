package agent.rest.model;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class Transaction {
    private final List<String> originalQueries;
    private final Long time;
    private final TransactionStatus status;

    public Transaction(List<String> originalQueries, Long time, TransactionStatus status) {
        this.originalQueries = originalQueries;
        this.time = time;
        this.status = status;
    }
}
