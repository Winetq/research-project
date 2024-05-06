package agent;

import agent.rest.RestClient;
import agent.rest.model.Transaction;
import agent.rest.model.TransactionBuilder;
import agent.rest.model.TransactionStatus;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class DataStore {
    static final Map<String, List<Transaction>> queryToTransactions = new ConcurrentHashMap<>();
    private static final Map<Connection, TransactionBuilder> connectionToTransaction = new ConcurrentHashMap<>();
    private static final RestClient restClient = new RestClient();
    @Setter private static DataExporter dataExporter = DataExporter.CONSOLE;

    public static void processData(String query, List<String> parameters, Connection connection, long timeElapsed) throws SQLException {
        String queryWithParameters = SqlParser.replaceWildcards(query, parameters);
        String queryWithWildcards = SqlParser.replaceParameters(query);
        if (connection.getAutoCommit()) {
            put(queryWithWildcards, List.of(queryWithParameters), timeElapsed, TransactionStatus.DEFAULT);
        } else { // transaction starts
            put(connection, timeElapsed, queryWithWildcards, queryWithParameters);
        }
        System.err.println("Parameters: " + parameters);
        System.err.println(query + " executed in " + timeElapsed + " microseconds");
    }

    public static void executeTransaction(Connection connection, String status) { // commit or rollback happens (transaction ends)
        TransactionBuilder transaction = connectionToTransaction.get(connection);
        long transactionTimeElapsed = System.nanoTime() - transaction.getStartTransactionTime();
        put(transaction.getTransactionWithWildcards(), transaction.getTransactionWithParameters(),
                TimeUnit.NANOSECONDS.toMicros(transactionTimeElapsed), TransactionStatus.of(status));
        connectionToTransaction.remove(connection);
    }

    private static void put(String key, List<String> originalQueries, long time, TransactionStatus status) {
        List<Transaction> transactions = queryToTransactions.computeIfAbsent(key, k -> new ArrayList<>());
        transactions.add(new Transaction(originalQueries, time, status));
        queryToTransactions.put(key, transactions);
        System.err.println(status);
        System.err.println(queryToTransactions);
        if (dataExporter == DataExporter.SERVER) restClient.updateTransactions(queryToTransactions);
    }

    private static void put(Connection connection, long timeElapsed, String queryWithWildcards, String queryWithParameters) {
        TransactionBuilder transactionBuilder = connectionToTransaction.computeIfAbsent(connection, k -> new TransactionBuilder(timeElapsed));
        transactionBuilder.addQueryWithWildcards(queryWithWildcards);
        transactionBuilder.addQueryWithParameters(queryWithParameters);
        connectionToTransaction.put(connection, transactionBuilder);
    }
}
