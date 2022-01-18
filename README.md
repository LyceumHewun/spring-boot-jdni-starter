# spring-boot-jdni-starter

## What is JDNI

JNDI here refers to Java to .Net Interface, not Java Naming and Directory Interface.

## Overview

Java to .Net Interface based on Spring Boot. Call managed DLL using Javonet or Jacob as the driver. Native DLL (Unmanaged DLL) are not supported.

## TODO

- [x] support DLL lib
- [ ] support SO lib
- [x] support [Javonet](https://www.javonet.com/) Driver
- [ ] support [Jacob](https://github.com/freemansoft/jacob-project) Driver
- [ ] suppor [JCOBridge](https://www.jcobridge.com/) Driver
- [ ] support [JNA](https://github.com/java-native-access/jna) Driver
- [ ] custom selection .Net framework (currently only 4.0 is supported)
- [ ] support more javanet authentication methods

## Starter

- Maven

```xm
// not yet
```

## Usage

### 1. Introduce supported dependency drivers

Currently only supported Javonet Driver.

### 2. Add annotation on your startup application

Add `@EnableJdni` annotation on startup application, and you can enter the `driver()` value to select the JDNI driver, default use Javonet driver.

```java
@EnableJdni(driver = DriverType.JAVONET)
public class MyApplication
```

### 3. Write interface class

- Simplest interface class

Build interface class, the name corresponds to the *.Net* type name. Write *.Net* mapping interface, method name must be  corresponds to the .Net method. Method parameter names do not have to correspond, **only the same count, but pay attention to the type.**

*Most importantly add `@DotNetFunction` annotation on your interface.* Modify the annotation `@EnableJdni(basePackage = {})` to select the package to be scanned.

```java
@DotNetFunction
public interface DotNetTypeName {

    String DotNetMethod(String arg0, int arg1);
}
```

## Advanced

### 1. Configure

- Use default configuration class

The [default config class](https://github.com/LyceumHewun/spring-boot-jdni-starter/blob/master/src/main/java/cc/lyceum/jdni/config/DefaultJdniConfig.java) supports configuration in the form of properties file, the following is an example of *yaml*.

```yaml
jdni:
  lib:
    - "System.IO"
    - "D:\\MyLib.dll"
  javonet-email: ${javonet.email}
  javonet-license: ${javonet.license}
```

- Implement [JdniConfig](https://github.com/LyceumHewun/spring-boot-jdni-starter/blob/2ef3c50da89da84f4c278a3c69eceb435500fc05/src/main/java/cc/lyceum/jdni/config/JdniConfig.java) and inject into spring container

The JdniConfig implementation class will be used in the [JdniConfigProcessor](https://github.com/LyceumHewun/spring-boot-jdni-starter/blob/master/src/main/java/cc/lyceum/jdni/proxy/JdniConfigProcessor.java) class.

```java
@Configration
public class MyJdniConfig implements JdniConfig {
    
    @Override
    public Properties initProperties(DriverType driverType) {
        // returned here will be used by JdniDriver.init(Properties)
    }

    @Override
    public String[] loadLibrary() {
        // returned here will be used by JdniDriver.loadLibrary(Path...)
    }
}
```

### 2. More annotation usage

- ***@DotNetFunction***
  - The `typeName` property is the mapped *.Net* type name, if not set, it **defaults to the interface name** of the annotation.
  - `isStatic` is set to true, the .Net object will not be instantiated.
  - The `constructor` property sets the class that can return the parameters required by the constructor, which must be **constructed without parameters** or **injected into the *Spring Boot* container**.

- ***@DotNetMethod***
  - The `name` property  is the mapping *.Net* method name, if not set, it **defaults to the interface method name**.
  - `typeHandler` sets the method return type handler, which specifically handles the value returned by the JNDI driver. 

```java
@DotNetFunction
public interface DotNetTypeName {

    // normal return Integer
    Integer DotNetMethod();
    
    // is processed as String
    @DotNetMethod(typeHandler = MyTypeHandler.class)
    String DotNetMethod();
}

public class MyTypeHandler implements JdniTypeHandler {
    
    @Override
    public Object process(Object obj) {
        return String.valueOf(obj);
    }
}
```

- ***@DotNetParam***
  - `typeHandler` is different from `@DotNetMethod`, this handles input parameters.

