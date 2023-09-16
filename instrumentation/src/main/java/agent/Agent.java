package agent;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation instr) {
        System.err.println("[Agent] START");
        instr.addTransformer(new Transformer("org.postgresql.jdbc.PgConnection", "prepareStatement"));
    }

}
