package im.com.slay.ui.animation;

/**
 * Utility to compute critically damped spring animations. The implementation is
 * deterministic and avoids allocations so it can run inside tight render loops.
 */
public final class SpringEquation {

    private double velocity;
    private double position;
    private double target;
    private double tension = 200;
    private double friction = 20;

    public SpringEquation position(double position) {
        this.position = position;
        return this;
    }

    public SpringEquation target(double target) {
        this.target = target;
        return this;
    }

    public SpringEquation tension(double tension) {
        this.tension = tension;
        return this;
    }

    public SpringEquation friction(double friction) {
        this.friction = friction;
        return this;
    }

    public double update(double deltaTime) {
        double displacement = position - target;
        double springForce = -tension * displacement;
        double dampingForce = -friction * velocity;
        double acceleration = springForce + dampingForce;
        velocity += acceleration * deltaTime;
        position += velocity * deltaTime;
        return position;
    }

    public boolean isAtRest(double epsilon) {
        return Math.abs(velocity) < epsilon && Math.abs(position - target) < epsilon;
    }
}
