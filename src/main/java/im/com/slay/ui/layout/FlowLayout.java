package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.geometry.Insets;
import im.com.slay.ui.geometry.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout that arranges children in rows or columns with wrapping, similar to
 * CSS flex-wrap. Helpful for tag clouds, quick command grids, etc.
 */
public final class FlowLayout implements UILayout {

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    private final Orientation orientation;
    private final double gap;
    private final double lineSpacing;

    private FlowLayout(Orientation orientation, double gap, double lineSpacing) {
        this.orientation = orientation;
        this.gap = gap;
        this.lineSpacing = lineSpacing;
    }

    public static FlowLayout horizontal() {
        return new FlowLayout(Orientation.HORIZONTAL, 8, 8);
    }

    public static FlowLayout vertical() {
        return new FlowLayout(Orientation.VERTICAL, 8, 8);
    }

    public FlowLayout gap(double gap) {
        return new FlowLayout(orientation, gap, lineSpacing);
    }

    public FlowLayout lineSpacing(double lineSpacing) {
        return new FlowLayout(orientation, gap, lineSpacing);
    }

    @Override
    public Vec2 measure(UIContainer container, Vec2 availableSize) {
        double main = 0;
        double cross = 0;
        double lineMain = 0;
        double lineCross = 0;
        List<UIComponent> children = new ArrayList<UIComponent>(container.getChildren());
        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            LayoutConstraints constraint = constraintsFor(child, container);
            Vec2 measured = child.getPreferredSize();
            if (measured.equals(Vec2.ZERO)) {
                measured = child.onMeasure(availableSize);
            }
            Insets margin = constraint.getMargin();
            double childMain = mainAxis(measured) + marginMain(margin);
            double childCross = crossAxis(measured) + marginCross(margin);
            if (lineMain > 0 && lineMain + childMain + gap > mainAxis(availableSize)) {
                main = Math.max(main, lineMain);
                cross += lineCross + lineSpacing;
                lineMain = 0;
                lineCross = 0;
            }
            lineMain += childMain + gap;
            lineCross = Math.max(lineCross, childCross);
        }
        if (!children.isEmpty()) {
            main = Math.max(main, lineMain);
            cross += lineCross;
        }
        return axisVec(main, cross);
    }

    @Override
    public void layout(UIContainer container) {
        List<UIComponent> children = new ArrayList<UIComponent>(container.getChildren());
        double availableMain = Math.max(1, mainAxis(container.getPreferredSize()));
        double lineMain = 0;
        double lineCross = 0;
        double crossOffset = 0;
        List<Segment> line = new ArrayList<Segment>();
        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            LayoutConstraints constraint = constraintsFor(child, container);
            Vec2 measured = child.getActualSize();
            if (measured.equals(Vec2.ZERO)) {
                measured = child.onMeasure(container.getPreferredSize());
            }
            Insets margin = constraint.getMargin();
            double childMain = mainAxis(measured) + marginMain(margin);
            double childCross = crossAxis(measured) + marginCross(margin);
            if (!line.isEmpty() && lineMain + childMain + gap > availableMain) {
                layoutLine(line, crossOffset);
                crossOffset += lineCross + lineSpacing;
                line.clear();
                lineMain = 0;
                lineCross = 0;
            }
            line.add(new Segment(child, constraint, measured));
            lineMain += childMain + gap;
            lineCross = Math.max(lineCross, childCross);
        }
        if (!line.isEmpty()) {
            layoutLine(line, crossOffset);
        }
    }

    private void layoutLine(List<Segment> line, double crossOffset) {
        double cursor = 0;
        for (int i = 0; i < line.size(); i++) {
            Segment segment = line.get(i);
            Insets margin = segment.constraints.getMargin();
            double mainSize = mainAxis(segment.size);
            double crossSize = crossAxis(segment.size);
            Vec2 position = axisVec(cursor + margin.getLeft(), crossOffset + margin.getTop());
            segment.component.setPosition(position);
            segment.component.setPreferredSize(axisVec(mainSize, crossSize));
            cursor += mainSize + marginMain(margin) + gap;
        }
    }

    private double mainAxis(Vec2 vec) {
        return orientation == Orientation.HORIZONTAL ? vec.getX() : vec.getY();
    }

    private double crossAxis(Vec2 vec) {
        return orientation == Orientation.HORIZONTAL ? vec.getY() : vec.getX();
    }

    private Vec2 axisVec(double main, double cross) {
        return orientation == Orientation.HORIZONTAL ? new Vec2(main, cross) : new Vec2(cross, main);
    }

    private double marginMain(Insets margin) {
        return orientation == Orientation.HORIZONTAL ? margin.getHorizontal() : margin.getVertical();
    }

    private double marginCross(Insets margin) {
        return orientation == Orientation.HORIZONTAL ? margin.getVertical() : margin.getHorizontal();
    }

    @Override
    public LayoutConstraints constraintsFor(UIComponent component, UIContainer container) {
        return container.getConstraints(component);
    }

    private static final class Segment {
        final UIComponent component;
        final LayoutConstraints constraints;
        final Vec2 size;

        Segment(UIComponent component, LayoutConstraints constraints, Vec2 size) {
            this.component = component;
            this.constraints = constraints;
            this.size = size;
        }
    }
}
