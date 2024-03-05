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
    private static String transaction = "";
    private static String originalTransaction = "";
    private static long startTransactionTime = 0;

    public static void processData(String query, long timeElapsed, boolean autoCommit) {
        String queryWithWildcards = SqlParser.replaceParameters(query);
        if (autoCommit) {
            put(queryWithWildcards, query, timeElapsed, QueryStatus.DEFAULT);
        } else {
            transaction += queryWithWildcards + ";";
            originalTransaction += query + ";";
            if (startTransactionTime == 0) {
                startTransactionTime = System.nanoTime() - timeElapsed;
            }
        }
        System.err.println(query + " executed in " + timeElapsed + " microseconds");
    }

    public static void executeTransaction(String status) {
        long transactionTimeElapsed = System.nanoTime() - startTransactionTime;
        put(transaction, originalTransaction, TimeUnit.NANOSECONDS.toMicros(transactionTimeElapsed), QueryStatus.of(status));
        transaction = "";
        originalTransaction = "";
        startTransactionTime = 0;
    }

    private static void put(String key, String originalQuery, long time, QueryStatus status) {
        List<Transaction> transactions = queryToTransactions.computeIfAbsent(key, k -> new ArrayList<>());
        transactions.add(new Transaction(originalQuery, time, status));
        queryToTransactions.put(key, transactions);
        System.err.println(status);
        System.err.println(queryToTransactions);
        restClient.updateTransactions(queryToTransactions);
    }
}
