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
import java.util.List;

public class PreparedStatementTransformer implements ClassFileTransformer {
    private final String targetInterfaceName;
    private final List<String> targetMethodNames;

    public PreparedStatementTransformer() {
        this.targetInterfaceName = "PreparedStatement";
        this.targetMethodNames = List.of("executeQuery", "executeUpdate", "execute");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;

        try {
            if (checkIfImplements(classfileBuffer)) {
                System.err.println("[Agent] Transforming " + className);
                byteCode = transformClass(classfileBuffer);
            }
        } catch (CannotCompileException | IOException | NotFoundException e) {
            System.err.println(e.getMessage());
        }

        return byteCode;
    }

    private boolean checkIfImplements(byte[] classfileBuffer) throws IOException, NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
        CtClass[] ctClassInterfaces = ctClass.getInterfaces();

        return !ctClass.isInterface() && ctClassInterfaces.length == 1 && ctClassInterfaces[0].getSimpleName().equals(targetInterfaceName);
    }

    private byte[] transformClass(byte[] classfileBuffer) throws CannotCompileException, IOException, NotFoundException {
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
            transformMethod(ctClass, method);
        }

        ctClass.writeFile();
        ctClass.detach();
        return ctClass.toBytecode();
    }

    private void transformMethod(CtClass ctClass, String method) throws CannotCompileException, NotFoundException {
        CtMethod ctMethod = ctClass.getDeclaredMethod(method, null);
        ctMethod.insertBefore("{ start = System.nanoTime();" +
                "parameters = new ArrayList();" +
                "for (int i = 1; i <= preparedParameters.getParameterCount(); i++) parameters.add(preparedParameters.toString(i, true)); }");
        ctMethod.insertAfter("{ finish = System.nanoTime();" +
                "timeElapsed = finish - start;" +
                "agent.DataStore.processData(preparedQuery.query.toString(), parameters, connection, TimeUnit.NANOSECONDS.toMicros(timeElapsed)); }");
    }
}
