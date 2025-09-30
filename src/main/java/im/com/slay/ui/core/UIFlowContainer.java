package im.com.slay.ui.core;

import im.com.slay.ui.layout.FlowLayout;
import im.com.slay.ui.layout.UILayout;

/**
 * Convenience container preconfigured with a {@link FlowLayout}.
 */
public final class UIFlowContainer extends UIContainer {

    public UIFlowContainer() {
        this(FlowLayout.horizontal());
    }

    public UIFlowContainer(FlowLayout layout) {
        super(layout);
    }

    @Override
    public void setLayout(UILayout layout) {
        if (!(layout instanceof FlowLayout)) {
            throw new IllegalArgumentException("UIFlowContainer requires FlowLayout");
        }
        super.setLayout(layout);
    }
}
