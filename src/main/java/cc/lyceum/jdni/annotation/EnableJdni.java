package cc.lyceum.jdni.annotation;

import cc.lyceum.jdni.DriverType;
import cc.lyceum.jdni.config.BeansRegisterConfig;
import cc.lyceum.jdni.proxy.JdniRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        JdniRegistrar.class,
        BeansRegisterConfig.class
})
public @interface EnableJdni {

    /**
     * 要扫描的包, 默认启动类所在包
     * <p>
     * 扫描带有{@link DotNetFunction}注解的接口类
     */
    String[] basePackage() default {};

    /**
     * see {@link DriverType}
     */
    DriverType driver() default DriverType.JAVONET;
}
