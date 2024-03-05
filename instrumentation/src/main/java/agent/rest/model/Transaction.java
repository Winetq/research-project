package agent.rest.model;

public class Transaction {
    private final String originalQuery;
    private final Long time;
    private final QueryStatus status;

    public Transaction(String originalQuery, Long time, QueryStatus status) {
        this.originalQuery = originalQuery;
        this.time = time;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "originalQuery='" + originalQuery + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                '}';
    }

    public String getOriginalQuery() {
        return originalQuery;
    }

    public Long getTime() {
        return time;
    }

    public QueryStatus getStatus() {
        return status;
    }
}
