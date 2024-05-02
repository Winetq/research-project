package agent.rest.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class TransactionBuilder {
    private String transactionWithWildcards;
    private final List<String> transactionWithParameters;
    private final long startTransactionTime;

    public TransactionBuilder(long timeElapsed) {
        this.transactionWithWildcards = "";
        this.transactionWithParameters = new ArrayList<>();
        this.startTransactionTime = System.nanoTime() - timeElapsed;
    }

    public void addQueryWithWildcards(String queryWithWildcards) {
        this.transactionWithWildcards += queryWithWildcards + ";";
    }

    public void addQueryWithParameters(String queryWithParameters) {
        this.transactionWithParameters.add(queryWithParameters);
    }
}
