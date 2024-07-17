package agent;

import agent.transformer.ConnectionTransformer;
import agent.transformer.PreparedStatementTransformer;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation instr) {
        if (args != null) DataStore.setDataExporter(DataExporter.of(args));
        instr.addTransformer(new PreparedStatementTransformer());
        instr.addTransformer(new ConnectionTransformer());
    }
}