package agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class Transformer implements ClassFileTransformer {
    private final String targetClassName;
    private final String targetMethodName;

    private final Set<String> qqq = new HashSet<>();

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
                CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

                CtClass arrListClazz = classPool.get("java.util.ArrayList");
                CtField ctField = new CtField(arrListClazz, "preparedSqlStatements", ctClass);
                ctClass.addField(ctField);

                CtField f = new CtField(CtClass.booleanType, "initialized", ctClass);
                ctClass.addField(f, "false");

                classPool.importPackage("java.util");

                CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethodName);

                ctMethod.insertBefore(
                        "{" +
                        "if (!initialized) {" +
                        "preparedSqlStatements = new ArrayList();" +
                        "initialized = true;" +
                        "}" +
                        "preparedSqlStatements.add($1);" +
                        "System.err.println(preparedSqlStatements); " +
                        "}"
                );

                //  kurr generyki nie sa wspierane XDDD, wszystko jasne

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
