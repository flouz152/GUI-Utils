package im.com.slay.ui.core;

import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.geometry.Vec2;
import im.com.slay.ui.render.SurfaceRenderer;
import im.com.slay.ui.state.ObservableList;
import im.com.slay.ui.theme.ColorUtil;
import im.com.slay.ui.theme.ThemePalette;

import java.util.ArrayList;
import java.util.List;

/**
 * Virtualized list view capable of handling thousands of entries. Items are
 * represented by {@link CellRenderer} objects that lazily render content.
 */
public final class UIListView<T> extends UIComponent {

    public interface CellRenderer<T> {
        void render(T item, Rect bounds, SurfaceRenderer renderer, ThemeContext theme);
    }

    public interface ThemeContext {
        ThemePalette palette();
    }

    private final ObservableList<T> items = new ObservableList<T>();
    private final List<T> visibleItems = new ArrayList<T>();
    private double itemHeight = 36;
    private double scrollOffset;
    private CellRenderer<T> cellRenderer;

    public UIListView() {
        items.addListener(new ObservableList.Listener<T>() {
            @Override
            public void onInserted(int index, T value) {
                requestRebuild();
            }

            @Override
            public void onRemoved(int index, T value) {
                requestRebuild();
            }

            @Override
            public void onChanged(int index, T value) {
                requestRebuild();
            }

            @Override
            public void onCleared() {
                requestRebuild();
            }
        });
    }

    public UIListView<T> renderer(CellRenderer<T> renderer) {
        this.cellRenderer = renderer;
        return this;
    }

    public UIListView<T> items(List<T> newItems) {
        items.clear();
        for (int i = 0; i < newItems.size(); i++) {
            items.add(newItems.get(i));
        }
        return this;
    }

    public UIListView<T> addItem(T item) {
        items.add(item);
        return this;
    }

    public UIListView<T> itemHeight(double height) {
        this.itemHeight = height;
        return this;
    }

    public void scroll(double amount) {
        double viewport = getActualSize().getY();
        if (viewport <= 0) {
            viewport = getPreferredSize().getY();
        }
        scrollOffset = Math.max(0, Math.min(scrollOffset + amount, Math.max(0, items.size() * itemHeight - viewport)));
        rebuildVisible();
    }

    private void requestRebuild() {
        rebuildVisible();
    }

    private void rebuildVisible() {
        visibleItems.clear();
        double viewport = getActualSize().getY();
        if (viewport <= 0) {
            viewport = getPreferredSize().getY();
        }
        int visibleCount = (int) Math.ceil(viewport / itemHeight) + 1;
        int startIndex = (int) Math.floor(scrollOffset / itemHeight);
        int endIndex = Math.min(items.size(), startIndex + visibleCount);
        for (int i = startIndex; i < endIndex; i++) {
            visibleItems.add(items.get(i));
        }
    }

    @Override
    protected Vec2 onMeasure(Vec2 availableSize) {
        return new Vec2(availableSize.getX(), availableSize.getY());
    }

    @Override
    protected void onRender(final UIContext context, SurfaceRenderer renderer, Rect bounds) {
        renderer.drawRect(bounds, ColorUtil.withOpacity(getPalette().getSurfaceColor(), 0.8), 18);
        double y = bounds.getTop() - (scrollOffset % itemHeight);
        for (int i = 0; i < visibleItems.size(); i++) {
            Rect cellBounds = Rect.of(bounds.getLeft(), y, bounds.getWidth(), itemHeight);
            renderer.drawRect(cellBounds, i % 2 == 0 ? ColorUtil.withOpacity(getPalette().getBackgroundColor(), 0.6)
                    : ColorUtil.withOpacity(getPalette().getBackgroundColor(), 0.4), 12);
            if (cellRenderer != null) {
                final T item = visibleItems.get(i);
                cellRenderer.render(item, cellBounds, renderer, new ThemeContext() {
                    @Override
                    public ThemePalette palette() {
                        return getPalette();
                    }
                });
            }
            y += itemHeight;
        }
    }
}
