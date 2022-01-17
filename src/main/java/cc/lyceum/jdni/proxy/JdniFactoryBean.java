package cc.lyceum.jdni.proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Proxy;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
public class JdniFactoryBean<T> implements FactoryBean<T>, InitializingBean {

    private final Class<T> jdniInterface;

    private final JdniInterfaceInfo info;

    private boolean singleton;

    /**
     * JDNI接口代理对象
     */
    private volatile T jdniProxyObject;

    public JdniFactoryBean(Class<T> jdniInterface, JdniInterfaceInfo info, boolean singleton) {
        if (null == jdniInterface || !jdniInterface.isInterface()) {
            throw new IllegalArgumentException(jdniInterface + " must be a interface or not null");
        }
        this.jdniInterface = jdniInterface;
        this.info = info;
        this.singleton = singleton;
    }

    @Override
    public T getObject() throws Exception {
        if (isSingleton()) {
            return getInstance();
        }
        return createProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return this.jdniInterface;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null == jdniInterface || !jdniInterface.isInterface())
            throw new IllegalArgumentException(jdniInterface + " must be a interface or not null");
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    /**
     * 初始化单例
     */
    private T getInstance() {
        if (isSingleton()) {
            if (null == this.jdniProxyObject) {
                synchronized (jdniInterface) {
                    if (null == this.jdniProxyObject) {
                        this.jdniProxyObject = createProxy();
                    }
                }
            }
        }
        return this.jdniProxyObject;
    }

    @SuppressWarnings("unchecked")
    private T createProxy() {
        return (T) Proxy.newProxyInstance(JdniFactoryBean.class.getClassLoader(),
                new Class[]{this.jdniInterface},
                new JdniInterfaceInfo.Handler(info));
    }
}
