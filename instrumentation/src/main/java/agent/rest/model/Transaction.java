package agent.rest.model;

import java.util.List;

public class Transaction {

    private final String transactionQueries;
    private final List<Long> times;

    public Transaction(String transactionQueries, List<Long> times) {
        this.transactionQueries = transactionQueries;
        this.times = times;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionQueries='" + transactionQueries + '\'' +
                ", times=" + times +
                '}';
    }

    public String getTransactionQueries() {
        return transactionQueries;
    }

    public List<Long> getTimes() {
        return times;
    }
}
