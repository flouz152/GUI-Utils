package im.com.slay.ui.geometry;

/**
 * Represents an axis aligned rectangle. The class includes helpers for
 * splitting and aligning content which are frequently used when resolving
 * complex layout structures such as grids or stacks with auto fit behavior.
 */
public final class Rect {

    public static Rect of(double x, double y, double width, double height) {
        return new Rect(new Vec2(x, y), new Vec2(width, height));
    }

    private final Vec2 position;
    private final Vec2 size;

    public Rect(Vec2 position, Vec2 size) {
        this.position = position;
        this.size = size;
    }

    public Vec2 getPosition() {
        return position;
    }

    public Vec2 getSize() {
        return size;
    }

    public double getLeft() {
        return position.getX();
    }

    public double getTop() {
        return position.getY();
    }

    public double getRight() {
        return position.getX() + size.getX();
    }

    public double getBottom() {
        return position.getY() + size.getY();
    }

    public Rect inset(Insets insets) {
        Vec2 newPosition = new Vec2(position.getX() + insets.getLeft(), position.getY() + insets.getTop());
        Vec2 newSize = new Vec2(
                size.getX() - insets.getHorizontal(),
                size.getY() - insets.getVertical()
        );
        return new Rect(newPosition, newSize);
    }

    public Rect alignWithin(Rect container, double xAlign, double yAlign) {
        double nx = container.position.getX() + (container.size.getX() - size.getX()) * xAlign;
        double ny = container.position.getY() + (container.size.getY() - size.getY()) * yAlign;
        return new Rect(new Vec2(nx, ny), size);
    }

    public boolean contains(Vec2 point) {
        return point.getX() >= getLeft() && point.getX() <= getRight()
                && point.getY() >= getTop() && point.getY() <= getBottom();
    }

    @Override
    public String toString() {
        return "Rect{" + "position=" + position + ", size=" + size + '}';
    }
}
