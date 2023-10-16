package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private final static Map<String, List<Long>> queryToTimeElapsed = new HashMap<>();

    public static void put(String query, long timeElapsed) {
        String queryWithWildcards =  replaceParametersWithWildcards(query);
        List<Long> timeElapsedList = queryToTimeElapsed.computeIfAbsent(queryWithWildcards, k -> new ArrayList<>());
        timeElapsedList.add(timeElapsed);
        queryToTimeElapsed.put(queryWithWildcards, timeElapsedList);
        System.err.println(queryToTimeElapsed);
        System.err.println(queryWithWildcards + " executed in " + timeElapsed + " microseconds");
    }

    public static String replaceParametersWithWildcards(String sql) {
        String valuesSqlKeyword = "VALUES";
        if (sql.toUpperCase().contains(valuesSqlKeyword)) {
            int indexOfValues = sql.indexOf(valuesSqlKeyword);
            String sqlWithoutParameters = sql.substring(0, indexOfValues + valuesSqlKeyword.length());
            long numberOfParameters = sqlWithoutParameters.chars().filter(ch -> ch == ',').count() + 1;
            String wildcards = buildWildcards(numberOfParameters);
            return sqlWithoutParameters.concat(wildcards);
        }
        return sql;
    }

    private static String buildWildcards(long numberOfParameters) {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 1; i <= numberOfParameters; i++) {
            if (i != numberOfParameters) {
                sb.append("?, ");
            } else {
                sb.append("?)");
            }
        }
        return sb.toString();
    }
}
