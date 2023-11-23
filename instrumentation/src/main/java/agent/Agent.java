package agent;

import agent.configuration.ConfigurationManager;
import agent.transformer.PgConnectionTransformer;
import agent.transformer.PgPreparedStatementTransformer;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class Agent {

    private static final String CONFIGURATION_FILE = "/http.json";

    public static void premain(String args, Instrumentation instr) throws IOException {
        instr.addTransformer(new PgPreparedStatementTransformer("org.postgresql.jdbc.PgPreparedStatement", "executeWithFlags"));
        instr.addTransformer(new PgConnectionTransformer("org.postgresql.jdbc.PgConnection", "executeTransactionCommand"));
        ConfigurationManager.getInstance().loadConfigurationFile(CONFIGURATION_FILE);
        new Server(ConfigurationManager.getInstance().getConfiguration());
    }

}
