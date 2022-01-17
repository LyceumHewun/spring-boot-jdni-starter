package cc.lyceum.jdni.config;

import cc.lyceum.jdni.NoArgumentConstructor;
import cc.lyceum.jdni.util.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
public class BeansRegisterConfig {

    @Bean
    @ConditionalOnMissingBean
    public BeanUtils infrastructureBeanUtil() {
        return new BeanUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public NoArgumentConstructor noArgumentConstructor() {
        return new NoArgumentConstructor();
    }

    @Bean
    @ConditionalOnMissingBean
    public JdniConfig defaultJdniConfig() {
        return new DefaultJdniConfig();
    }
}
