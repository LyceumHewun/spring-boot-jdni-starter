package cc.lyceum.jdni.config;

import cc.lyceum.jdni.DriverType;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Properties;

/**
 * @author Lyceum
 * @date 2022/1/17
 */
public class DefaultJdniConfig implements JdniConfig {

    @Value("jdni.javonet-email")
    private String javonetEmail;

    @Value("jdni.javonet-license")
    private String javonetLicense;

    @Value("jdni.lib")
    private List<String> lib;

    @Override
    public Properties initProperties(DriverType driverType) {
        Properties properties = new Properties();
        if (DriverType.JAVONET.equals(driverType)) {
            properties.put(JAVONET_EMAIL, javonetEmail);
            properties.put(JAVONET_LICENSE, javonetLicense);
        }
        return properties;
    }

    @Override
    public String[] loadLibrary() {
        return lib.toArray(new String[0]);
    }

    public void setJavonetEmail(String javonetEmail) {
        this.javonetEmail = javonetEmail;
    }

    public void setJavonetLicense(String javonetLicense) {
        this.javonetLicense = javonetLicense;
    }

    public void setLib(List<String> lib) {
        this.lib = lib;
    }
}
