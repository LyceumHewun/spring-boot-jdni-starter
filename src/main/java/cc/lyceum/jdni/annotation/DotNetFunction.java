package cc.lyceum.jdni.annotation;

import cc.lyceum.jdni.DotNetClassConstructor;
import cc.lyceum.jdni.NoArgumentConstructor;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Lyceum
 * @date 2022/1/15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DotNetFunction {

    @AliasFor("typeName")
    String value() default "";

    /**
     * .Net类名称或接口名称, 默认为标注的类名
     */
    @AliasFor("value")
    String typeName() default "";

    /**
     * 是否静态方法
     */
    boolean isStatic() default false;

    /**
     * 构造函数
     */
    Class<? extends DotNetClassConstructor> constructor() default NoArgumentConstructor.class;
}
