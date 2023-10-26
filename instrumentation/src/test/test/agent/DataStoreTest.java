package agent;

import static agent.DataStore.replaceParameters;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DataStoreTest {

    @ParameterizedTest
    @MethodSource("provideSelectTestScenarios")
    void whenReplaceParametersInSelect_thenParametersAreReplacedWithWildcard(String actual, String expected) {
        assertEquals(expected, replaceParameters(actual));
    }

    @ParameterizedTest
    @MethodSource("provideUpdateTestScenarios")
    void whenReplaceParametersInUpdate_thenParametersAreReplacedWithWildcard(String actual, String expected) {
        assertEquals(expected, replaceParameters(actual));
    }

    @ParameterizedTest
    @MethodSource("provideDeleteTestScenarios")
    void whenReplaceParametersInDelete_thenParametersAreReplacedWithWildcard(String actual, String expected) {
        assertEquals(expected, replaceParameters(actual));
    }

    private static Stream<Arguments> provideSelectTestScenarios() {
        return Stream.of(
                Arguments.of("SELECT * FROM table", "SELECT * FROM table"),
                Arguments.of("SELECT d1, d2, d3 FROM table", "SELECT d1, d2, d3 FROM table"),
                Arguments.of("SELECT * FROM table WHERE x = 'y' AND x2 = 4", "SELECT * FROM table WHERE x = ? AND x2 = ?"),
                Arguments.of("SELECT * FROM table WHERE x1 = 'y' AND x2 > 0", "SELECT * FROM table WHERE x1 = ? AND x2 > ?"),
                Arguments.of("SELECT * FROM table WHERE x1 = 'y' AND x2 > 0 OR name = 'xyz'", "SELECT * FROM table WHERE x1 = ? AND x2 > ? OR name = ?"),
                Arguments.of("select * from table where x1 = 'y' AND x2 > 0 OR name = 'xyz'", "SELECT * FROM table WHERE x1 = ? AND x2 > ? OR name = ?"),
                Arguments.of("Select * From table WHERE x1 > 'y' AND x2 < 0 OR name = 'xyz'", "SELECT * FROM table WHERE x1 > ? AND x2 < ? OR name = ?"),
                Arguments.of("Select * From table WHERE x1 > 'y' AND x2 < 0 OR name = 'xyz'", "SELECT * FROM table WHERE x1 > ? AND x2 < ? OR name = ?"),
                Arguments.of("SELECT * FROM table WHERE (x1 = 'y' AND x2 > 0) OR (name = 'xyz' AND b = true)", "SELECT * FROM table WHERE (x1 = ? AND x2 > ?) OR (name = ? AND b = ?)"),
                Arguments.of("SELECT d1, d2 FROM table WHERE (x1 = 'y' AND x2 > 0) OR (name = 'xyz' AND b = true)", "SELECT d1, d2 FROM table WHERE (x1 = ? AND x2 > ?) OR (name = ? AND b = ?)"),
                Arguments.of("SELECT * FROM table WHERE x1 = (SELECT id FROM next_table WHERE y = 1)", "SELECT * FROM table WHERE x1 = (SELECT id FROM next_table WHERE y = ?)"),
                Arguments.of("SELECT * FROM table WHERE x1 = (SELECT id FROM next_table WHERE y = 1) AND x2 = 'z'", "SELECT * FROM table WHERE x1 = (SELECT id FROM next_table WHERE y = ?) AND x2 = ?"),
                Arguments.of("SELECT * FROM table WHERE x1 = " + "'" + "test" + "'" + " " + "AND x2 > 11 AND x3 = 'y' AND x4 < 6", "SELECT * FROM table WHERE x1 = ? AND x2 > ? AND x3 = ? AND x4 < ?"),
                Arguments.of("SELECT * FROM table LEFT JOIN next_table ON table.id = next_table.fId WHERE x1 = 'y' AND x2 > 0 OR name = 'xyz'", "SELECT * FROM table LEFT JOIN next_table ON table.id = next_table.fId WHERE x1 = ? AND x2 > ? OR name = ?"),
                Arguments.of("SELECT * FROM table WHERE x1 = 'y' AND x2 > 0 OR name = 'xyz' OR xyz = 'test' ORDER BY xyz", "SELECT * FROM table WHERE x1 = ? AND x2 > ? OR name = ? OR xyz = ? ORDER BY xyz")
        );
    }

    private static Stream<Arguments> provideUpdateTestScenarios() {
        return Stream.of(
                Arguments.of("Update table SET x = 'y' WHERE x2 = 'y2'", "UPDATE table SET x = ? WHERE x2 = ?"),
                Arguments.of("Update table SET x = 'y' WHERE x2 = 'y2' AND x3 = 2", "UPDATE table SET x = ? WHERE x2 = ? AND x3 = ?"),
                Arguments.of("Update table SET x = 'y' WHERE (x2 = 'y2' AND x3 = 2) OR x4 > 11", "UPDATE table SET x = ? WHERE (x2 = ? AND x3 = ?) OR x4 > ?"),
                Arguments.of("Update table SET x = 'y' WHERE x2 = 'y2' AND x3 = 2 OR xyz = 'xyz'", "UPDATE table SET x = ? WHERE x2 = ? AND x3 = ? OR xyz = ?"),
                Arguments.of("Update table SET x = 'y' WHERE x2 = false OR xyz = 'xyz'", "UPDATE table SET x = ? WHERE x2 = ? OR xyz = ?"),
                Arguments.of("Update table SET x = 'y' WHERE x2 = FALSE OR xyz = 'xyz'", "UPDATE table SET x = ? WHERE x2 = ? OR xyz = ?"),
                Arguments.of("Update table SET x1 = 'y', x2 = 'z'", "UPDATE table SET x1 = ?, x2 = ?"),
                Arguments.of("Update table SET x1 = 'y', x2 = 'z' WHERE (x3 > 0 AND x4 = false) OR (x6 < 123 AND x7 = true) OR (x8 = 'xyz' AND x9 > 1)", "UPDATE table SET x1 = ?, x2 = ? WHERE (x3 > ? AND x4 = ?) OR (x6 < ? AND x7 = ?) OR (x8 = ? AND x9 > ?)")
        );
    }

    private static Stream<Arguments> provideDeleteTestScenarios() {
        return Stream.of(
                Arguments.of("DELETE FROM table WHERE x2 = 'y2'", "DELETE FROM table WHERE x2 = ?"),
                Arguments.of("DELETE FROM table WHERE x2 = 'y2' AND x3 = 2", "DELETE FROM table WHERE x2 = ? AND x3 = ?"),
                Arguments.of("DELETE FROM table WHERE (x2 = 'y2' AND x3 = 2) OR x4 > 11", "DELETE FROM table WHERE (x2 = ? AND x3 = ?) OR x4 > ?"),
                Arguments.of("DELETE FROM table WHERE x2 = 'y2' AND x3 = 2 OR xyz = 'xyz'", "DELETE FROM table WHERE x2 = ? AND x3 = ? OR xyz = ?"),
                Arguments.of("DELETE FROM table WHERE x2 = false OR xyz = 'xyz'", "DELETE FROM table WHERE x2 = ? OR xyz = ?"),
                Arguments.of("DELETE FROM table", "DELETE FROM table"),
                Arguments.of("DELETE FROM table WHERE (x3 > 0 AND x4 = false) OR (x6 < 123 AND x7 = true) OR (x8 = 'xyz' AND x9 > 1)", "DELETE FROM table WHERE (x3 > ? AND x4 = ?) OR (x6 < ? AND x7 = ?) OR (x8 = ? AND x9 > ?)")
        );
    }
}
