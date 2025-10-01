package im.com.slay.ui.input;

import im.com.slay.ui.geometry.Vec2;

/**
 * Captures the state of a controller. Designed for future expansion when Labymod
 * exposes raw gamepad input. Provides helper methods for smoothing values so
 * they can directly drive animations.
 */
public final class GamepadState {

    private Vec2 leftStick = Vec2.ZERO;
    private Vec2 rightStick = Vec2.ZERO;
    private double leftTrigger;
    private double rightTrigger;

    public Vec2 getLeftStick() {
        return leftStick;
    }

    public void setLeftStick(Vec2 leftStick) {
        this.leftStick = leftStick;
    }

    public Vec2 getRightStick() {
        return rightStick;
    }

    public void setRightStick(Vec2 rightStick) {
        this.rightStick = rightStick;
    }

    public double getLeftTrigger() {
        return leftTrigger;
    }

    public void setLeftTrigger(double leftTrigger) {
        this.leftTrigger = leftTrigger;
    }

    public double getRightTrigger() {
        return rightTrigger;
    }

    public void setRightTrigger(double rightTrigger) {
        this.rightTrigger = rightTrigger;
    }

    public Vec2 smoothStick(Vec2 current, Vec2 target, double smoothing) {
        double x = current.getX() + (target.getX() - current.getX()) * smoothing;
        double y = current.getY() + (target.getY() - current.getY()) * smoothing;
        return new Vec2(x, y);
    }
}
