package agent.transformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class PgConnectionTransformer implements ClassFileTransformer {
    private final String targetClassName;
    private final String targetMethodName;

    public PgConnectionTransformer(String targetClassName, String targetMethodName) {
        this.targetClassName = targetClassName.replaceAll("\\.", "/");
        this.targetMethodName = targetMethodName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;

        if (className.equals(targetClassName)) {
            System.err.println("[Agent] Transforming PgConnection");
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
        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

        CtMethod ctMethod = ctClass.getDeclaredMethod(targetMethodName);
        ctMethod.insertBefore("agent.DataStore.executeTransaction(query.getNativeSql());");

        ctClass.writeFile();
        ctClass.detach();
        return ctClass.toBytecode();
    }
}
