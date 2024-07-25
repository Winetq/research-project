package agent.transformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public abstract class Transformer implements ClassFileTransformer {
    private final String targetInterfaceName;

    public Transformer(String targetInterfaceName) {
        this.targetInterfaceName = targetInterfaceName;
    }

    protected abstract byte[] transformClass(byte[] classfileBuffer) throws CannotCompileException, IOException, NotFoundException;

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
        List<String> allChildInterfaces = getAllChildInterfaces(ctClass);

        return !ctClass.isInterface() && allChildInterfaces.stream().anyMatch(i -> i.equals(targetInterfaceName));
    }

    private List<String> getAllChildInterfaces(CtClass parentClass) throws NotFoundException {
        List<String> childInterfaces = new ArrayList<>();
        Queue<CtClass> queue = new LinkedList<>();
        queue.add(parentClass);
        while (!queue.isEmpty()) {
            CtClass head = queue.poll();
            CtClass[] interfaces = head.getInterfaces();
            if (interfaces.length != 0) {
                childInterfaces.addAll(stream(interfaces).map(CtClass::getSimpleName).collect(toList()));
                queue.addAll(asList(interfaces));
            }
        }

        return childInterfaces;
    }
}
