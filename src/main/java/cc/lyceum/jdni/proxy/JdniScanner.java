package cc.lyceum.jdni.proxy;

import cc.lyceum.jdni.NoArgumentConstructor;
import cc.lyceum.jdni.annotation.DotNetFunction;
import cc.lyceum.jdni.util.ClassUtils;
import cc.lyceum.jdni.DotNetClassConstructor;
import cc.lyceum.jdni.JdniDriver;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
public class JdniScanner extends ClassPathBeanDefinitionScanner {

    private final JdniDriver driver;

    public JdniScanner(BeanDefinitionRegistry registry, JdniDriver driver) {
        // 禁用掉默认的扫描选择条件
        // 因为默认只扫描被@Component标记的类、衍生注解(@Controller、@Service、@Repository)也会被扫描
        super(registry, false);
        this.driver = driver;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isInterface();
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        // 父类方法已经向Spring容器中注册过这些BeanDefinition了, 只需修改此引用
        // 不需要再注册
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : holders) {
            convertToJdniFactoryBean(holder.getBeanDefinition());
        }
        return holders;
    }

    /**
     * 修改原有Jdni接口(.Net映射接口)的beanDefinition, 将其转化为JdniFactoryBean的beanDefinition
     */
    private void convertToJdniFactoryBean(BeanDefinition beanDefinition) {
        GenericBeanDefinition jdniFactoryBeanDefinition = (GenericBeanDefinition) beanDefinition;

        // 接口全限定类名
        String jdniInterfaceName = beanDefinition.getBeanClassName();

        // JDNI 接口信息
        Class<?> classOfBean = ClassUtils.getClass0(beanDefinition.getBeanClassName());
        String dotNetTypeName = classOfBean.getSimpleName();
        DotNetFunction dotNetType = classOfBean.getAnnotation(DotNetFunction.class);
        boolean staticClass = false;
        Class<? extends DotNetClassConstructor> constructor = NoArgumentConstructor.class;
        if (null != dotNetType) {
            if (StringUtils.hasText(dotNetType.typeName())) {
                dotNetTypeName = dotNetType.typeName();
            } else if (StringUtils.hasText(dotNetType.value())) {
                dotNetTypeName = dotNetType.value();
            }
            staticClass = dotNetType.isStatic();
            constructor = dotNetType.constructor();
        }

        JdniInterfaceInfo info = new JdniInterfaceInfo(dotNetTypeName, driver, staticClass, constructor);

        // 修改bean class
        jdniFactoryBeanDefinition.setBeanClass(JdniFactoryBean.class);
        // 传入构造函数参数
        ConstructorArgumentValues constructorArgumentValues = jdniFactoryBeanDefinition.getConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0, jdniInterfaceName);
        constructorArgumentValues.addIndexedArgumentValue(1, info);
        constructorArgumentValues.addIndexedArgumentValue(2, beanDefinition.isSingleton());
    }
}
