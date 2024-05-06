package agent;

import agent.rest.model.Transaction;
import agent.rest.model.TransactionStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEqualsDeep;

public class DataStoreTest {
    private final static String URL = "jdbc:h2:mem:test";
    private final static String USER = "admin";
    private final static String PASSWORD = "admin";

    private final static String SELECT_QUERY = "SELECT * FROM t1";
    private final static String INSERT_QUERY_WITHOUT_PARAMETERS = "INSERT INTO t2 (x1, x2, x3) VALUES (?, ?, ?)";
    private final static String INSERT_QUERY_WITH_PARAMETERS = "INSERT INTO t2 (x1, x2, x3) VALUES (1, 50, 100)";
    private final static String UPDATE_QUERY_WITHOUT_PARAMETERS = "UPDATE t3 SET x = x + ? WHERE id = ?";
    private final static String UPDATE_QUERY_WITH_PARAMETERS = "UPDATE t3 SET x = x + 100 WHERE id = 1";

    private final static Map<String, List<Transaction>> expected = Map.of(
            SELECT_QUERY,
            List.of(
                    new Transaction(List.of(SELECT_QUERY), 1_000L, TransactionStatus.DEFAULT),
                    new Transaction(List.of(SELECT_QUERY), 1_000L, TransactionStatus.DEFAULT),
                    new Transaction(List.of(SELECT_QUERY), 1_000L, TransactionStatus.DEFAULT)
            ),
            INSERT_QUERY_WITHOUT_PARAMETERS,
            List.of(
                    new Transaction(List.of(INSERT_QUERY_WITH_PARAMETERS), 2_500L, TransactionStatus.DEFAULT)
            ),
            String.join(";", INSERT_QUERY_WITHOUT_PARAMETERS, UPDATE_QUERY_WITHOUT_PARAMETERS) + ";",
            List.of(
                    new Transaction(List.of(INSERT_QUERY_WITH_PARAMETERS, UPDATE_QUERY_WITH_PARAMETERS), 7_500L, TransactionStatus.COMMIT),
                    new Transaction(List.of(INSERT_QUERY_WITH_PARAMETERS, UPDATE_QUERY_WITH_PARAMETERS), 7_500L, TransactionStatus.ROLLBACK)
            )
    );

    @BeforeMethod
    public void setup() {
        DataStore.queryToTransactions.clear();
    }

    @Test
    public void whenDatabaseQueriesAndTransactionsAreExecutedInSeparateConnections_thenDataAreProcessedCorrectly() throws SQLException {
        Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
        DataStore.processData(SELECT_QUERY, List.of(), connection1, 1_000);
        connection1.close();

        Connection connection2 = DriverManager.getConnection(URL, USER, PASSWORD);
        DataStore.processData(INSERT_QUERY_WITHOUT_PARAMETERS, List.of("1", "50", "100"), connection2, 2_500);
        connection2.close();

        Connection connection3 = DriverManager.getConnection(URL, USER, PASSWORD);
        connection3.setAutoCommit(false);
        DataStore.processData(INSERT_QUERY_WITH_PARAMETERS, List.of(), connection3, 2_500);
        DataStore.processData(UPDATE_QUERY_WITHOUT_PARAMETERS, List.of("100", "1"), connection3, 5_000);
        DataStore.executeTransaction(connection3, "COMMIT");
        connection3.close();

        Connection connection4 = DriverManager.getConnection(URL, USER, PASSWORD);
        DataStore.processData(SELECT_QUERY, List.of(), connection4, 1_000);
        connection4.close();

        Connection connection5 = DriverManager.getConnection(URL, USER, PASSWORD);
        connection5.setAutoCommit(false);
        DataStore.processData(INSERT_QUERY_WITH_PARAMETERS, List.of(), connection5, 2_500);
        DataStore.processData(UPDATE_QUERY_WITHOUT_PARAMETERS, List.of("100", "1"), connection5, 5_000);
        DataStore.executeTransaction(connection5, "ROLLBACK");
        connection5.close();

        Connection connection6 = DriverManager.getConnection(URL, USER, PASSWORD);
        DataStore.processData(SELECT_QUERY, List.of(), connection6, 1_000);
        connection6.close();

        assertEqualsDeep(DataStore.queryToTransactions, expected);
    }

    @Test
    public void whenDatabaseQueriesAndTransactionsAreExecutedInTheSameConnection_thenDataAreProcessedCorrectly() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        DataStore.processData(SELECT_QUERY, List.of(), connection, 1_000);

        DataStore.processData(INSERT_QUERY_WITHOUT_PARAMETERS, List.of("1", "50", "100"), connection, 2_500);

        connection.setAutoCommit(false);
        DataStore.processData(INSERT_QUERY_WITH_PARAMETERS, List.of(), connection, 2_500);
        DataStore.processData(UPDATE_QUERY_WITHOUT_PARAMETERS, List.of("100", "1"), connection, 5_000);
        DataStore.executeTransaction(connection, "COMMIT");
        connection.setAutoCommit(true);

        DataStore.processData(SELECT_QUERY, List.of(), connection, 1_000);

        connection.setAutoCommit(false);
        DataStore.processData(INSERT_QUERY_WITH_PARAMETERS, List.of(), connection, 2_500);
        DataStore.processData(UPDATE_QUERY_WITHOUT_PARAMETERS, List.of("100", "1"), connection, 5_000);
        DataStore.executeTransaction(connection, "ROLLBACK");
        connection.setAutoCommit(true);

        DataStore.processData(SELECT_QUERY, List.of(), connection, 1_000);

        connection.close();

        assertEqualsDeep(DataStore.queryToTransactions, expected);
    }

    @Test
    public void whenSeveralDatabaseTransactionsAreExecutedInParallel_thenDataAreProcessedCorrectly() throws SQLException {
        Map<String, List<Transaction>> expected = Map.of(
                String.join(";", INSERT_QUERY_WITHOUT_PARAMETERS, UPDATE_QUERY_WITHOUT_PARAMETERS) + ";",
                List.of(
                        new Transaction(List.of(INSERT_QUERY_WITH_PARAMETERS, UPDATE_QUERY_WITH_PARAMETERS), 7_500L, TransactionStatus.ROLLBACK),
                        new Transaction(List.of(INSERT_QUERY_WITH_PARAMETERS, UPDATE_QUERY_WITH_PARAMETERS), 7_500L, TransactionStatus.COMMIT),
                        new Transaction(List.of(INSERT_QUERY_WITH_PARAMETERS, UPDATE_QUERY_WITH_PARAMETERS), 7_500L, TransactionStatus.ROLLBACK)
                ),
                String.join(";", INSERT_QUERY_WITHOUT_PARAMETERS, INSERT_QUERY_WITHOUT_PARAMETERS, UPDATE_QUERY_WITHOUT_PARAMETERS) + ";",
                List.of(
                        new Transaction(List.of(INSERT_QUERY_WITH_PARAMETERS, INSERT_QUERY_WITH_PARAMETERS, UPDATE_QUERY_WITH_PARAMETERS), 10_000L, TransactionStatus.COMMIT)
                )
        );

        Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
        Connection connection2 = DriverManager.getConnection(URL, USER, PASSWORD);
        Connection connection3 = DriverManager.getConnection(URL, USER, PASSWORD);

        connection1.setAutoCommit(false);
        DataStore.processData(INSERT_QUERY_WITH_PARAMETERS, List.of(), connection1, 2_500);

        connection2.setAutoCommit(false);
        DataStore.processData(INSERT_QUERY_WITH_PARAMETERS, List.of(), connection2, 2_500);
        DataStore.processData(UPDATE_QUERY_WITHOUT_PARAMETERS, List.of("100", "1"), connection2, 5_000);

        connection3.setAutoCommit(false);
        DataStore.processData(INSERT_QUERY_WITH_PARAMETERS, List.of(), connection3, 2_500);
        DataStore.processData(UPDATE_QUERY_WITHOUT_PARAMETERS, List.of("100", "1"), connection3, 5_000);
        DataStore.executeTransaction(connection3, "ROLLBACK");

        DataStore.processData(UPDATE_QUERY_WITHOUT_PARAMETERS, List.of("100", "1"), connection1, 5_000);
        DataStore.executeTransaction(connection1, "COMMIT");

        DataStore.executeTransaction(connection2, "ROLLBACK");

        DataStore.processData(INSERT_QUERY_WITH_PARAMETERS, List.of(), connection3, 2_500);
        DataStore.processData(INSERT_QUERY_WITHOUT_PARAMETERS, List.of("1", "50", "100"), connection3, 2_500);
        DataStore.processData(UPDATE_QUERY_WITHOUT_PARAMETERS, List.of("100", "1"), connection3, 5_000);
        DataStore.executeTransaction(connection3, "COMMIT");

        connection1.close();
        connection2.close();
        connection3.close();

        assertEqualsDeep(DataStore.queryToTransactions, expected);
    }
}
