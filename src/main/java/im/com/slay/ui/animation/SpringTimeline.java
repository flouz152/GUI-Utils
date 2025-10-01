package im.com.slay.ui.animation;

import java.util.HashMap;
import java.util.Map;

/**
 * Timeline based on spring physics.
 */
public final class SpringTimeline {

    public interface Listener {
        void onUpdate(String key, double value);
    }

    private final Map<String, PhysicsDriver> drivers = new HashMap<String, PhysicsDriver>();
    private double stiffness = 280;
    private double damping = 26;
    private Listener listener;

    public SpringTimeline listener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public SpringTimeline stiffness(double stiffness) {
        this.stiffness = stiffness;
        return this;
    }

    public SpringTimeline damping(double damping) {
        this.damping = damping;
        return this;
    }

    public void setTarget(String key, double target) {
        PhysicsDriver driver = drivers.get(key);
        if (driver == null) {
            driver = new PhysicsDriver(target);
            drivers.put(key, driver);
        }
        driver.setVelocity(0);
        driver.setPosition(target);
        if (listener != null) {
            listener.onUpdate(key, target);
        }
    }

    public void update(double target, double deltaTime) {
        for (Map.Entry<String, PhysicsDriver> entry : drivers.entrySet()) {
            double value = entry.getValue().update(target, stiffness, damping, deltaTime);
            if (listener != null) {
                listener.onUpdate(entry.getKey(), value);
            }
        }
    }
}
