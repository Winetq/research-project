package agent.transformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class PgPreparedStatementTransformer implements ClassFileTransformer {
    private final String targetClassName;
    private final String targetMethodName;

    public PgPreparedStatementTransformer(String targetClassName, String targetMethodName) {
        this.targetClassName = targetClassName.replaceAll("\\.", "/");
        this.targetMethodName = targetMethodName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;

        if (className.equals(targetClassName)) {
            System.err.println("[Agent] Transforming PgPreparedStatement");
            try {
                byteCode = transform(classfileBuffer);
            } catch (CannotCompileException | IOException | NotFoundException e) {
                System.err.println(e.getMessage());
            }
        }

        return byteCode;
    }

    private byte[] transform(byte[] classfileBuffer) throws CannotCompileException, IOException, NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage("java.util.concurrent");
        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

        CtField ctFieldStart = new CtField(CtClass.longType, "start", ctClass);
        ctClass.addField(ctFieldStart);
        CtField ctFieldFinish = new CtField(CtClass.longType, "finish", ctClass);
        ctClass.addField(ctFieldFinish);
        CtField ctFieldTimeElapsed = new CtField(CtClass.longType, "timeElapsed", ctClass);
        ctClass.addField(ctFieldTimeElapsed);

        CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethodName);
        ctMethod.insertBefore("start = System.nanoTime();");
        ctMethod.insertAfter("{ finish = System.nanoTime();" +
                "timeElapsed = finish - start;" +
                "agent.DataStore.processData(preparedQuery.query.toString(), TimeUnit.NANOSECONDS.toMicros(timeElapsed), connection.getAutoCommit()); }");

        ctClass.writeFile();
        ctClass.detach();
        return ctClass.toBytecode();
    }
}
