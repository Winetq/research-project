package agent;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

import java.util.*;


public class DataStore {
    private final static Map<String, List<Long>> queryToTimeElapsed = new HashMap<>();
    private static final String QUESTION_MARK = "?";

    public static void put(String query, long timeElapsed) {
        String queryWithWildCards = replaceParameters(query);
        List<Long> timeElapsedList = queryToTimeElapsed.computeIfAbsent(queryWithWildCards, k -> new ArrayList<>());
        timeElapsedList.add(timeElapsed);
        queryToTimeElapsed.put(queryWithWildCards, timeElapsedList);
        System.err.println(queryToTimeElapsed);
        System.err.println(queryWithWildCards + " executed in " + timeElapsed + " microseconds");
    }

    static String replaceParameters(String sql) {
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
                        Objects.equals(equalsTo.getRightExpression().toString().toLowerCase(), Boolean.FALSE.toString()) ) {
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
}
