package cc.lyceum.jdni.proxy;

import cc.lyceum.jdni.DriverType;
import cc.lyceum.jdni.JdniDriver;
import cc.lyceum.jdni.config.JdniConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;

import java.util.Properties;

/**
 * @author Lyceum
 * @date 2022/1/17
 */
public class JdniConfigProcessor implements InitializingBean {

    private final DriverType driverType;
    private final JdniDriver driver;
    private final JdniConfig config;

    public JdniConfigProcessor(DriverType driverType, @Nullable JdniConfig config) {
        this.driverType = driverType;
        this.driver = driverType.getInstance();
        this.config = config;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null != config) {
            Properties properties = config.initProperties(driverType);
            driver.init(properties);
        }
    }
}
