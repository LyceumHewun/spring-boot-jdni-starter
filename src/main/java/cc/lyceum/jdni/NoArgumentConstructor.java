package cc.lyceum.jdni;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
public class NoArgumentConstructor implements DotNetClassConstructor {

    @Override
    public Object[] getConstructorParams() {
        return null;
    }
}
