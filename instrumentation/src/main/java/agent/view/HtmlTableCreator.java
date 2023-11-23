package agent.view;

import java.util.List;
import java.util.Map;

public class HtmlTableCreator {

    public String createTable(Map<String, List<Long>> queryToTimeElapsed) {
        String content = "";

        for (Map.Entry<String, List<Long>> entry: queryToTimeElapsed.entrySet()) {
            content = String.format("%s <tr> <td> %s </td>" +
                                    "<td> %s </td></tr>",
                                    content, entry.getKey(), entry.getValue() );
        }
        return content;
    }
}
