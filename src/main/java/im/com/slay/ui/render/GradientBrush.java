package im.com.slay.ui.render;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.theme.ColorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility for approximating gradients using repeated rectangle draws. While the
 * renderer only supports solid fills, layering enough thin segments yields a
 * smooth gradient that works both horizontally and vertically. The brush keeps
 * color stops sorted and performs color interpolation per slice.
 */
public final class GradientBrush {

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    public static final class Stop {
        private final double position;
        private final int color;

        public Stop(double position, int color) {
            this.position = Math.max(0.0, Math.min(1.0, position));
            this.color = color;
        }

        public double getPosition() {
            return position;
        }

        public int getColor() {
            return color;
        }
    }

    private final List<Stop> stops = new ArrayList<Stop>();

    public GradientBrush addStop(double position, int color) {
        stops.add(new Stop(position, color));
        Collections.sort(stops, new Comparator<Stop>() {
            @Override
            public int compare(Stop o1, Stop o2) {
                return Double.compare(o1.getPosition(), o2.getPosition());
            }
        });
        return this;
    }

    public int sample(double position) {
        if (stops.isEmpty()) {
            return 0xFFFFFFFF;
        }
        if (stops.size() == 1) {
            return stops.get(0).getColor();
        }
        double clamped = Math.max(0.0, Math.min(1.0, position));
        Stop previous = stops.get(0);
        for (int i = 1; i < stops.size(); i++) {
            Stop next = stops.get(i);
            if (clamped <= next.getPosition()) {
                double range = next.getPosition() - previous.getPosition();
                double local = range <= 0 ? 0.0 : (clamped - previous.getPosition()) / range;
                return ColorUtil.lerp(previous.getColor(), next.getColor(), local);
            }
            previous = next;
        }
        return stops.get(stops.size() - 1).getColor();
    }

    public void fill(SurfaceRenderer renderer, Rect bounds, Orientation orientation, double cornerRadius, int steps) {
        int slices = Math.max(1, steps);
        if (slices == 1) {
            renderer.drawRect(bounds, sample(0.5), cornerRadius);
            return;
        }
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        for (int i = 0; i < slices; i++) {
            double t = i / (double) (slices - 1);
            int color = sample(t);
            if (orientation == Orientation.VERTICAL) {
                double sliceHeight = height / slices;
                Rect slice = Rect.of(bounds.getLeft(), bounds.getTop() + i * sliceHeight, width, sliceHeight + 1.0);
                renderer.drawRect(slice, color, cornerRadius);
            } else {
                double sliceWidth = width / slices;
                Rect slice = Rect.of(bounds.getLeft() + i * sliceWidth, bounds.getTop(), sliceWidth + 1.0, height);
                renderer.drawRect(slice, color, cornerRadius);
            }
        }
    }

    public void fillRadial(SurfaceRenderer renderer, Rect bounds, int rings) {
        int slices = Math.max(1, rings);
        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();
        double maxRadius = Math.max(bounds.getWidth(), bounds.getHeight()) * 0.5;
        for (int i = slices - 1; i >= 0; i--) {
            double t = i / (double) Math.max(1, slices - 1);
            int color = sample(t);
            double radius = maxRadius * (i + 1) / (double) slices;
            Rect circle = Rect.of(centerX - radius, centerY - radius, radius * 2.0, radius * 2.0);
            renderer.drawCircle(circle, color, 1.0);
        }
    }
}
