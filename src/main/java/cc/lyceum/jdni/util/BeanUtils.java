package cc.lyceum.jdni.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Lyceum
 * @date 2021/11/13
 */
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (null == BeanUtils.applicationContext) {
            BeanUtils.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, Object... args) {
        return applicationContext.getBean(clazz, args);
    }

    /**
     * 选择性复制字段
     *
     * @param assignFields 选择要复制的字段
     */
    public static <T> T copyProperties0(Object source, Supplier<T> supplier, String... assignFields) {
        Assert.notNull(source, "source must not be null");
        Assert.notNull(supplier, "supplier must not be null");
        Assert.notEmpty(assignFields, "at least one field must be assign");

        Field[] fields = source.getClass().getDeclaredFields();
        if (fields.length == 0) return supplier.get();

        List<String> assignList = Arrays.stream(assignFields).collect(Collectors.toList());
        String[] ignoreProperties = Arrays.stream(fields)
                .map(Field::getName)
                .filter(fieldName -> !assignList.contains(fieldName))
                .toArray(String[]::new);

        return copyProperties(source, supplier, ignoreProperties);
    }

    public static <T> T copyProperties(Object source, Supplier<T> supplier, String... ignoreProperties) {
        Assert.notNull(source, "source must not be null");
        Assert.notNull(supplier, "supplier must not be null");
        T t = supplier.get();
        org.springframework.beans.BeanUtils.copyProperties(source, t, ignoreProperties);
        return t;
    }
}
