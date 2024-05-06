package agent;

import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

@Test
public class DataStoreTest {
    private final static String URL = "jdbc:h2:mem:test";
    private final static String USER = "admin";
    private final static String PASSWORD = "admin";

    public void test() throws SQLException {
        Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
        Connection connection2 = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("TEST");
        System.out.println(connection1);
        System.out.println(connection2);
        System.out.println(connection1.getAutoCommit());
        DataStore.processData("select * from x", new ArrayList<>(), connection1, 1_000);
        connection1.close();
        connection2.close();
    }
}
