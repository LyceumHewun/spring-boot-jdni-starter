package cc.lyceum.jdni;

/**
 * @author Lyceum
 * @date 2022/1/15
 */
public interface DotNetClassConstructor {

    /**
     * 获取构造函数参数
     *
     * @return 返回.Net方法的构造函数参数
     */
    Object[] getConstructorParams();
}
