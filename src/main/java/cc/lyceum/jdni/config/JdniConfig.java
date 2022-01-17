package cc.lyceum.jdni.config;

import cc.lyceum.jdni.DriverType;

import java.util.Properties;

/**
 * @author Lyceum
 * @date 2022/1/17
 */
public interface JdniConfig {

    String JAVONET_EMAIL = "javonet email";
    String JAVONET_LICENSE = "javonet license";

    /**
     * 初始化属性
     *
     * @param driverType 驱动类型
     * @return 提供JDNI驱动初始化属性
     */
    Properties initProperties(DriverType driverType);
}
