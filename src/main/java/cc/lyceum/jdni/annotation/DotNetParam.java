package cc.lyceum.jdni.annotation;

import cc.lyceum.jdni.JdniTypeHandler;

import java.lang.annotation.*;

/**
 * @author Lyceum
 * @date 2022/1/15
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DotNetParam {

    /**
     * 类型处理器
     */
    Class<? extends JdniTypeHandler>[] typeHandler() default {};
}
