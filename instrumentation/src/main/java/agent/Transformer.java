package agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {
    private final String targetClassName;
    private final String targetMethodName;

    public Transformer(String targetClassName, String targetMethodName) {
        this.targetClassName = targetClassName.replaceAll("\\.", "/");
        this.targetMethodName = targetMethodName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;

        if (className.equals(targetClassName)) {
            System.err.println("[Agent] Transforming");
            try {
                ClassPool classPool = ClassPool.getDefault();
                classPool.importPackage("java.util");
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

                CtField ctField = CtField.make("private final List preparedSqlStatements = new ArrayList();", ctClass);
                ctClass.addField(ctField);

                CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethodName);
                ctMethod.insertBefore("{ preparedSqlStatements.add($1);" +
                        "System.err.println(preparedSqlStatements); }");

                ctClass.writeFile();
                byteCode = ctClass.toBytecode();
                ctClass.detach();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return byteCode;
    }
}
