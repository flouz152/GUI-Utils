package im.com.slay.ui.core;

import im.com.slay.ui.layout.ConstraintLayout;
import im.com.slay.ui.layout.UILayout;

/**
 * Container that wires {@link ConstraintLayout} with child-specific constraints.
 * Developers can use {@link #constraints(UIComponent)} to obtain a mutable
 * constraints object for each child, similar to Android's ConstraintLayout API.
 */
public final class UIConstraintContainer extends UIContainer {

    private final ConstraintLayout constraintLayout;

    public UIConstraintContainer() {
        this(new ConstraintLayout());
    }

    public UIConstraintContainer(ConstraintLayout constraintLayout) {
        super(constraintLayout);
        this.constraintLayout = constraintLayout;
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }

    public ConstraintLayout.Constraints constraints(UIComponent child) {
        ConstraintLayout.Constraints constraints = new ConstraintLayout.Constraints();
        constraintLayout.setConstraints(child, constraints);
        return constraints;
    }

    @Override
    public void setLayout(UILayout layout) {
        if (!(layout instanceof ConstraintLayout)) {
            throw new IllegalArgumentException("UIConstraintContainer requires a ConstraintLayout instance");
        }
        super.setLayout(layout);
    }
}
