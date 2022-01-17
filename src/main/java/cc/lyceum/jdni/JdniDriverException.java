package cc.lyceum.jdni;

/**
 * .Net应用错误或者Jdni驱动（.Net bridge）错误
 *
 * @author Lyceum
 * @date 2022/1/15
 */
public class JdniDriverException extends RuntimeException {

    public JdniDriverException() {
    }

    public JdniDriverException(String message) {
        super(message);
    }

    public JdniDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdniDriverException(Throwable cause) {
        super(cause);
    }

    public JdniDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
