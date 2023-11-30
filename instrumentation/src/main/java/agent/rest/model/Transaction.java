package agent.rest.model;

import java.util.List;

public class Transaction {

    public Transaction(List<String> transactionQueries, List<Long> times) {
        this.transactionQueries = transactionQueries;
        this.times = times;
    }

    private List<String> transactionQueries;

    private List<Long> times;

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
}
