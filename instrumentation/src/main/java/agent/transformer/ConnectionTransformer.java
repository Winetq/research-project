package agent.transformer;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.util.Arrays.stream;

public class ConnectionTransformer extends Transformer {
    private final String commitMethodName;
    private final String rollbackMethodName;

    public ConnectionTransformer() {
        super("Connection");
        this.commitMethodName = "commit";
        this.rollbackMethodName = "rollback";
    }

    @Override
    protected boolean checkIfImplements(byte[] classfileBuffer) throws IOException, NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
        CtClass[] ctClassInterfaces = ctClass.getInterfaces();

        if (!ctClass.isInterface() && ctClassInterfaces.length == 1 && ctClassInterfaces[0].getSimpleName().endsWith(targetInterfaceName)) { // e.g. PostgreSQL: BaseConnection, MySQl: JdbcConnection
            return stream(ctClassInterfaces[0].getInterfaces()).anyMatch(i -> i.getSimpleName().equals(targetInterfaceName));
        }

        return false;
    }

    @Override
    protected byte[] transformClass(byte[] classfileBuffer) throws CannotCompileException, IOException, NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

        CtMethod commitMethod = ctClass.getDeclaredMethod(commitMethodName, null);
        commitMethod.insertAfter("agent.DataStore.executeTransaction(this, commitQuery.getNativeSql());");

        CtMethod rollbackMethod = ctClass.getDeclaredMethod(rollbackMethodName, null);
        rollbackMethod.insertAfter("agent.DataStore.executeTransaction(this, rollbackQuery.getNativeSql());");

        ctClass.writeFile();
        ctClass.detach();
        return ctClass.toBytecode();
    }
}
