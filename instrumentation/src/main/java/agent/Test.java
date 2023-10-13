package agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public final static Map<String, List<Long>> queryToTimeElapsed2 = new HashMap<>();

    public static void put(String query, long timeElapsed) {
        System.out.println("SUPER METODA");
        System.out.println(query + ": " + timeElapsed);
    }
}
