package agent;

import agent.rest.RestClient;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataStore {
    private static final Map<String, List<Long>> queryToTimeElapsed = new HashMap<>();
    private static final Map<String, List<String>> queryToOriginalQueries = new HashMap<>();
    private static final RestClient restClient = new RestClient();
    private static final String QUESTION_MARK = "?";
    private static String transaction = "";
    private static String originalTransaction = "";
    private static long startTransactionTime = 0;

    public static void processData(String query, long timeElapsed, boolean autoCommit) {
        String queryWithWildcards = replaceParameters(query);
        if (autoCommit) {
            put(queryWithWildcards, timeElapsed, query);
        } else {
            transaction += queryWithWildcards + ";";
            originalTransaction += query + ";";
            if (startTransactionTime == 0) {
                startTransactionTime = System.nanoTime() - timeElapsed;
            }
        }
        System.err.println(query + " executed in " + timeElapsed + " microseconds");
    }

    public static String replaceParameters(String sql) {
        StringBuilder buffer = new StringBuilder();
        ExpressionDeParser expressionDeParser = new ExpressionDeParser() {
            @Override
            public void visit(StringValue stringValue) {
                this.getBuffer().append(QUESTION_MARK);
            }

            @Override
            public void visit(DoubleValue doubleValue) {
                this.getBuffer().append(QUESTION_MARK);
            }

            @Override
            public void visit(DateValue dateValue) {
                this.getBuffer().append(QUESTION_MARK);
            }

            @Override
            public void visit(TimestampValue timestampValue) {
                this.getBuffer().append(QUESTION_MARK);
            }

            @Override
            public void visit(EqualsTo equalsTo) {

                if(Objects.equals(equalsTo.getRightExpression().toString().toLowerCase(), Boolean.TRUE.toString()) ||
                        Objects.equals(equalsTo.getRightExpression().toString().toLowerCase(), Boolean.FALSE.toString())) {
                    equalsTo.setRightExpression(new StringValue(QUESTION_MARK));
                }
                super.visit(equalsTo);
            }

            @Override
            public void visit(LongValue longValue) {
                this.getBuffer().append(QUESTION_MARK);
            }
        };

        SelectDeParser selectDeparser = new SelectDeParser(expressionDeParser, buffer);
        expressionDeParser.setSelectVisitor(selectDeparser);
        expressionDeParser.setBuffer(buffer);
        StatementDeParser stmtDeparser = new StatementDeParser(expressionDeParser, selectDeparser, buffer);
        Statement stmt = null;
        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        stmt.accept(stmtDeparser);
        return stmtDeparser.getBuffer().toString();
    }

    public static void commitTransaction() {
        long transactionTimeElapsed = System.nanoTime() - startTransactionTime;
        put(transaction, TimeUnit.NANOSECONDS.toMicros(transactionTimeElapsed), originalTransaction);
        transaction = "";
        originalTransaction = "";
        startTransactionTime = 0;
    }

    private static void put(String key, long time, String originalQuery) {
        List<Long> timeElapsedList = queryToTimeElapsed.computeIfAbsent(key, k -> new ArrayList<>());
        timeElapsedList.add(time);
        queryToTimeElapsed.put(key, timeElapsedList);
        List<String> originalQueryList = queryToOriginalQueries.computeIfAbsent(key, k -> new ArrayList<>());
        originalQueryList.add(originalQuery);
        queryToOriginalQueries.put(key, originalQueryList);
        System.err.println(queryToTimeElapsed);
        restClient.updateTransactions(queryToTimeElapsed, queryToOriginalQueries);
    }
}
