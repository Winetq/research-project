package agent.rest.model;

import java.util.List;

public class Transaction {

    private final List<String> transactionQueries;

    private final List<Long> times;

    private final List<String> originalQueries;

    public Transaction(List<String> transactionQueries, List<Long> times, List<String> originalQueries) {
        this.transactionQueries = transactionQueries;
        this.times = times;
        this.originalQueries = originalQueries;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionQueries='" + transactionQueries + '\'' +
                ", times=" + times +
                '}';
    }

    public List<String> getTransactionQueries() {
        return transactionQueries;
    }

    public List<Long> getTimes() {
        return times;
    }

    public List<String> getOriginalQueries() {
        return originalQueries;
    }
}
