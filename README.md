# spring-boot-jdni-starter

Java to .Net Interface based on Spring Boot. Call managed DLLs using JNI, Javonet or Jacob as the driver. Native DLLs (Unmanaged DLLs) are not supported.

## TODO

- [x] support DLL lib
- [ ] support SO lib
- [x] support Javonet Driver
- [ ] support Jacob Driver
- [ ] support JNA Driver
- [ ] custom selection .Net framework (currently only 4.0 is supported)
- [ ] support more javanet authentication methods

## Starter

- Maven

```xm
// not yet
```

## Using

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

Build interface class, the name corresponds to the .Net type name. Write .Net mapping interface, method name must be  corresponds to the .Net method. Method parameter names do not have to correspond, only the same count, but pay attention to the type.

Most importantly add `@DotNetFunction` annotation on your interface. Modify the annotation `@EnableJdni(basePackage = {})` to select the package to be scanned.

```java
@DotNetFunction
public interface DotNetTypeName {

    String DotNetMethod(String arg0, int arg1);
}
```

