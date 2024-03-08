package agent;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

@Test
public class SqlParserTest {

    @Test(dataProvider = "provideSelectTestScenarios")
    public void whenReplaceParametersInSelect_thenParametersAreReplacedWithWildcards(String actual, String expected) {
        assertEquals(SqlParser.replaceParameters(actual), expected);
    }

    @DataProvider
    private static Object[][] provideSelectTestScenarios() {
        return new Object[][] {
                {"SELECT * FROM table", "SELECT * FROM table"},
                {"SELECT d1, d2, d3 FROM table", "SELECT d1, d2, d3 FROM table"},
                {"SELECT * FROM table WHERE x = 'y' AND x2 = 4", "SELECT * FROM table WHERE x = ? AND x2 = ?"},
                {"SELECT * FROM table WHERE x1 = 'y' AND x2 > 0", "SELECT * FROM table WHERE x1 = ? AND x2 > ?"},
                {"SELECT * FROM table WHERE x1 = 'y' AND x2 > 0 OR name = 'xyz'", "SELECT * FROM table WHERE x1 = ? AND x2 > ? OR name = ?"},
                {"select * from table where x1 = 'y' AND x2 > 0 OR name = 'xyz'", "SELECT * FROM table WHERE x1 = ? AND x2 > ? OR name = ?"},
                {"Select * From table WHERE x1 > 'y' AND x2 < 0 OR name = 'xyz'", "SELECT * FROM table WHERE x1 > ? AND x2 < ? OR name = ?"},
                {"Select * From table WHERE x1 > 'y' AND x2 < 0 OR name = 'xyz'", "SELECT * FROM table WHERE x1 > ? AND x2 < ? OR name = ?"},
                {"SELECT * FROM table WHERE (x1 = 'y' AND x2 > 0) OR (name = 'xyz' AND b = true)", "SELECT * FROM table WHERE (x1 = ? AND x2 > ?) OR (name = ? AND b = ?)"},
                {"SELECT d1, d2 FROM table WHERE (x1 = 'y' AND x2 > 0) OR (name = 'xyz' AND b = true)", "SELECT d1, d2 FROM table WHERE (x1 = ? AND x2 > ?) OR (name = ? AND b = ?)"},
                {"SELECT * FROM table WHERE x1 = (SELECT id FROM next_table WHERE y = 1)", "SELECT * FROM table WHERE x1 = (SELECT id FROM next_table WHERE y = ?)"},
                {"SELECT * FROM table WHERE x1 = (SELECT id FROM next_table WHERE y = 1) AND x2 = 'z'", "SELECT * FROM table WHERE x1 = (SELECT id FROM next_table WHERE y = ?) AND x2 = ?"},
                {"SELECT * FROM table WHERE x1 = " + "'" + "test" + "'" + " " + "AND x2 > 11 AND x3 = 'y' AND x4 < 6", "SELECT * FROM table WHERE x1 = ? AND x2 > ? AND x3 = ? AND x4 < ?"},
                {"SELECT * FROM table LEFT JOIN next_table ON table.id = next_table.fId WHERE x1 = 'y' AND x2 > 0 OR name = 'xyz'", "SELECT * FROM table LEFT JOIN next_table ON table.id = next_table.fId WHERE x1 = ? AND x2 > ? OR name = ?"},
                {"SELECT * FROM table WHERE x1 = 'y' AND x2 > 0 OR name = 'xyz' OR xyz = 'test' ORDER BY xyz", "SELECT * FROM table WHERE x1 = ? AND x2 > ? OR name = ? OR xyz = ? ORDER BY xyz"}
        };
    }

    @Test(dataProvider = "provideUpdateTestScenarios")
    public void whenReplaceParametersInUpdate_thenParametersAreReplacedWithWildcards(String actual, String expected) {
        assertEquals(SqlParser.replaceParameters(actual), expected);
    }

    @DataProvider
    private static Object[][] provideUpdateTestScenarios() {
        return new Object[][] {
                {"Update table SET x = 'y' WHERE x2 = 'y2'", "UPDATE table SET x = ? WHERE x2 = ?"},
                {"Update table SET x = 'y' WHERE x2 = 'y2' AND x3 = 2", "UPDATE table SET x = ? WHERE x2 = ? AND x3 = ?"},
                {"Update table SET x = 'y' WHERE (x2 = 'y2' AND x3 = 2) OR x4 > 11", "UPDATE table SET x = ? WHERE (x2 = ? AND x3 = ?) OR x4 > ?"},
                {"Update table SET x = 'y' WHERE x2 = 'y2' AND x3 = 2 OR xyz = 'xyz'", "UPDATE table SET x = ? WHERE x2 = ? AND x3 = ? OR xyz = ?"},
                {"Update table SET x = 'y' WHERE x2 = false OR xyz = 'xyz'", "UPDATE table SET x = ? WHERE x2 = ? OR xyz = ?"},
                {"Update table SET x = 'y' WHERE x2 = FALSE OR xyz = 'xyz'", "UPDATE table SET x = ? WHERE x2 = ? OR xyz = ?"},
                {"Update table SET x1 = 'y', x2 = 'z'", "UPDATE table SET x1 = ?, x2 = ?"},
                {"Update table SET x1 = 'y', x2 = 'z' WHERE (x3 > 0 AND x4 = false) OR (x6 < 123 AND x7 = true) OR (x8 = 'xyz' AND x9 > 1)", "UPDATE table SET x1 = ?, x2 = ? WHERE (x3 > ? AND x4 = ?) OR (x6 < ? AND x7 = ?) OR (x8 = ? AND x9 > ?)"}
        };
    }

    @Test(dataProvider = "provideDeleteTestScenarios")
    public void whenReplaceParametersInDelete_thenParametersAreReplacedWithWildcards(String actual, String expected) {
        assertEquals(SqlParser.replaceParameters(actual), expected);
    }

    @DataProvider
    private static Object[][] provideDeleteTestScenarios() {
        return new Object[][] {
                {"DELETE FROM table WHERE x2 = 'y2'", "DELETE FROM table WHERE x2 = ?"},
                {"DELETE FROM table WHERE x2 = 'y2' AND x3 = 2", "DELETE FROM table WHERE x2 = ? AND x3 = ?"},
                {"DELETE FROM table WHERE (x2 = 'y2' AND x3 = 2) OR x4 > 11", "DELETE FROM table WHERE (x2 = ? AND x3 = ?) OR x4 > ?"},
                {"DELETE FROM table WHERE x2 = 'y2' AND x3 = 2 OR xyz = 'xyz'", "DELETE FROM table WHERE x2 = ? AND x3 = ? OR xyz = ?"},
                {"DELETE FROM table WHERE x2 = false OR xyz = 'xyz'", "DELETE FROM table WHERE x2 = ? OR xyz = ?"},
                {"DELETE FROM table", "DELETE FROM table"},
                {"DELETE FROM table WHERE (x3 > 0 AND x4 = false) OR (x6 < 123 AND x7 = true) OR (x8 = 'xyz' AND x9 > 1)", "DELETE FROM table WHERE (x3 > ? AND x4 = ?) OR (x6 < ? AND x7 = ?) OR (x8 = ? AND x9 > ?)"}
        };
    }

    @Test(dataProvider = "provideInsertTestScenarios")
    public void whenReplaceParametersInInsert_thenParametersAreReplacedWithWildcards(String actual, String expected) {
        assertEquals(SqlParser.replaceParameters(actual), expected);
    }

    @DataProvider
    private static Object[][] provideInsertTestScenarios() {
        return new Object[][] {
                {"INSERT INTO Action (title, amount, type, account_id, status, date) VALUES ('trip', 10000, 'card', 2, 'done', '2023-10-16 10:40:38.0')", "INSERT INTO Action (title, amount, type, account_id, status, date) VALUES (?, ?, ?, ?, ?, ?)"},
                {"INSERT INTO account (id, balance, creation_date) VALUES (14, 100, '2023-10-16 10:41:48.0')", "INSERT INTO account (id, balance, creation_date) VALUES (?, ?, ?)"},
                {"INSERT INTO test (id) VALUES (14)", "INSERT INTO test (id) VALUES (?)"},
                {"INSERT INTO test (id) VALUES ()", "INSERT INTO test (id) VALUES ()"},
                {"INSERT INTO account (id, balance, creation_date) VALUES(?, ?, ?)", "INSERT INTO account (id, balance, creation_date) VALUES (?, ?, ?)"}
        };
    }

    @Test(dataProvider = "provideTestScenarios")
    public void whenReplaceWildcardsIsInvoked_thenWildcardsAreReplacedWithParameters(String sql, List<String> parameters, String expected) {
        assertEquals(SqlParser.replaceWildcards(sql, parameters), expected);
    }

    @DataProvider
    private static Object[][] provideTestScenarios() {
        return new Object[][] {
                {"INSERT INTO account (id, balance, creation_date) VALUES(?, ?, ?)", List.of("99", "999", "2024-03-08 11:08:41+01"), "INSERT INTO account (id, balance, creation_date) VALUES(99, 999, 2024-03-08 11:08:41+01)"},
                {"INSERT INTO account (id, balance, creation_date) VALUES(99, ?, 2024-03-08 11:08:41+01)", List.of("999"), "INSERT INTO account (id, balance, creation_date) VALUES(99, 999, 2024-03-08 11:08:41+01)"},
                {"SELECT * FROM table", List.of(), "SELECT * FROM table"},
                {"Update table SET x = ? WHERE x2 = ?", List.of("?", "1000"), "Update table SET x = 1000 WHERE x2 = ?"}
        };
    }
}
