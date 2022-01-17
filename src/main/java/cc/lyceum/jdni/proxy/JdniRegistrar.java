package cc.lyceum.jdni.proxy;

import cc.lyceum.jdni.DriverType;
import cc.lyceum.jdni.annotation.EnableJdni;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
public class JdniRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attr = importingClassMetadata.getAnnotationAttributes(EnableJdni.class.getName());

        assert null != attr;
        String[] basePackage = (String[]) attr.get("basePackage");
        if (basePackage.length == 0) {
            // 如果没有设置该属性
            // 默认使用启动类包名
            basePackage = new String[]{ClassUtils.getPackageName(importingClassMetadata.getClassName())};
        }

        DriverType driverType = (DriverType) attr.get("driver");

        // JdniBeanProcessor
        BeanDefinitionBuilder builder0 = BeanDefinitionBuilder.rootBeanDefinition(JdniBeanProcessor.class.getName());
        builder0.addPropertyValue("basePackage", basePackage);
        builder0.addPropertyValue("driver", driverType.getInstance());
        registry.registerBeanDefinition(JdniBeanProcessor.class.getName(), builder0.getBeanDefinition());

        // JdniDriver
        BeanDefinitionBuilder builder1 = BeanDefinitionBuilder.rootBeanDefinition(driverType.getClazz().getName());
        registry.registerBeanDefinition(driverType.getClazz().getName(), builder1.getBeanDefinition());
    }
}
