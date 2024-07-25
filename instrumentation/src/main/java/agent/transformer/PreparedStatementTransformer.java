package agent.transformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class PreparedStatementTransformer extends Transformer {
    private final List<String> targetMethodNames;

    public PreparedStatementTransformer() {
        super("PreparedStatement");
        this.targetMethodNames = List.of("executeQuery", "executeUpdate", "execute");
    }

    @Override
    protected byte[] transformClass(byte[] classfileBuffer) throws CannotCompileException, IOException, NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage("java.util.concurrent");
        classPool.importPackage("java.util.List");
        classPool.importPackage("java.util.ArrayList");
        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

        CtField ctFieldStart = new CtField(CtClass.longType, "start", ctClass);
        ctClass.addField(ctFieldStart);
        CtField ctFieldFinish = new CtField(CtClass.longType, "finish", ctClass);
        ctClass.addField(ctFieldFinish);
        CtField ctFieldTimeElapsed = new CtField(CtClass.longType, "timeElapsed", ctClass);
        ctClass.addField(ctFieldTimeElapsed);

        CtClass listType = classPool.get("java.util.List");
        CtField ctFieldParameters = new CtField(listType, "parameters", ctClass);
        ctClass.addField(ctFieldParameters);

        for (String method : targetMethodNames) {
            if (ctClass.getName().contains("postgresql")) {
                transformPostgreSqlMethod(ctClass, method);
            } else if (ctClass.getName().contains("mysql")) {
                transformMySqlMethod(ctClass, method, classPool);
            } else {
                System.err.println("Not supported database.");
            }
        }

        ctClass.writeFile();
        ctClass.detach();
        return ctClass.toBytecode();
    }

    private void transformPostgreSqlMethod(CtClass ctClass, String method) throws CannotCompileException, NotFoundException {
        CtMethod ctMethod = ctClass.getDeclaredMethod(method, null);
        ctMethod.insertBefore("{ start = System.nanoTime();" +
                "parameters = new ArrayList();" +
                "for (int i = 1; i <= preparedParameters.getParameterCount(); i++) parameters.add(preparedParameters.toString(i, true)); }");
        ctMethod.insertAfter("{ finish = System.nanoTime();" +
                "timeElapsed = finish - start;" +
                "agent.DataStore.processData(preparedQuery.query.toString(), parameters, connection, TimeUnit.NANOSECONDS.toMicros(timeElapsed)); }");
    }

    private void transformMySqlMethod(CtClass ctClass, String method, ClassPool pool) throws CannotCompileException, NotFoundException {
        CtMethod ctMethod = ctClass.getDeclaredMethod(method, null);
        ctMethod.addLocalVariable("preparedQuery", pool.get("com.mysql.cj.PreparedQuery"));
        ctMethod.addLocalVariable("queryBindings", pool.get("com.mysql.cj.QueryBindings"));
        ctMethod.insertBefore("{ start = System.nanoTime();" +
                "parameters = new ArrayList();" +
                "preparedQuery = ((com.mysql.cj.PreparedQuery) query);" +
                "queryBindings = preparedQuery.getQueryBindings();" +
                "for (int i = 0; i < preparedQuery.getParameterCount(); i++) parameters.add(queryBindings.getBindValues()[i].getString()); }");
        ctMethod.insertAfter("{ finish = System.nanoTime();" +
                "timeElapsed = finish - start;" +
                "agent.DataStore.processData(preparedQuery.getOriginalSql(), parameters, connection, TimeUnit.NANOSECONDS.toMicros(timeElapsed)); }");
    }
}
