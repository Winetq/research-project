package agent;

import agent.transformer.PgConnectionTransformer;
import agent.transformer.PgPreparedStatementTransformer;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation instr) {
        instr.addTransformer(new PgPreparedStatementTransformer("org.postgresql.jdbc.PgPreparedStatement", "executeWithFlags"));
        instr.addTransformer(new PgConnectionTransformer("org.postgresql.jdbc.PgConnection", "executeTransactionCommand"));
    }

}
