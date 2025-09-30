package im.com.slay.ui.animation;

/**
 * Physics integrator used for spring simulations.
 */
public final class PhysicsDriver {

    private double velocity;
    private double position;

    public PhysicsDriver(double initialPosition) {
        this.position = initialPosition;
    }

    public double update(double target, double stiffness, double damping, double deltaTime) {
        double displacement = target - position;
        double springForce = displacement * stiffness;
        double dampingForce = -velocity * damping;
        double acceleration = springForce + dampingForce;
        velocity += acceleration * deltaTime;
        position += velocity * deltaTime;
        return position;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }
}
