package agent.transformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ConnectionTransformer extends Transformer {
    private final String commitMethodName;
    private final String rollbackMethodName;

    public ConnectionTransformer() {
        super("Connection");
        this.commitMethodName = "commit";
        this.rollbackMethodName = "rollback";
    }

    @Override
    protected byte[] transformClass(byte[] classfileBuffer) throws CannotCompileException, IOException, NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

        CtMethod commitMethod = ctClass.getDeclaredMethod(commitMethodName, null);
        commitMethod.insertAfter("agent.DataStore.executeTransaction(this, \"COMMIT\");");

        CtMethod rollbackMethod = ctClass.getDeclaredMethod(rollbackMethodName, null);
        rollbackMethod.insertAfter("agent.DataStore.executeTransaction(this, \"ROLLBACK\");");

        ctClass.writeFile();
        ctClass.detach();
        return ctClass.toBytecode();
    }
}
