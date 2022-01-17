package cc.lyceum.jdni.config;

import cc.lyceum.jdni.DriverType;

import java.util.Properties;

/**
 * see {@link cc.lyceum.jdni.proxy.JdniConfigProcessor}
 *
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

    /**
     * 加载库文件
     *
     * @return 提供需要加载的库文件完整路径
     */
    String[] loadLibrary();
}
