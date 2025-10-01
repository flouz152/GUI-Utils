package im.com.slay.ui.interop;

/**
 * Callback executed against the Java 21 classloader. Allows the main mod to
 * request services such as vector math or animation easing that are implemented
 * in the modern runtime while remaining compatible with Java 8.
 */
public interface Java21Callable<T> {
    T call(ClassLoader java21Loader) throws Exception;
}
