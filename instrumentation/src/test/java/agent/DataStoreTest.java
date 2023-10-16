package agent;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class DataStoreTest {

    @Test(dataProvider = "getSqlQueries")
    public void testReplaceParametersWithWildcards(String sql, String replacedSql) {
        assertEquals(DataStore.replaceParametersWithWildcards(sql), replacedSql);
    }

    @DataProvider
    public static Object[][] getSqlQueries() {
        return new Object[][] {
                {"SELECT * FROM account", "SELECT * FROM account"},
                {"SELECT * FROM customer", "SELECT * FROM customer"},
                {"SELECT * FROM action", "SELECT * FROM action"},
                {"INSERT INTO Action (title, amount, type, account_id, status, date) VALUES('trip', 10000, 'card', 2, 'done', '2023-10-16 10:40:38.0')",
                        "INSERT INTO Action (title, amount, type, account_id, status, date) VALUES(?, ?, ?, ?, ?, ?)"},
                {"INSERT INTO account (id, balance, creation_date) VALUES(14, 100, '2023-10-16 10:41:48.0')",
                        "INSERT INTO account (id, balance, creation_date) VALUES(?, ?, ?)"},
                {"INSERT INTO test (id) VALUES(14)",
                        "INSERT INTO test (id) VALUES(?)"},
                {"INSERT INTO test (id) VALUES()",
                        "INSERT INTO test (id) VALUES(?)"}
        };
    }
}
