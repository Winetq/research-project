package agent;

import java.io.*;
import java.nio.charset.StandardCharsets;

import agent.configuration.Configuration;
import agent.view.HtmlTableCreator;
import fi.iki.elonen.NanoHTTPD;
import lombok.SneakyThrows;
// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

public class Server extends NanoHTTPD{

    private static final String SIMPLE_HTML = "/transaction_table.html";
    private static final String TR_CLOSE = "</tr>";

    public Server(Configuration configuration) throws IOException {
        super(configuration.getPort());
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:/" + configuration.getPort() + "\n");
    }

    @SneakyThrows
    @Override
    public Response serve(IHTTPSession session) {
        return newFixedLengthResponse(prepareFile());
    }

    private String prepareFile() throws IOException {
        HtmlTableCreator creator = new HtmlTableCreator();
        String fileContent = readFile();

        return String.format("%s\n%s\n%s",
                fileContent.substring(0, fileContent.indexOf(TR_CLOSE)),
                        creator.createTable(DataStore.getQueryToTimeElapsed()),
                        fileContent.substring(fileContent.indexOf(TR_CLOSE)));
    }

    private static String readFile() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream htmlStream = Server.class.getResourceAsStream(Server.SIMPLE_HTML)) {
            try (Reader reader = new BufferedReader(new InputStreamReader
                    (htmlStream, StandardCharsets.UTF_8))) {
                int c = 0;
                while ((c = reader.read()) != -1) {
                    stringBuilder.append((char) c);
                }
            }
        }
        return stringBuilder.toString();
    }
}