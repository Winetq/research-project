package agent;

import agent.rest.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DataStore {
    private static final Map<String, List<Long>> queryToTimeElapsed = new HashMap<>();
    private static final Map<String, List<String>> queryToOriginalQueries = new HashMap<>();
    private static final RestClient restClient = new RestClient();
    private static String transaction = "";
    private static String originalTransaction = "";
    private static long startTransactionTime = 0;

    public static void processData(String query, long timeElapsed, boolean autoCommit) {
        String queryWithWildcards = SqlParser.replaceParameters(query);
        if (autoCommit) {
            put(queryWithWildcards, timeElapsed, query);
        } else {
            transaction += queryWithWildcards + ";";
            originalTransaction += query + ";";
            if (startTransactionTime == 0) {
                startTransactionTime = System.nanoTime() - timeElapsed;
            }
        }
        System.err.println(query + " executed in " + timeElapsed + " microseconds");
    }

    public static void commitTransaction(String status) {
        System.err.println(status);
        long transactionTimeElapsed = System.nanoTime() - startTransactionTime;
        put(transaction, TimeUnit.NANOSECONDS.toMicros(transactionTimeElapsed), originalTransaction);
        transaction = "";
        originalTransaction = "";
        startTransactionTime = 0;
    }

    private static void put(String key, long time, String originalQuery) {
        putTime(key, time);
        putOriginalQuery(key, originalQuery);
        System.err.println(queryToTimeElapsed);
        restClient.updateTransactions(queryToTimeElapsed, queryToOriginalQueries);
    }

    private static void putTime(String key, long time) {
        List<Long> timeElapsedList = queryToTimeElapsed.computeIfAbsent(key, k -> new ArrayList<>());
        timeElapsedList.add(time);
        queryToTimeElapsed.put(key, timeElapsedList);
    }

    private static void putOriginalQuery(String key, String originalQuery) {
        List<String> originalQueryList = queryToOriginalQueries.computeIfAbsent(key, k -> new ArrayList<>());
        originalQueryList.add(originalQuery);
        queryToOriginalQueries.put(key, originalQueryList);
    }
}
