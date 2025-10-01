package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.geometry.Insets;
import im.com.slay.ui.geometry.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Flexbox-inspired layout manager capable of sophisticated alignment and
 * animation friendly transitions. Unlike a direct port of CSS flexbox this
 * implementation exposes the measured segments to allow developers to
 * interpolate between layout states for buttery smooth transitions comparable to
 * those created with Framer Motion.
 */
public final class FlexLayout implements UILayout {

    private LayoutDirection direction = LayoutDirection.LEFT_TO_RIGHT;
    private double gap = 8.0;
    private boolean wrap;

    public FlexLayout direction(LayoutDirection direction) {
        this.direction = direction;
        return this;
    }

    public FlexLayout gap(double gap) {
        this.gap = gap;
        return this;
    }

    public FlexLayout wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    public LayoutDirection getDirection() {
        return direction;
    }

    public double getGap() {
        return gap;
    }

    public boolean isWrap() {
        return wrap;
    }

    @Override
    public Vec2 measure(UIContainer container, Vec2 availableSize) {
        double totalMain = 0;
        double maxCross = 0;
        List<UIComponent> children = new ArrayList<UIComponent>(container.getChildren());
        for (UIComponent child : children) {
            LayoutConstraints constraints = constraintsFor(child, container);
            Vec2 measured = child.getPreferredSize();
            if (measured.equals(Vec2.ZERO)) {
                measured = child.onMeasure(availableSize);
            }
            Insets margin = constraints.getMargin();
            double main = mainAxisValue(measured) + margin.getHorizontal();
            double cross = crossAxisValue(measured) + margin.getVertical();
            totalMain += main + gap;
            maxCross = Math.max(maxCross, cross);
        }
        if (!children.isEmpty()) {
            totalMain -= gap;
        }
        return axisVec(totalMain, maxCross);
    }

    @Override
    public void layout(UIContainer container) {
        List<UIComponent> children = new ArrayList<UIComponent>(container.getChildren());
        double cursor = 0.0;
        double crossCursor = 0.0;
        double lineMain = 0.0;
        double lineCross = 0.0;
        List<Segment> currentLine = new ArrayList<Segment>();

        for (UIComponent child : children) {
            LayoutConstraints constraint = constraintsFor(child, container);
            Vec2 measured = child.getActualSize();
            if (measured.equals(Vec2.ZERO)) {
                measured = child.onMeasure(container.getPreferredSize());
            }
            Insets margin = constraint.getMargin();
            double main = mainAxisValue(measured) + gap + margin.getHorizontal();
            double cross = crossAxisValue(measured) + margin.getVertical();
            if (wrap && cursor + main > mainAxisValue(container.getPreferredSize()) && !currentLine.isEmpty()) {
                layoutLine(currentLine, container, cursor - gap, lineCross, crossCursor);
                crossCursor += lineCross + gap;
                cursor = 0.0;
                currentLine.clear();
                lineCross = 0.0;
            }
            currentLine.add(new Segment(child, constraint, measured));
            cursor += main;
            lineMain = cursor;
            lineCross = Math.max(lineCross, cross);
        }
        if (!currentLine.isEmpty()) {
            layoutLine(currentLine, container, lineMain - gap, lineCross, crossCursor);
        }
    }

    private void layoutLine(List<Segment> line, UIContainer container, double usedMain, double lineCross, double crossCursor) {
        double availableMain = mainAxisValue(container.getPreferredSize());
        double extra = Math.max(0, availableMain - usedMain);
        double totalGrow = 0;
        double totalShrink = 0;
        for (Segment segment : line) {
            totalGrow += segment.constraint.getGrow();
            totalShrink += segment.constraint.getShrink();
        }
        double cursor = 0.0;
        for (Segment segment : line) {
            double componentMain = mainAxisValue(segment.measured);
            if (extra > 0 && totalGrow > 0) {
                double delta = (segment.constraint.getGrow() / totalGrow) * extra;
                componentMain += delta;
            } else if (extra < 0 && totalShrink > 0) {
                double delta = (segment.constraint.getShrink() / totalShrink) * extra;
                componentMain += delta;
            }
            Vec2 size = axisVec(componentMain, lineCross - segment.constraint.getMargin().getVertical());
            segment.component.setPreferredSize(size);
            Vec2 alignment = segment.constraint.getAlignment();
            double crossPosition = crossCursor + alignment.getY() * Math.max(0, lineCross - crossAxisValue(segment.measured));
            double mainPosition = cursor + alignment.getX() * Math.max(0, componentMain - mainAxisValue(segment.measured));
            segment.component.setPosition(axisVec(mainPosition + segment.constraint.getMargin().getLeft(), crossPosition + segment.constraint.getMargin().getTop()));
            cursor += componentMain + gap;
        }
    }

    private double mainAxisValue(Vec2 vec) {
        switch (direction) {
            case TOP_TO_BOTTOM:
            case BOTTOM_TO_TOP:
                return vec.getY();
            default:
                return vec.getX();
        }
    }

    private double crossAxisValue(Vec2 vec) {
        switch (direction) {
            case TOP_TO_BOTTOM:
            case BOTTOM_TO_TOP:
                return vec.getX();
            default:
                return vec.getY();
        }
    }

    private Vec2 axisVec(double main, double cross) {
        switch (direction) {
            case TOP_TO_BOTTOM:
            case BOTTOM_TO_TOP:
                return new Vec2(cross, main);
            default:
                return new Vec2(main, cross);
        }
    }

    public List<Segment> breakdown(UIContainer container) {
        List<Segment> segments = new ArrayList<Segment>();
        for (UIComponent child : container.getChildren()) {
            segments.add(new Segment(child, constraintsFor(child, container), child.getActualSize()));
        }
        Collections.sort(segments, new Comparator<Segment>() {
            @Override
            public int compare(Segment o1, Segment o2) {
                return o1.component.toString().compareTo(o2.component.toString());
            }
        });
        return segments;
    }

    private static final class Segment {
        private final UIComponent component;
        private final LayoutConstraints constraint;
        private final Vec2 measured;

        private Segment(UIComponent component, LayoutConstraints constraint, Vec2 measured) {
            this.component = component;
            this.constraint = constraint;
            this.measured = measured;
        }
    }

    public static FlexLayout horizontal() {
        return new FlexLayout().direction(LayoutDirection.LEFT_TO_RIGHT);
    }

    public static FlexLayout vertical() {
        return new FlexLayout().direction(LayoutDirection.TOP_TO_BOTTOM);
    }

    @Override
    public LayoutConstraints constraintsFor(UIComponent component, UIContainer container) {
        if (container instanceof im.com.slay.ui.core.UIContainer) {
            return ((im.com.slay.ui.core.UIContainer) container).getConstraints(component);
        }
        return LayoutConstraints.none();
    }
}
