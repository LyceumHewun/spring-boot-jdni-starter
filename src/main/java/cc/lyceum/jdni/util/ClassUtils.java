package cc.lyceum.jdni.util;

/**
 * @author Lyceum
 * @date 2022/1/17
 */
public class ClassUtils {

    public static Class<?> getClass0(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
