package agent.rest.model;

import java.util.List;

public class Transaction {

    public Transaction(String transactionQueries, List<Long> times) {
        this.transactionQueries = transactionQueries;
        this.times = times;
    }

    private String transactionQueries;

    private List<Long> times;

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
