package cc.lyceum.jdni;

/**
 * 注意要有无参构造函数
 *
 * @author Lyceum
 * @date 2022/1/15
 */
public interface JdniTypeHandler {

    /**
     * 对象转换处理
     *
     * @param obj 需要处理的对象
     * @return 处理后的对象
     */
    Object process(Object obj);
}
