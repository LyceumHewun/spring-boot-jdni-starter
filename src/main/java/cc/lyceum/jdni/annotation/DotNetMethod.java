package cc.lyceum.jdni.annotation;

import cc.lyceum.jdni.JdniTypeHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Lyceum
 * @date 2022/1/15
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DotNetMethod {

    @AliasFor("name")
    String value() default "";

    /**
     * .Net方法名称, 默认为标注的方法名
     */
    @AliasFor("value")
    String name() default "";

    /**
     * 是否静态方法
     */
    boolean isStatic() default false;

    /**
     * 返回类型处理器
     */
    Class<? extends JdniTypeHandler>[] typeHandler() default {};
}
