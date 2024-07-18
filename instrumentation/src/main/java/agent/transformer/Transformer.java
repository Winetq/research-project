package agent.transformer;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public abstract class Transformer implements ClassFileTransformer {
    protected final String targetInterfaceName;

    public Transformer(String targetInterfaceName) {
        this.targetInterfaceName = targetInterfaceName;
    }

    protected abstract boolean checkIfImplements(byte[] classfileBuffer) throws IOException, NotFoundException;

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
}
