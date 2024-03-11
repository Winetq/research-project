package agent.rest.model;

import java.util.List;

public class Transaction {
    private final List<String> originalQueries;
    private final Long time;
    private final QueryStatus status;

    public Transaction(List<String> originalQueries, Long time, QueryStatus status) {
        this.originalQueries = originalQueries;
        this.time = time;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "originalQueries=" + originalQueries +
                ", time=" + time +
                ", status=" + status +
                '}';
    }

    public List<String> getOriginalQueries() {
        return originalQueries;
    }

    public Long getTime() {
        return time;
    }

    public QueryStatus getStatus() {
        return status;
    }
}
