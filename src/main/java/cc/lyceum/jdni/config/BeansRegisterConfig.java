package cc.lyceum.jdni.config;

import cc.lyceum.jdni.util.BeanUtils;
import cc.lyceum.jdni.NoArgumentConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
@Configuration("jdniBeansRegisterConfig")
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
}
