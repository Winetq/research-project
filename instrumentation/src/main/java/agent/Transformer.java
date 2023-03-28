package agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {

    private final String targetClassName;

    private final ClassLoader targetClassLoader;

    public Transformer(String targetClassName, ClassLoader targetClassLoader) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        // System.out.println(className); - shows lots of classes

        byte[] byteCode = classfileBuffer;

        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/");

        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (loader.equals(targetClassLoader)) {
            System.err.println("[Agent] Transforming Calculator.");
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(targetClassName);
                CtMethod m = cc.getDeclaredMethod("add");

                m.insertBefore("System.out.println(\"[Agent] I'm here!\");");

                m.insertBefore("this.y = -8;");

                m.insertAfter("System.out.println(\"[Agent] y: \" + this.y);");

                m.insertAfter("System.out.println(\"[Agent] Hahaha!\");");

                byteCode = cc.toBytecode();
                cc.detach();
            } catch (Exception e) {
                System.err.println("[Agent] Exception - Transforming Calculator");
            }
        }

        return byteCode;
    }
}
