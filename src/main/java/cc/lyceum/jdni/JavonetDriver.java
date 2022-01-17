package cc.lyceum.jdni;

import cc.lyceum.jdni.config.JdniConfig;
import com.javonet.Javonet;
import com.javonet.JavonetException;
import com.javonet.JavonetFramework;
import com.javonet.api.NObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Properties;

/**
 * @author Lyceum
 * @date 2022/1/15
 */
public class JavonetDriver implements JdniDriver {

    private static final Logger LOG = LoggerFactory.getLogger(JavonetDriver.class);

    private static volatile JavonetDriver instance;

    public static JavonetDriver getInstance() {
        if (null == instance) {
            synchronized (JavonetDriver.class) {
                if (null == instance) {
                    instance = new JavonetDriver();
                }
            }
        }
        return instance;
    }

    @Override
    public void init(Properties properties) throws JdniDriverException {
        if (!Javonet.isActivated()) {
            try {
                boolean activate = Javonet.activate(properties.getProperty(JdniConfig.JAVONET_EMAIL),
                        properties.getProperty(JdniConfig.JAVONET_LICENSE),
                        JavonetFramework.v40);
                LOG.info("Javonet driver init result: {}", activate);
            } catch (JavonetException e) {
                throw new JdniDriverException("Javonet驱动启动失败", e);
            }
        }
    }

    @Override
    public void loadLibrary(String... path) throws JdniDriverException {
        Assert.notEmpty(path, "请输入至少一个路径");
        try {
            Javonet.addReference(path);
        } catch (JavonetException e) {
            throw new JdniDriverException("加载引用失败", e);
        }
    }

    @Override
    public Object constructor(String typeName, Object... params) throws JdniDriverException {
        try {
            return Javonet.create(typeName, params);
        } catch (JavonetException e) {
            throw new JdniDriverException("构造方式调用失败", e);
        }
    }

    @Override
    public Object method(Object obj, String methodName, Object... params) throws JdniDriverException {
        Assert.notNull(obj, ".Net静态类没有构造函数, 请考虑是否使用静态方法");
        if (NObject.class.isAssignableFrom(obj.getClass())) {
            try {
                return ((NObject) obj).invoke(methodName, params);
            } catch (JavonetException e) {
                throw new JdniDriverException("方法调用失败", e);
            }
        }
        throw new JdniDriverException("方法调用失败, 对象[obj]不是Javonet对象");
    }

    @Override
    public Object staticMethod(String typeName, String methodName, Object... params) throws JdniDriverException {
        try {
            return Javonet.getType(typeName)
                    .invoke(methodName, params);
        } catch (JavonetException e) {
            throw new JdniDriverException("静态方法调用失败", e);
        }
    }
}
