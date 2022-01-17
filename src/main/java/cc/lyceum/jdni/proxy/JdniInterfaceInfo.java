package cc.lyceum.jdni.proxy;

import cc.lyceum.jdni.JdniTypeHandler;
import cc.lyceum.jdni.annotation.DotNetMethod;
import cc.lyceum.jdni.annotation.DotNetParam;
import cc.lyceum.jdni.util.BeanUtils;
import cc.lyceum.jdni.util.ClassUtils;
import cc.lyceum.jdni.DotNetClassConstructor;
import cc.lyceum.jdni.JdniDriver;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
public class JdniInterfaceInfo {

    /**
     * 对应的.Net类名
     */
    private final String type;
    private final JdniDriver driver;
    private final boolean staticClass;
    /**
     * .Net构造器, 提供构造函数参数
     */
    private final Class<? extends DotNetClassConstructor> constructor;

    public JdniInterfaceInfo(String type, JdniDriver driver, boolean staticClass, Class<? extends DotNetClassConstructor> constructor) {
        this.type = type;
        this.driver = driver;
        this.staticClass = staticClass;
        this.constructor = constructor;
    }

    public String getType() {
        return type;
    }

    public JdniDriver getDriver() {
        return driver;
    }

    public boolean isStaticClass() {
        return staticClass;
    }

    public
    static class Handler implements InvocationHandler {

        static final Method OBJECT_TO_STRING;
        static final Method OBJECT_HASHCODE;
        static final Method OBJECT_EQUALS;

        static {
            try {
                OBJECT_TO_STRING = Object.class.getMethod("toString");
                OBJECT_HASHCODE = Object.class.getMethod("hashCode");
                OBJECT_EQUALS = Object.class.getMethod("equals", Object.class);
            } catch (Exception e) {
                throw new Error("Error retrieving Object.toString() method");
            }
        }

        /**
         * 对应的.Net对象
         * <p>
         * 首次调用JDNI方法才会初始化
         */
        private volatile Object dotNetObj;

        private final JdniInterfaceInfo info;

        public Handler(JdniInterfaceInfo info) {
            this.info = info;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            // intercept Object methods
            if (OBJECT_TO_STRING.equals(method)) {
                return "Proxy interface to DotNet interface";
            } else if (OBJECT_HASHCODE.equals(method)) {
                return hashCode();
            } else if (OBJECT_EQUALS.equals(method)) {
                Object o = args[0];
                if (o != null && Proxy.isProxyClass(o.getClass())) {
                    return Proxy.getInvocationHandler(o) == this;
                }
                return Boolean.FALSE;
            }

            // .Net methods
            // init .Net object
            initDotNetObj();

            // method params type handler
            convertMethodParams(method.getParameters(), args);

            // choose method
            String methodName = method.getName();
            boolean isStatic = false;
            List<JdniTypeHandler> jdniTypeHandlers = new ArrayList<>();
            DotNetMethod dotNetMethod = method.getAnnotation(DotNetMethod.class);
            if (null != dotNetMethod) {
                if (StringUtils.hasText(dotNetMethod.name())) {
                    methodName = dotNetMethod.name();
                } else if (StringUtils.hasText(dotNetMethod.value())) {
                    methodName = dotNetMethod.value();
                }
                isStatic = dotNetMethod.isStatic();
                Class<? extends JdniTypeHandler>[] classOfTypeHandler = dotNetMethod.typeHandler();
                jdniTypeHandlers = Arrays.stream(classOfTypeHandler)
                        .filter(clazz -> !clazz.isInterface())
                        .map(ClassUtils::newInstance)
                        .map(JdniTypeHandler.class::cast)
                        .collect(Collectors.toList());
            }

            // method invoke
            Object returnObj;
            if (isStatic) {
                returnObj = info.driver.staticMethod(info.type, methodName, args);
            } else {
                returnObj = info.driver.method(dotNetObj, methodName, args);
            }
            for (JdniTypeHandler typeHandler : jdniTypeHandlers) {
                returnObj = typeHandler.process(returnObj);
            }
            return returnObj;
        }

        private void convertMethodParams(Parameter[] parameters, Object[] args) {
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                DotNetParam dotNetParam = parameter.getAnnotation(DotNetParam.class);
                List<JdniTypeHandler> jdniTypeHandlers = new ArrayList<>();
                if (null != dotNetParam) {
                    Class<? extends JdniTypeHandler>[] classOfTypeHandler = dotNetParam.typeHandler();
                    jdniTypeHandlers = Arrays.stream(classOfTypeHandler)
                            .filter(clazz -> !clazz.isInterface())
                            .map(ClassUtils::newInstance)
                            .map(JdniTypeHandler.class::cast)
                            .collect(Collectors.toList());
                }
                for (JdniTypeHandler typeHandler : jdniTypeHandlers) {
                    args[i] = typeHandler.process(args[i]);
                }
            }
        }

        private void initDotNetObj() {
            if (null == dotNetObj && !info.isStaticClass()) {
                synchronized (this) {
                    if (null == dotNetObj) {
                        dotNetObj = info.driver.constructor(info.type, getConstructorParams());
                    }
                }
            }
        }

        /**
         * 获取.Net对象构造参数
         */
        private Object[] getConstructorParams() {
            DotNetClassConstructor constructor = null;
            try {
                constructor = BeanUtils.getBean(info.constructor);
            } catch (NullPointerException ex) {
                // ApplicationContext are not ready
            } catch (BeansException ex) {
                // not found bean
            }
            if (null == constructor) {
                try {
                    constructor = info.constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    // new instance exception
                    ex.printStackTrace();
                }
            }
            if (null != constructor) {
                return constructor.getConstructorParams();
            }
            // try no argument
            return null;
        }
    }
}
