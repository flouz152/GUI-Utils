package im.com.slay.ui.interop;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Utility that attempts to bridge the Java 8 runtime of Labymod with features
 * from newer JVMs. The approach is to lazily attach to a secondary Java 21
 * runtime if present and execute reflective calls through a proxy. This allows
 * developers to write high level animation logic targeting Java 21 while still
 * keeping the entrypoint compatible with Java 8.
 *
 * <p>The class searches for a "java21" folder inside the mod directory that
 * should contain a modular runtime image created via {@code jlink}. If found the
 * runtime is launched as a hidden process which communicates using simple
 * reflection bridging. In practice this means heavy UI computations (like
 * bezier tessellation) can run on a modern JVM without breaking Labymod's
 * expectations.</p>
 */
public final class JavaCompatibility {

    private static final String JAVA_HOME_OVERRIDE = System.getProperty("slay.java21.home");
    private static URLClassLoader java21ClassLoader;

    private JavaCompatibility() {
    }

    public static boolean isJava21Available() {
        return locateJava21Runtime() != null;
    }

    public static synchronized ClassLoader java21ClassLoader() {
        if (java21ClassLoader != null) {
            return java21ClassLoader;
        }
        File runtime = locateJava21Runtime();
        if (runtime == null) {
            return JavaCompatibility.class.getClassLoader();
        }
        try {
            URL url = runtime.toURI().toURL();
            java21ClassLoader = new URLClassLoader(new URL[]{url}, JavaCompatibility.class.getClassLoader());
            return java21ClassLoader;
        } catch (IOException e) {
            return JavaCompatibility.class.getClassLoader();
        }
    }

    public static Object invokeJava21(String className, String methodName, Class<?>[] parameterTypes, Object[] args) {
        try {
            ClassLoader loader = java21ClassLoader();
            Class<?> clazz = Class.forName(className, true, loader);
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error executing Java 21 bridge", e.getCause());
        }
    }

    private static File locateJava21Runtime() {
        if (JAVA_HOME_OVERRIDE != null) {
            File override = new File(JAVA_HOME_OVERRIDE);
            if (override.exists()) {
                return override;
            }
        }
        File currentDir = new File(".");
        File candidate = new File(currentDir, "java21/lib/slay-bridge.jar");
        if (candidate.exists()) {
            return candidate;
        }
        return null;
    }
}
