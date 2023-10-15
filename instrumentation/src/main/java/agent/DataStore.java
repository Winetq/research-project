package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private final static Map<String, List<Long>> queryToTimeElapsed = new HashMap<>();

    public static void put(String query, long timeElapsed) {
        List<Long> timeElapsedList = queryToTimeElapsed.computeIfAbsent(query, k -> new ArrayList<>());
        timeElapsedList.add(timeElapsed);
        queryToTimeElapsed.put(query, timeElapsedList);
        System.err.println(queryToTimeElapsed);
        System.err.println(query + " executed in " + timeElapsed + " microseconds");
    }
}
