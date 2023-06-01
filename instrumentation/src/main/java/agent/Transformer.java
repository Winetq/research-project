package agent;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {

    private final String targetClassName;

    private final ClassLoader targetClassLoader;
    private long startTime;
    private long endTime;

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
            System.err.println("[Agent] Transforming AccountRepository");
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(targetClassName);
                //CtMethod m2 = new CtMethod(CtPrimitiveType.intType,"methodName",new CtClass[]{},cc);

              // CtMethod m2 = CtNewMethod.make("public void getStartTime() {  }", cc);
             //   CtMethod m3 = CtNewMethod.make("public void getEndTime() { System.out.println(\"End: \" + System.currentTimeMillis()); }", cc);
              //  m2.setBody("return 1;");


                CtMethod m = cc.getDeclaredMethod("executeQuery");
                //cc.addMethod(m2);
               // System.out.println(isNative(m));
               // System.out.println(isNative(m2));
            //    cc.addMethod(m3);
                m.insertBefore("System.out.println(\"[Agent] I'm here!\");");
              //  m.insertAfter(" getEndTime(); ");

                byteCode = cc.toBytecode();
                cc.detach();
            } catch (Exception e) {
                System.err.println("[Agent] Exception - Transforming AccountRepository " );
                e.printStackTrace();
            }
        }

        return byteCode;
    }

    private void getStartTime() {
        startTime = System.currentTimeMillis();
    }

    public static boolean isNative(CtMethod method) {
        return Modifier.isNative(method.getModifiers());
    }
}
