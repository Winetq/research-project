package agent;

import agent.rest.RestClient;
import agent.rest.model.QueryStatus;
import agent.rest.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DataStore {
    private static final Map<String, List<Transaction>> queryToTransactions = new HashMap<>();
    private static final RestClient restClient = new RestClient();
    private static List<String> transactionWithParameters = new ArrayList<>();
    private static String transactionWithWildcards = "";
    private static long startTransactionTime = 0;

    public static void processData(String query, List<String> parameters, long timeElapsed, boolean autoCommit) {
        String queryWithParameters = SqlParser.replaceWildcards(query, parameters);
        String queryWithWildcards = SqlParser.replaceParameters(query);
        if (autoCommit) {
            put(queryWithWildcards, List.of(queryWithParameters), timeElapsed, QueryStatus.DEFAULT);
        } else {
            transactionWithWildcards += queryWithWildcards + ";";
            transactionWithParameters.add(queryWithParameters);
            if (startTransactionTime == 0) {
                startTransactionTime = System.nanoTime() - timeElapsed;
            }
        }
        System.err.println("Parameters: " + parameters);
        System.err.println(query + " executed in " + timeElapsed + " microseconds");
    }

    public static void executeTransaction(String status) {
        long transactionTimeElapsed = System.nanoTime() - startTransactionTime;
        put(transactionWithWildcards, transactionWithParameters, TimeUnit.NANOSECONDS.toMicros(transactionTimeElapsed), QueryStatus.of(status));
        transactionWithWildcards = "";
        transactionWithParameters = new ArrayList<>();
        startTransactionTime = 0;
    }

    private static void put(String key, List<String> originalQueries, long time, QueryStatus status) {
        List<Transaction> transactions = queryToTransactions.computeIfAbsent(key, k -> new ArrayList<>());
        transactions.add(new Transaction(originalQueries, time, status));
        queryToTransactions.put(key, transactions);
        System.err.println(status);
        System.err.println(queryToTransactions);
        restClient.updateTransactions(queryToTransactions);
    }
}
