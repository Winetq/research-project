package agent;

import agent.transformer.PgConnectionTransformer;
import agent.transformer.PgPreparedStatementTransformer;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String args, Instrumentation instr) {
        if (args != null) DataStore.setDataExporter(DataExporter.of(args));
        instr.addTransformer(new PgPreparedStatementTransformer("org.postgresql.jdbc.PgPreparedStatement", "executeWithFlags"));
        instr.addTransformer(new PgConnectionTransformer("org.postgresql.jdbc.PgConnection", "executeTransactionCommand"));
        /*
        PostgreSQL: PgPreparedStatement (PreparedStatement, Statement), PgConnection (Connection)
        MySQL: ClientPreparedStatement (PreparedStatement, Statement) ConnectionImpl (Connection)

        Niestety instanceof nie przejdzie wiec to sie sprowadza do ifowania odpowiednich klas implementujacych te
        interfejsy. Ale sample-web-application-mysql moge wrzucic na githuba i sample-web-application zmienic na
        sample-web-application-postgresql a sample-application wywalic zeby bylo ze probowalem i w
        sample-web-application jeszcze pozamieniac account na Account!
         */
    }
}