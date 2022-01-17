package cc.lyceum.jdni.proxy;

import cc.lyceum.jdni.JdniDriver;
import cc.lyceum.jdni.annotation.DotNetFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * @author Lyceum
 * @date 2022/1/15
 */
public class JdniBeanProcessor implements BeanDefinitionRegistryPostProcessor, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(JdniBeanProcessor.class);

    private final Class<? extends Annotation> annotation = DotNetFunction.class;

    private String[] basePackage;
    private JdniDriver driver;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        JdniScanner scanner = new JdniScanner(registry, driver);
        scanner.addIncludeFilter(new AnnotationTypeFilter(this.annotation));
        // 扫描
        int scanCount = scanner.scan(basePackage);
        LOG.info("Scanned {} JDNI interfaces", scanCount);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // nothing to do
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (basePackage == null)
            throw new IllegalArgumentException("the property 'basePackage' can not be null!");
    }

    public void setBasePackage(String[] basePackage) {
        this.basePackage = basePackage;
    }

    public void setDriver(JdniDriver driver) {
        this.driver = driver;
    }
}
