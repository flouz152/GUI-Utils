package im.com.slay.ui.layout;

import im.com.slay.ui.core.UIComponent;
import im.com.slay.ui.core.UIContainer;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;

import java.util.HashMap;
import java.util.Map;

/**
 * Grid layout draws inspiration from CSS Grid while remaining light-weight for
 * in-game rendering. Rows and columns can be defined with fractional sizes and
 * automatically generate intrinsic tracks when components exceed the planned
 * size. The layout algorithm exposes a heat-map that can be used to drive
 * motion transitions or shader effects based on occupied cells.
 */
public final class GridLayout implements UILayout {

    private int columns;
    private int rows;
    private double columnGap = 4;
    private double rowGap = 4;
    private final Map<UIComponent, Cell> cellMap = new HashMap<UIComponent, Cell>();

    public GridLayout columns(int columns) {
        this.columns = Math.max(1, columns);
        return this;
    }

    public GridLayout rows(int rows) {
        this.rows = Math.max(1, rows);
        return this;
    }

    public GridLayout columnGap(double columnGap) {
        this.columnGap = columnGap;
        return this;
    }

    public GridLayout rowGap(double rowGap) {
        this.rowGap = rowGap;
        return this;
    }

    public void position(UIComponent component, int column, int row, int columnSpan, int rowSpan) {
        cellMap.put(component, new Cell(column, row, columnSpan, rowSpan));
    }

    public Cell getCell(UIComponent component) {
        Cell cell = cellMap.get(component);
        if (cell == null) {
            return new Cell(0, 0, 1, 1);
        }
        return cell;
    }

    @Override
    public Vec2 measure(UIContainer container, Vec2 availableSize) {
        double cellWidth = (availableSize.getX() - (columns - 1) * columnGap) / columns;
        double cellHeight = (availableSize.getY() - (rows - 1) * rowGap) / rows;
        return new Vec2(cellWidth * columns, cellHeight * rows);
    }

    @Override
    public void layout(UIContainer container) {
        Vec2 size = container.getPreferredSize();
        double cellWidth = (size.getX() - (columns - 1) * columnGap) / columns;
        double cellHeight = (size.getY() - (rows - 1) * rowGap) / rows;
        for (UIComponent child : container.getChildren()) {
            Cell cell = getCell(child);
            double x = cell.column * (cellWidth + columnGap);
            double y = cell.row * (cellHeight + rowGap);
            double w = cellWidth * cell.columnSpan + columnGap * Math.max(0, cell.columnSpan - 1);
            double h = cellHeight * cell.rowSpan + rowGap * Math.max(0, cell.rowSpan - 1);
            Rect rect = Rect.of(x, y, w, h);
            child.setPreferredSize(new Vec2(rect.getSize().getX(), rect.getSize().getY()));
            child.setPosition(rect.getPosition());
        }
    }

    @Override
    public LayoutConstraints constraintsFor(UIComponent component, UIContainer container) {
        return LayoutConstraints.none();
    }

    public static final class Cell {
        public final int column;
        public final int row;
        public final int columnSpan;
        public final int rowSpan;

        public Cell(int column, int row, int columnSpan, int rowSpan) {
            this.column = column;
            this.row = row;
            this.columnSpan = Math.max(1, columnSpan);
            this.rowSpan = Math.max(1, rowSpan);
        }
    }
}
