package cc.lyceum.jdni;

/**
 * @author Lyceum
 * @date 2022/1/16
 */
public enum DriverType {

    /**
     * https://www.javonet.com/
     */
    JAVONET("javonet", JavonetDriver.class) {
        @Override
        public JdniDriver getInstance() {
            return JavonetDriver.getInstance();
        }
    },

    /**
     * https://github.com/freemansoft/jacob-project
     */
    JACOB("jacob", null),

    /**
     * https://www.jcobridge.com/
     */
    JCOB("JCOBridge", null),

    /**
     * https://github.com/java-native-access/jna
     */
    JNA("Java-Native-Access", null);

    private final String name;
    private final Class<? extends JdniDriver> clazz;

    DriverType(String name, Class<? extends JdniDriver> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<? extends JdniDriver> getClazz() {
        return clazz;
    }

    public JdniDriver getInstance() {
        throw new IllegalArgumentException("method not implemented");
    }
}
