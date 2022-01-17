package cc.lyceum.jdni;

import cc.lyceum.jdni.proxy.JdniRegistrar;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * {@link Component} 这个注解没用的, Spring注入默认过滤底层类, 只是让IDE有提示作用,
 * 实际注入是在{@link JdniRegistrar#registerBeanDefinitions(AnnotationMetadata, BeanDefinitionRegistry)}上实现的
 *
 * @author Lyceum
 * @date 2022/1/15
 */
@Component
public interface JdniDriver {

    /**
     * 根据配置初始化驱动
     *
     * @param properties 配置
     * @throws JdniDriverException 驱动运行错误时抛出该异常
     */
    void init(Properties properties) throws JdniDriverException;

    /**
     * 加载DLL
     *
     * @param path DLL完整路径
     * @throws JdniDriverException 驱动运行错误时抛出该异常
     */
    void loadLibrary(String... path) throws JdniDriverException;

    /**
     * 映射构造方法
     *
     * @param typeName DLL对应类名
     * @param params   构造参数
     * @return DLL映射方法的返回类型
     * @throws JdniDriverException 驱动运行错误时抛出该异常
     */
    Object constructor(String typeName, Object... params) throws JdniDriverException;

    /**
     * 映射方法
     *
     * @param obj        DLL映射对象
     * @param methodName DLL映射的方法名
     * @param params     方法参数
     * @return DLL映射方法的返回类型
     * @throws JdniDriverException 驱动运行错误时抛出该异常
     */
    Object method(Object obj, String methodName, Object... params) throws JdniDriverException;

    /**
     * 映射静态方法
     *
     * @param typeName   DLL对应类名
     * @param methodName DLL映射的方法名
     * @param params     方法参数
     * @return DLL映射方法的返回类型
     * @throws JdniDriverException 驱动运行错误时抛出该异常
     */
    Object staticMethod(String typeName, String methodName, Object... params) throws JdniDriverException;
}
