package agent;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.err.println("[Agent] START");
        String className = "app.Calculator";
        transformClass(className, inst);
    }

    private static void transformClass(String className, Instrumentation instrumentation) {
        Class<?> targetClass;
        ClassLoader targetClassLoader;

        // see if we can get the class using forName
        try {
            targetClass = Class.forName(className);
            targetClassLoader = targetClass.getClassLoader();
            transform(targetClass, targetClassLoader, instrumentation);
            return;
        } catch (Exception ex) {
            System.err.println("Class [" + className + "] not found with forName!");
        }

        // otherwise iterate all loaded classes and find what we want
        for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
            if (clazz.getName().equals(className)) {
                targetClass = clazz;
                targetClassLoader = targetClass.getClassLoader();
                transform(targetClass, targetClassLoader, instrumentation);
                return;
            }
        }

        throw new RuntimeException("Failed to find class [" + className + "]");
    }

    private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
        Transformer dt = new Transformer(clazz.getName(), classLoader);
        instrumentation.addTransformer(dt, true);

        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Transform failed for: [" + clazz.getName() + "]", ex);
        }
    }

}
